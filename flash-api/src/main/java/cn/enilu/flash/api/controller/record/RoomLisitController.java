package cn.enilu.flash.api.controller.record;


import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.entity.cms.Article;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.utils.DateUtil;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.kmss.bean.entity.AnnouncementBean;
import cn.enilu.kmss.bean.entity.RoomList;
import cn.enilu.kmss.service.AnnouncementService;
import cn.enilu.kmss.service.RoomListService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/room")
public class RoomLisitController {

    @Autowired
    private RoomListService roomListService;


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private AnnouncementService announcementService;
    /**
     * 房屋查询
     */
    @PostMapping("/list")
    public Object list(@RequestParam(required = false) String values) {
        Page<RoomList> page = new PageFactory<RoomList>().defaultPage();
        page.addFilter("fdRoomName", SearchFilter.Operator.LIKE, values, SearchFilter.Join.or);
        page.addFilter( "fdName", SearchFilter.Operator.LIKE, values , SearchFilter.Join.or);
        page = roomListService.queryPage(page);
       //roomListService.queryIndexNews()
        List<RoomList> list = page.getRecords();
        return Rets.success(list);
    }
    /**
     * 查询房屋详情
     */
    @GetMapping("/view")
    @BussinessLog(value = "删除档案", key = "id")
    public Object showView(Long id) {

        RoomList room=roomListService.get(id);
        return Rets.success(room);
    }

    /**
     * 删除房屋
     */
    @GetMapping("/delect")
    @BussinessLog(value = "删除档案", key = "id")
    public Object remove(Long id) {
        roomListService.delete(id);
        return Rets.success();
    }
    /**
     * 新增房屋数据
     * @param recordDao
     * @return
     */
    @PostMapping("/add")
    @BussinessLog(value = "房屋新增", key = "name")
    public Object Add(@RequestBody @Valid RoomList recordDao) {
        if (recordDao.getId() == null) {

            //新增房源
           RoomList id= roomListService.insert(recordDao);
            //发布房源信息
            AnnouncementBean announcementBean=new AnnouncementBean();
            Calendar calendar = Calendar.getInstance();
            java.util.Date now = new java.util.Date();
            // 将 java.util.Date 转换为 Timestamp
            Timestamp timestamp = new Timestamp(now.getTime());
            // recordDao.setCreateTime(timestamp);
            long endTime=timestamp.getTime()+60*1000*60*24;
            announcementBean.setFdEndDate(new Timestamp(endTime));//当前时间加三天
            announcementBean.setFdName(id.getFdRoomName());
            announcementBean.setFdRoomId(id.getId().toString());
            announcementBean.setFdMassage("房源名称："+id.getFdRoomName()+"\n房源地址："+id.getFdAddres()+"\n房源备注:"+id.getFdbz());

            announcementService.insert(announcementBean);
        } else {
            roomListService.update(recordDao);
        }
        return Rets.success();
    }
    @PostMapping("/card/getCardSelectList")
    public Object getCardSelectList(
            @RequestParam(value = "inputs.fdAssetStatus",required = false) String  fdAssetStatus,
            @RequestParam(value = "inputs.fdCode",required = false) String  fdCode,
            @RequestParam(value = "inputs.fdName",required = false) String  fdName,
            @RequestParam(value = "inputs.fdTime",required = false) String  fdTime
    ) throws Exception {


        System.out.println("测试");

    return null;
    }


