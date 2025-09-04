package cn.gui.app.management.util;

import cn.enilu.flash.api.utils.RandomDateUtil;
import cn.enilu.util.DateUtil;
import cn.gui.app.management.bean.ProjectBean;
import cn.gui.app.management.bean.Task;
import cn.gui.app.management.dao.ProjctDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONArray;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BasicAuthHttpClientJackson {

    public static void create(  List<Task> tasks) throws Exception {
        String apiUrl = "http://123.249.96.139/openapi/sys-modeling/apis/data/ls-gsgl/V1/gs/add";
        String username = "gxl";
        String password = "gxl@0519";

        // 使用Jackson构建请求体
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode requestBody = buildRequestBody(mapper,tasks);

        // 创建Basic认证头
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        // 创建HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // 创建HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + encodedAuth)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        // 发送请求并获取响应
        HttpResponse<String> response = client.send(
                request,
               HttpResponse.BodyHandlers.ofString());

        System.out.println("响应码: " + response.statusCode());
        System.out.println("响应内容: " + response.body());
    }

    private static ObjectNode buildRequestBody(ObjectMapper mapper,List<Task> tasks) {
        ObjectNode requestBody = mapper.createObjectNode();

        // bpmOperation
        ObjectNode bpmOperation = mapper.createObjectNode();
        bpmOperation.put("templateCode", "template_j7gbuh");
        requestBody.set("bpmOperation", bpmOperation);

        requestBody.put("checkAuth", false);

        // curUser
        ObjectNode curUser = mapper.createObjectNode();
        curUser.put("fdLoginName", "guxinlei");
        requestBody.set("curUser", curUser);

        // formData
        ObjectNode formData = mapper.createObjectNode();
        ObjectNode dynamicProps = mapper.createObjectNode();

        // dynamicProps中的各个字段
        ObjectNode fdColGhxumm = mapper.createObjectNode();
        fdColGhxumm.put("fdLoginName", "guxinlei");
        dynamicProps.set("fd_col_ghxumm", fdColGhxumm);

        ObjectNode fdColQvs4rv = mapper.createObjectNode();
        fdColQvs4rv.put("fdId", "1icn6g2kdw1w324kb5w3hn5lfosqr0c433w0");
        fdColQvs4rv.put("fdName", "开发部");
        dynamicProps.set("fd_col_qvs4rv", fdColQvs4rv);

        dynamicProps.put("fd_col_8uutph", 1.0);
        //dynamicProps.put("fd_col_dx9xpg", DateUtil.getDate(0).getTime()-1000*60*60*24);
        dynamicProps.put("fd_col_dx9xpg", DateUtil.getDate(0).getTime()-(1000*60*60*24)*2);
        dynamicProps.put("fd_col_nzc8js", "风险项");

        ObjectNode fdCreator = mapper.createObjectNode();
        fdCreator.put("fdLoginName", "guxinlei");
        dynamicProps.set("fd_creator", fdCreator);

        dynamicProps.put("fd_create_time", 0);

        ObjectNode fdCreatorDept = mapper.createObjectNode();
        fdCreatorDept.put("fdId", "1icn6g2kdw1w324kb5w3hn5lfosqr0c433w0");
        fdCreatorDept.put("fdName", "开发部");
        dynamicProps.set("fd_creator_dept", fdCreatorDept);

        dynamicProps.put("fd_doc_status", "10");
        dynamicProps.put("fd_doc_subject", "谷鑫磊");
       // dynamicProps.put("fd_xform_id", "");
        Map<String,String> map=new HashMap<>();
        map.put("NOT_STARTED","未开始");
        map.put("COMPLETED","已完成");
        map.put("IN_PROGRESS","进行中");
        map.put("STOPETED","任务暂停");

        ArrayNode arrayNode = dynamicProps.putArray("mk_model_gsxx_detail");


        List<Integer> list= RandomDateUtil.splitEqually(10, tasks.size());

        int i=0;
        for (Task task:tasks){
            // mk_model_gsxx_detail数组
            ObjectNode detailItem = mapper.createObjectNode();
            detailItem.put("fd_col_3ldpdl", "任务名称："+task.getTaskName()+" 任务描述："+task.getDescription().replaceAll("\\s*|\t|\r|\n", "")+" 是否完成："+map.get(task.getStatus())+"\n");
            detailItem.put("fd_col_js3w44", "0."+list.get(i));
            detailItem.put("fd_col_yxh6rm", "1");
            ObjectNode xmmcItem = mapper.createObjectNode();
            xmmcItem.put("fdId", task.getFdTYpe());
            ProjctDao p=new ProjctDao();
            ProjectBean date= p.getTaskName(task.getFdTYpe()).get(0);
            xmmcItem.put("fdName", date.getFdName());
            detailItem.putArray("fd_xmmc").add(xmmcItem);
            detailItem.put("fd_order", 0);
            arrayNode.add(detailItem);
            i++;
        }
        formData.set("dynamicProps", dynamicProps);
        requestBody.set("formData", formData);

        // saveOption
        ObjectNode saveOption = mapper.createObjectNode();
        saveOption.put("saveDraft", true);
        //是否为草稿
        requestBody.set("saveOption", saveOption);
        System.out.println("响应报文"+requestBody);
        return requestBody;
    }



}