import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TencentDocFetcher {

    private static final String DOC_ID = "DSlBmSkZURVJqZ1Rr";
    private static final String SHEET_ID = "BB08J2";
    private static final String API_URL = "https://docs.qq.com/dop-api/opendoc";

    public static void main(String[] args) {
        try {
            // 1. 获取QQ登录Cookie（需通过浏览器登录后获取）
            String cookie = getQQCookie();

            // 2. 获取文档数据
            String jsonData = fetchDocumentData(cookie);

            // 3. 解析并处理数据
            processDocumentData(jsonData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String fetchDocumentData(String cookie) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 构建请求URL
        String url = String.format("%s?id=%s&tab=%s", API_URL, DOC_ID, SHEET_ID);

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Cookie", cookie);
        httpGet.setHeader("Referer", "https://docs.qq.com/sheet/" + DOC_ID);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        }
    }

    private static void processDocumentData(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        // 提取表格数据
        JsonNode sheet = root.path("data").path("sheet");
        JsonNode rows = sheet.path("rows");

        System.out.println("表格标题: " + sheet.path("title").asText());
        System.out.println("行数: " + rows.size());

        // 打印表头
        JsonNode columns = sheet.path("columns");
        System.out.print("| ");
        for (JsonNode column : columns) {
            System.out.print(column.path("title").asText() + " | ");
        }
        System.out.println("\n----------------------------------");

        // 打印数据行
        for (JsonNode row : rows) {
            System.out.print("| ");
            JsonNode cells = row.path("cells");
            for (JsonNode cell : cells) {
                System.out.print(cell.path("text").asText() + " | ");
            }
            System.out.println();
        }
    }

    // 需要实现获取QQ登录Cookie的方法
    private static String getQQCookie() {
        // 实际项目中应该通过登录流程获取或从配置读取
        return "你的QQ登录Cookie";
    }
}