    @PostMapping("/UtilGetModelMk")
    public Object UtilGetModelMk(String id,String cook) {

        JSONObject boby=new JSONObject();
            String url = "http://testmk.naura.local/data/km-review/kmReviewTemplate/get";
          //  String cookie =
                 //       "isMkLogin=true; X-AUTH-TOKEN=1ittmou4cw5w4c8p0w1telu1j3496lrp3ow0"; // 替换为实际的cookie值

            try {
                // 1. 创建HTTP客户端
                HttpClient httpClient = HttpClients.createDefault();

                // 2. 创建POST请求
                HttpPost httpPost = new HttpPost(url);

                // 3. 设置请求头
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setHeader("Cookie", cook);

                // 4. 设置请求体
                JSONObject requestBody = new JSONObject();
                requestBody.put("fdId", id);

                JSONObject mechanisms = new JSONObject();
                mechanisms.put("load", "*");
                requestBody.put("mechanisms", mechanisms);

                StringEntity entity = new StringEntity(requestBody.toString(), StandardCharsets.UTF_8);
                httpPost.setEntity(entity);

                // 5. 执行请求并获取响应
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity responseEntity = response.getEntity();

                if (responseEntity != null) {
                    String responseString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                   // System.out.println("响应内容: " + responseString);



                    JSONObject  responseStrings=handleDuplicateKeys(responseString);

                    JSONObject  data=responseStrings.getJSONObject("data");

                    String dateName= (data.getJSONObject("dynamicProps")).getString("fdNameCn");



                    JSONObject datamechanisms= data.getJSONObject("mechanisms");

                    JSONArray  lbpmTemplate=datamechanisms.getJSONArray("lbpmTemplate");
                    JSONObject lbpmTemplates= (JSONObject) lbpmTemplate.get(0);

                    JSONObject   fdFormFields=lbpmTemplates.getJSONObject("fdFormFields");


                    JSONObject modelDate= fdFormFields.getJSONObject(id);
                    JSONObject properties=modelDate.getJSONObject("properties");
                    //字段id
                    Set<String> set= properties.keySet();

                    JSONObject main=new JSONObject();
                    JSONObject  mainData=new JSONObject();
                    JSONObject  detailData=new JSONObject();
                    JSONArray mainArr=new JSONArray();

                    JSONArray attachment=new JSONArray();
                    for (String key : set) {
                        //生成数据
                        if(key.indexOf("_")>-1){
                            //明细封装
                            if(key.indexOf("mk_")>-1){
                                 JSONArray mxArr=new JSONArray();
                                JSONObject items = properties.getJSONObject(key).getJSONObject("items").getJSONObject("properties");
                                Set<String> itemskey= items.keySet();
                                JSONObject  mxlist=new JSONObject();
                                for (String keyls : itemskey) {
                                    JSONObject  massage=new JSONObject();

                                    JSONObject keylsjson = items.getJSONObject(keyls);
                                    massage.put("fdKey",key+"."+keyls);
                                    massage.put("fdName",keylsjson.getString("description"));
                                    massage.put("fdType",keylsjson.getString("type"));
                                    if( keylsjson.has("enum")){
                                        JSONArray arr=keylsjson.getJSONArray("enum");
                                        String emu="";
                                        for(Object obj:arr){
                                            String  emuDate=(String)obj;
                                            emu=emu+emuDate+"\n";
                                        }
                                        massage.put("fdEnum",emu);
                                    }else{
                                        massage.put("fdEnum","");
                                    }
                                    mainArr.put(massage);

                                    if(keylsjson.getString("type").equals("string")){
                                        mxlist.put(keyls,keylsjson.getString("description"));
                                    }else  if(keylsjson.getString("type").equals("number")){
                                        mxlist.put(keyls,1);
                                    }else  if(keylsjson.getString("type").equals("object")){



                                        if(keylsjson.has("$ref")){
                                            JSONObject keyDate=new JSONObject();
                                            keyDate.put("fdLoginName","mkadmin(单选为对象多选为数组)");
                                            main.put(key,keyDate);
                                        }
                                    }else  if(keylsjson.getString("type").equals("integer")){
                                        mxlist.put(keyls,new Date().getTime());
                                    }else if(keylsjson.getString("type").equals("array")){
                                        mxlist.put(keyls,"生成的唯一的key");

                                        JSONObject keyDate=new JSONObject();
                                        keyDate.put("fdEntityKey",key);
                                        keyDate.put("fdDownloadUrl","文件外部地址（如果传递fgId则不用传这个）");
                                        keyDate.put("fdFileName","文件名称");
                                        keyDate.put("fdFileSize",23);
                                        keyDate.put("fdBindType","ADD");
                                        keyDate.put("fdExternalPreviewUrl","文件预览地址（如果传递fgId则不用传这个）");
                                        keyDate.put("fdId","文件上传返回的参数");
                                        attachment.put(keyDate);

                                    }
                                  //  mxlist.put(keyls,keylsjson.getString("description"));
                                }
                                mxArr.put(mxlist);
                                detailData.put(key,mxArr);
                            }else{
                                //主表字段封装
                                JSONObject json = properties

                                        .getJSONObject(key);
                                JSONObject  massage=new JSONObject();
                                //System.out.println(json.getString("description"));
                                massage.put("fdKey",key);
                                massage.put("fdName",json.getString("description"));
                                massage.put("fdType",json.getString("type"));
                                if( json.has("enum")){

                                    JSONArray arr=json.getJSONArray("enum");
                                    String emu="";
                                    for(Object obj:arr){
                                        String  emuDate=(String)obj;
                                        emu=emu+emuDate+"\n";
                                    }
                                    massage.put("fdEnum",emu);
                                }else{
                                    massage.put("fdEnum","");
                                }
                                mainArr.put(massage);
                                if(json.getString("type").equals("string")){//字符串
                                    main.put(key,json.getString("description"));
                                }else  if(json.getString("type").equals("number")){//数字
                                    main.put(key,1);
                                }else  if(json.getString("type").equals("object")){

                                    if(json.has("$ref")){//地址本
                                        JSONObject keyDate=new JSONObject();
                                        keyDate.put("fdLoginName","mkadmin(单选为对象多选为数组)");
                                        main.put(key,keyDate);
                                    }
                                }else  if(json.getString("type").equals("integer")){//时间
                                    main.put(key,new Date().getTime());
                                }else if(json.getString("type").equals("array")){//附件
                                    JSONObject keyDate=new JSONObject();
                                    keyDate.put("fdEntityKey",key);
                                    keyDate.put("fdDownloadUrl","文件外部地址（如果传递fgId则不用传这个）");
                                    keyDate.put("fdFileName","文件名称");
                                    keyDate.put("fdFileSize",23);
                                    keyDate.put("fdBindType","ADD");
                                    keyDate.put("fdExternalPreviewUrl","文件预览地址（如果传递fgId则不用传这个）");
                                    keyDate.put("fdId","文件上传返回的参数");
                                    attachment.put(keyDate);
                                }
                            }
                        }
                    }

                    //明细数据
                    boby.put("list",detailData);
                    //主表数据

                    boby.put("main",main);
                    mainData.put("fdMianDate",mainArr);

                    // 7. 生成Excel文件
                   createExcelFromJson(mainData, dateName+".xlsx");
                    System.out.println("Excel文件已生成!");
                    redisTemplate.opsForValue().set("test", "Hello Redis");
                    redisTemplate.opsForValue().set("TEST:EDIS:DATA:name", "测试书");
                    redisTemplate.opsForValue().set("name", "测试书");
                    return chanegDate(main,detailData,attachment,id).toString();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

      //  redisTemplate.opsForValue().set("test", "Hello Redis");
        System.out.println("Redis Test: " + redisTemplate.opsForValue().get("test"));
        return boby.toString();
    }

    private JSONObject handleDuplicateKeys(String jsonString) {
        // 使用Jackson或其他更宽松的解析器
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.STRICT_DUPLICATE_DETECTION, false);
        try {
            JsonNode node = mapper.readTree(jsonString);
            return new JSONObject(node.toString());
        } catch (Exception e) {
            throw new JSONException("Failed to parse JSON with duplicate keys", e);
        }
    }


    //生成最终该报文
    public Object chanegDate(JSONObject  main,JSONObject list,JSONArray  att,String id){

        // 创建根JSON对象
        JSONObject root = new JSONObject();

        // 添加curUser对象
        JSONObject curUser = new JSONObject();
        curUser.put("fdLoginName", "lijifei");
        root.put("curUser", curUser);

        // 添加fdTemplateId
        root.put("fdTemplateId", id);

        // 创建mechanisms对象
        JSONObject mechanisms = new JSONObject();
        mechanisms.put("attachment", att); // 显式设置null

        // 创建sys-xform对象
        JSONObject sysXform = new JSONObject();

        // 创建mainData对象
        sysXform.put("mainData", main);

        // 创建空的detailData对象
        sysXform.put("detailData",list);

        mechanisms.put("sys-xform", sysXform);
        root.put("mechanisms", mechanisms);
        return root;
    }


    /**
     * 接送  参数设置
     * @param jSONObject    //主表数据
     * @param fileName    接口文件名称
     * @param jsonArray   //明细表数据
     * @throws Exception
     */
    private void     createExcelFromJson(JSONObject jSONObject, String fileName, JSONArray jsonArray) throws Exception {
        try {
            // 1. 解析JSON数据
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> dataMap = mapper.readValue(jSONObject.toString(), Map.class);
            List<Map<String, String>> fieldList = (List<Map<String, String>>) dataMap.get("fdMianDate");

            // 2. 创建Excel工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("1. 接口概括");

            // 3. 设置表头样式  加粗
            //灰色
            CellStyle GREY_25_PERCENT=this.setCellStyle(workbook,IndexedColors.GREY_25_PERCENT.getIndex());
            //红色
            CellStyle RED= this.setCellStyle(workbook,IndexedColors.RED.getIndex());
            //黄色
            CellStyle YELLOW= this.setCellStyle(workbook,IndexedColors.YELLOW.getIndex());
            //浅蓝色
            CellStyle LIGHT_TURQUOISE=this.setCellStyle(workbook,IndexedColors.LIGHT_TURQUOISE.getIndex());


            //第一行数据
            Row headerRow = sheet.createRow(0);
            // 合并单元格：第0行，从第0列到第4列（共5列）
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 12));
            Cell headerRowCell1 = headerRow.createCell(0);//第一行第一列数据
            headerRowCell1.setCellValue("接口概括");
            // headerStyle 已经包含了加粗和背景颜色设置，直接应用即可
            headerRowCell1.setCellStyle(GREY_25_PERCENT);


            //数据流向		数据流向						ESB接口地址						下游接口地址
            // 4. 创建表头
            Row headerRow2 = sheet.createRow(1);
            String[] headers = {"数据流向", "数据流向","ESB接口地址","下游接口地址"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow2.createCell(i);
                // 合并单元格
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 0, 2));
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 3, 6));
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 7, 9));
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 10, 12));
                if(i<3){
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(YELLOW);
                }else {
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(RED);
                }
            }

