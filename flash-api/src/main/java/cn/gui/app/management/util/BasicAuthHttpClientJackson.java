import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BasicAuthHttpClientJackson {

    public static void main(String[] args) throws Exception {
        String apiUrl = "http://123.249.96.139/openapi/sys-modeling/apis/data/ls-gsgl/V1/gs/add";
        String username = "gxl";
        String password = "gxl@0519";

        // 使用Jackson构建请求体
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode requestBody = buildRequestBody(mapper);

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

    private static ObjectNode buildRequestBody(ObjectMapper mapper) {
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

        dynamicProps.put("fd_col_dx9xpg", 0);
        dynamicProps.put("fd_col_8uutph", 0);
        dynamicProps.put("fd_col_nzc8js", "");

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
        dynamicProps.put("fd_xform_id", "");

        // mk_model_gsxx_detail数组
        ObjectNode detailItem = mapper.createObjectNode();
        detailItem.put("fd_col_3ldpdl", "");
        detailItem.put("fd_col_js3w44", 0);
        detailItem.put("fd_col_yxh6rm", "");

        ObjectNode xmmcItem = mapper.createObjectNode();
        xmmcItem.put("fdId", "1iqkknrk8w2wgs8rhw1ssvjv0128t8hc30w0");
        xmmcItem.put("fdName", "流程效率");
        detailItem.putArray("fd_xmmc").add(xmmcItem);

        detailItem.put("fd_order", 0);
        dynamicProps.putArray("mk_model_gsxx_detail").add(detailItem);

        formData.set("dynamicProps", dynamicProps);
        requestBody.set("formData", formData);

        // saveOption
        ObjectNode saveOption = mapper.createObjectNode();
        saveOption.put("saveDraft", true);
        requestBody.set("saveOption", saveOption);

        return requestBody;
    }
}