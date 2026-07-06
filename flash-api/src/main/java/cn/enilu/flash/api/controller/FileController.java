package cn.enilu.flash.api.controller;

import cn.enilu.flash.bean.entity.system.FileInfo;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.service.system.FileService;
import cn.enilu.flash.utils.*;
import cn.enilu.util.PicUtil;
import org.apache.poi.util.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/file/att")
public class FileController extends BaseController {
    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private FileService fileService;

    /**
     * 上传文件
     *
     * @param multipartFile
     * @return
     */
    @PostMapping
    @RequiresPermissions(value = {Permission.FILE_UPLOAD})
    public Object upload(@RequestPart("file") MultipartFile multipartFile) {
        try {
            FileInfo fileInfo = fileService.upload(multipartFile);
            return Rets.success(fileInfo);
        } catch (Exception e) {
            logger.error("上传文件异常", e);
            return Rets.failure("上传文件失败");
        }
    }

    /**
     * 下载文件
     *
     * @param idFile
     * @param fileName
     */
    @GetMapping(value = "download")
    public void download(@RequestParam("idFile") String idFile,
                         @RequestParam(value = "fileName", required = false) String fileName) {
        FileInfo fileInfo = fileService.get(idFile);
        fileName = StringUtil.isEmpty(fileName) ? fileInfo.getOriginalFileName() : fileName;
        HttpServletResponse response = HttpUtil.getResponse();
        response.setContentType("application/x-download");
        try {
            fileName = new String(fileName.getBytes(), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int length = 1024;
        byte[] buffer = new byte[length];
        FileInputStream fis = null;
        BufferedInputStream bis = null;

        OutputStream os = null;
        try {
            File file = new File(fileInfo.getAblatePath());
            os = response.getOutputStream();
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                buffer = new byte[length];
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            logger.error("download error", e);
        } finally {
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                logger.error("close inputstream error", e);
            }
        }

    }



    /**
     * 下载文件
     *
     * @param fdId
     */
    @GetMapping(value = "/get/download")
    public void download(@RequestParam("fdId") String fdId) {
        FileInfo fileInfo = fileService.getById(fdId);
        HttpServletResponse response = HttpUtil.getResponse();
        response.setContentType("application/x-download");
        String fileName="";
        try {
            fileName = new String(fileInfo.getOriginalFileName().getBytes(), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int length = 1024;
        byte[] buffer = new byte[length];
        FileInputStream fis = null;
        BufferedInputStream bis = null;

        OutputStream os = null;
        try {
            File file = new File(fileInfo.getAblatePath());
            os = response.getOutputStream();
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                buffer = new byte[length];
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            logger.error("download error", e);
        } finally {
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                logger.error("close inputstream error", e);
            }
        }

    }


    /**
     * 获取base64图片数据
     *
     * @param idFile
     * @return
     */
    @GetMapping(value = "getImgBase64")
    public Object getImgBase64(@RequestParam("idFile") String idFile) {

        FileInfo fileInfo = fileService.get(idFile);
        FileInputStream fis = null;
        try {
            File file = new File(fileInfo.getAblatePath());
            byte[] bytes = new byte[(int) file.length()];
            fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(bytes);
            String base64 = CryptUtil.encodeBASE64(bytes);
            return Rets.success(Maps.newHashMap("imgContent", base64));
        } catch (Exception e) {
            logger.error("get img error", e);
            return Rets.failure("获取图片异常");
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
                logger.error("close getImgBase64 error", e);
            }
        }

    }

    /**
     * 获取文件流
     *
     * @param response
     * @param idFile
     */
    @GetMapping(value = "getImgStream")
    public void getImgStream(HttpServletResponse response,
                             @RequestParam("idFile") String idFile) {
        FileInfo fileInfo = fileService.get(idFile);
        FileInputStream fis = null;
        String suffix = "." + fileInfo.getRealFileName().split("\\.")[1];
        String contentType = ContentType.get(suffix);
        response.setContentType(contentType);
        try {
            OutputStream out = response.getOutputStream();
            System.out.println("路径"+fileInfo.getAblatePath());
            File file = new File(fileInfo.getAblatePath());
            fis = new FileInputStream(file);
            //byte[] b = new byte[fis.available()];
            byte[] b = null;
            BufferedImage buff = PicUtil.fileStreamToBufferedImage(fis);
            BufferedImage change = PicUtil.resizeImage(buff, 614, 200);
            System.out.println("图片名称"+fileInfo.getOriginalFileName());
            String [] arr=fileInfo.getOriginalFileName().split(".");
           if( fileInfo.getOriginalFileName().indexOf("png")>-1){

               b = PicUtil.imageToBytes(change,"jpg");
               System.out.println("图片类型jpg");
           }else{

               b = PicUtil.imageToBytes(change,"png");
               System.out.println("图片类型png");
           }

            //System.out.println("图片类型"+arr[1]);
           // b = PicUtil.imageToBytes(change,arr[1]);

            fis.read(b);
            out.write(b);
            out.flush();
        } catch (Exception e) {
            logger.error("getImgStream error", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error("close getImgStream error", e);
                }
            }
        }
    }


    /**
     * 获取视频流
     */

    @GetMapping("/getVideo.do")
    public ResponseEntity<Resource> getVideo(HttpServletResponse response,
                         @RequestParam("idFile") String idFile) {
        FileInfo fileInfo = fileService.get(idFile);
        String url =fileInfo.getAblatePath();
       String type= fileInfo.getRealFileName().indexOf("mp4")>0?"mp4":"ogg";
        Path videoPath = Paths.get(url); // 视频文件路径
        Resource resource = null;
        try {
            resource = new UrlResource(videoPath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()

                        .body(resource);
            } else {
                // 视频文件未找到或不可读，可以返回404或其他错误状态
                return ResponseEntity.notFound().build();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace(); return ResponseEntity.notFound().build();
        }

    }
}