//            		1-价格审批_维修清洗追加工MK创建】						DEV:http://ipaas-uat.naura.com:8080/ipaas/MK/API_MK_flowCreate
//            PROD: http://ipaas.naura.com:8080/ipaas/MK/API_MK_flowCreate
//            Authorization:联系管理员获取接口授权						DEV:
//            TEST:
//            PROD:


            // 4. 创建表头
            Row headerRow3 = sheet.createRow(2);
            String[] col3 = {"第三方→MK", "","ESB接口地址","下游接口地址"};
            for (int i = 0; i < col3.length; i++) {
                Cell cell = headerRow3.createCell(i);
                // 合并单元格
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 0, 2));
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 3, 6));
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 7, 9));
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 10, 12));
                if(i<3){
                    cell.setCellValue(col3[i]);
                    cell.setCellStyle(YELLOW);
                }else {
                    cell.setCellValue(col3[i]);
                    cell.setCellStyle(RED);
                }
            }



            // 5. 填充数据
            int rowNum = 1;
            for (Map<String, String> field : fieldList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(field.get("fdKey"));
                row.createCell(1).setCellValue(field.get("fdName"));
                row.createCell(2).setCellValue(field.get("fdType"));
                row.createCell(3).setCellValue(field.get("fdEnum"));
            }

            // 6. 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 7. 写入文件
            try (FileOutputStream outputStream = new FileOutputStream("D:\\项目文件\\接口文档字段\\"+fileName+".xlsx")) {
                workbook.write(outputStream);
            }

            System.out.println("Excel文件生成成功！");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("生成Excel文件时出错: " + e.getMessage());
        }
    }



    /**
     * @param  workbook   Excel工作簿
     *设置单元格样式
     */
    private  CellStyle setCellStyle(Workbook workbook,short index) {

        CellStyle headerStyle = workbook.createCellStyle();
        // 创建一个单元格样式对象，用于定义单元格的格式
        Font headerFont = workbook.createFont();
        // 创建一个字体对象，用于设置文本的字体属性
        headerFont.setBold(true);
        // 将字体设置为粗体，使表头文字更醒目
        headerStyle.setFont(headerFont);
        // 将粗体字体应用到单元格样式中
        headerStyle.setFillForegroundColor(index );//IndexedColors.GREY_25_PERCENT.getIndex()
        // 设置单元格背景填充色为浅灰色（25%灰度）
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置填充模式为纯色填充，使背景色生效
        headerStyle.setBorderBottom(BorderStyle.THIN);
        // 为单元格底部添加细边框，增加视觉分隔
        //设置居中
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        return headerStyle;


    }
















    private static void     createExcelFromJson(JSONObject jSONObject, String fileName) throws Exception {
        try {
            // 1. 解析JSON数据
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> dataMap = mapper.readValue(jSONObject.toString(), Map.class);
            List<Map<String, String>> fieldList = (List<Map<String, String>>) dataMap.get("fdMianDate");

            // 2. 创建Excel工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("1. 接口概括");

            // 3. 设置表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);

            // 4. 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {"字段Key", "字段名称","字段类型","备注"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 5. 填充数据
            int rowNum = 1;
            for (Map<String, String> field : fieldList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(field.get("fdKey"));
                row.createCell(1).setCellValue(field.get("fdName"));
                row.createCell(2).setCellValue(field.get("fdType"));
                row.createCell(3).setCellValue(field.get("fdEnum"));
            }

            // 6. 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 7. 写入文件
            try (FileOutputStream outputStream = new FileOutputStream("D:\\项目文件\\接口文档字段\\"+fileName+".xlsx")) {
                workbook.write(outputStream);
            }

            System.out.println("Excel文件生成成功！");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("生成Excel文件时出错: " + e.getMessage());
        }
    }

}
