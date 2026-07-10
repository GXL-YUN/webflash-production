package cn.enilu.flash.service.system;

import cn.enilu.flash.bean.constant.cache.Cache;
import cn.enilu.flash.bean.constant.cache.CacheKey;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.entity.system.FileInfo;
import cn.enilu.flash.bean.enumeration.ConfigKeyEnum;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.cache.ConfigCache;
import cn.enilu.flash.cache.TokenCache;
import cn.enilu.flash.dao.system.FileInfoRepository;
import cn.enilu.flash.security.JwtUtil;
import cn.enilu.flash.service.BaseService;
import cn.enilu.flash.utils.XlsUtils;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.pet.bean.model.Demand;
import cn.enilu.pet.em.DemandStatus;
import cn.enilu.util.PdfUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class FileService extends BaseService<FileInfo, String, FileInfoRepository> {
    @Autowired
    private ConfigCache configCache;
    @Autowired
    private FileInfoRepository fileInfoRepository;
    @Autowired
    private TokenCache tokenCache;


    @Value("${urlpath}")
    private String configValue;


    /**
     *
     * @param fdId
     * @param className
     * @param key
     * @return根据单据id   类路径  关键字   查询对应的附件
     */
    public List<FileInfo> getListByIdFile(String fdId, String className, String key){
        Page<FileInfo> page = new PageFactory<FileInfo>().defaultPage();
        page.addFilter("fdModelId", SearchFilter.Operator.EQ, fdId, SearchFilter.Join.and);
        page.addFilter("fdKey", SearchFilter.Operator.EQ, key, SearchFilter.Join.and);
        page.addFilter("fdModelName", SearchFilter.Operator.EQ, className, SearchFilter.Join.and);
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        page.setSort(sort);
        Page<FileInfo> date= this.queryPage(page);
        return page.getRecords();
    }


    /**
     *
     * @param fdId
     * @param list
     * @return根据单据id
     */
    public void getListByIdFileChangeMainId(String fdId, List<String>   list){
        if(list!=null&&list.size()>0){
            for(String fdFileId:list){
                FileInfo main= this.getById(fdFileId);
                main.setFdModelId(fdId);
                this.update(main);
                log.info("附件更新成功{}",fdFileId);
            }
        }
    }


    /**
     * 批量上传上传附件
     */

    public void addAttList(List<MultipartFile> multipartFile, String fdKey, String fdModelId) {
        if(multipartFile.size()>0){
            for (MultipartFile mul:multipartFile){
                this.upload(mul,fdKey,fdModelId);
            }
        }
    }
    /**
     * 文件上传
     *
     * @param multipartFile
     * @return
     */
    public FileInfo upload(MultipartFile multipartFile, String fdKey, String fdModelName) {
        // 参数校验
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        InputStream inputStream = null;
        try {
            // 生成UUID
            String uuid = UUID.randomUUID().toString();
            log.info("originalFilename={}", multipartFile.getOriginalFilename());
            log.info("name={}", multipartFile.getName());
            log.info("contentType={}", multipartFile.getContentType());

            // 安全获取文件扩展名
            String originalFilename = multipartFile.getOriginalFilename();
            if (originalFilename == null || !originalFilename.contains(".")) {

                // 1. 从 content-type 取后缀（最靠谱）
                String contentType = multipartFile.getContentType();
                String ext = ".jpg";

                if (contentType != null) {
                    if (contentType.contains("png")) ext = ".png";
                    else if (contentType.contains("jpeg")) ext = ".jpg";
                    else if (contentType.contains("gif")) ext = ".gif";
                }

                // 2. UUID 文件名
                originalFilename = UUID.randomUUID() + ext;
                log.info("文件名不合法，从新生成文件名称={}", originalFilename);
                //throw new IllegalArgumentException("文件名不合法");
            }

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String realFileName = uuid + "." + fileExtension;

            // 创建目标文件
            String path = configValue;
           //path + File.separator + realFileName;


            File file = new File(path + File.separator + realFileName);
            String pateUrl=file.getPath();

            // 确保目录存在
            File parentDir = file.getParentFile();
            if (!parentDir.exists() && !parentDir.mkdirs()) {
                throw new IOException("无法创建目录: " + parentDir.getAbsolutePath());
            }

            // 使用try-with-resources确保流关闭
            inputStream = multipartFile.getInputStream();
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return save(originalFilename, file, fdKey, fdModelName,pateUrl);

        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        } finally {
            // 确保关闭输入流
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // 记录日志但不要抛出异常
                    //log.error("关闭输入流失败", e);
                }
            }
        }
    }
    public FileInfo upload(MultipartFile multipartFile) {
        //生成uUid
        String uuid = UUID.randomUUID().toString();
        //文件名称
        String realFileName = uuid + "." + multipartFile.getOriginalFilename().split("\\.")[1];
        try {
            File file = new File(configCache.get(ConfigKeyEnum.SYSTEM_FILE_UPLOAD_PATH) + File.separator + realFileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            multipartFile.transferTo(file);
            return save(multipartFile.getOriginalFilename(), file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据模板创建excel文件
     *
     * @param template excel模板
     * @param fileName 导出的文件名称
     * @param data     excel中填充的数据
     * @return
     */
    public FileInfo createExcel(String template, String fileName, Map<String, Object> data) {
        FileOutputStream outputStream = null;
        File file = new File(configCache.get(ConfigKeyEnum.SYSTEM_FILE_UPLOAD_PATH) + File.separator + UUID.randomUUID().toString() + ".xlsx");
        try {
            File directory = file.getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // 定义输出类型
            outputStream = new FileOutputStream(file);

            JxlsHelper jxlsHelper = JxlsHelper.getInstance();
            String templateFile = getClass().getClassLoader().getResource(template).getPath();
            InputStream is = new BufferedInputStream(new FileInputStream(templateFile));

            Transformer transformer = jxlsHelper.createTransformer(is, outputStream);
            Context context = new Context();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                context.putVar(entry.getKey(), entry.getValue());
            }

            JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
            Map<String, Object> funcs = new HashMap<String, Object>(4);
            funcs.put("utils", new XlsUtils());
            evaluator.getJexlEngine().setFunctions(funcs);
            jxlsHelper.processTemplate(context, transformer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return save(fileName, file);
    }

    /**
     * 创建文件
     *
     * @param originalFileName
     * @param file
     * @return
     */
    public FileInfo save(String originalFileName, File file,String fdKey,String fdModelName,String url ) {
        try {
            FileInfo fileInfo = new FileInfo();
           // fileInfo.setCreateBy(JwtUtil.getUserId());
            fileInfo.setOriginalFileName(originalFileName);
            fileInfo.setRealFileName(file.getName());
            fileInfo.setUrl(url);
            fileInfo.setFdKey(fdKey);
            fileInfo.setFdModelName(fdModelName);
            //doc转换为pdf
            //判断类型
            insert(fileInfo);
            //docToPdf(fileInfo.getId());
            return fileInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public FileInfo save(String originalFileName, File file) {
        try {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setCreateBy(JwtUtil.getUserId());
            fileInfo.setOriginalFileName(originalFileName);
            fileInfo.setRealFileName(file.getName());
            //doc转换为pdf
            //判断类型
            insert(fileInfo);
            //docToPdf(fileInfo.getId());
            return fileInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * doc转换为pdf
     */
    public void docToPdf(String idFile) {

        try {
            FileInfo fileInfo = get(idFile);
            //String s= PdfUtil.getType(fileInfo.getAblatePath());
            String docPath = fileInfo.getAblatePath();

            String[] parts = fileInfo.getRealFileName().split("\\.");
            String result = parts[0]; // This will store the part before the decimal point in the "result" variable.
            if ("docx".equals(parts[1])) {

                String pdfPath = configCache.get(ConfigKeyEnum.SYSTEM_FILE_UPLOAD_PATH) + "\\pdf\\" + result + ".pdf";
                PdfUtil.doc2pdf(docPath, pdfPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("类型错误");
        }
    }

    @Override
    public void delete(String aLong) {
        super.delete(aLong);
    }

    @Override
    @Cacheable(value = Cache.APPLICATION, key = "'" + CacheKey.FILE_INFO + "'+#id")
    public FileInfo get(String id) {
        FileInfo fileInfo = fileInfoRepository.getOne(id);


        fileInfo.setAblatePath(configCache.get(ConfigKeyEnum.SYSTEM_FILE_UPLOAD_PATH) + File.separator + fileInfo.getRealFileName());
        return fileInfo;
    }
}
