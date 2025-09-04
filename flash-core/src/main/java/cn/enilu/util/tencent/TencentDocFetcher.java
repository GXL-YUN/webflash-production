package cn.enilu.util.tencent;

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
        return "RK=n6fQQhvmY4; ptcz=4ff38aaa73cf35b9db4943cb354ebbec03cda4487ac6aa991f9719073be768a5; low_login_enable=1; _clck=1wp0p04|1|fwn|0; yyb_muid=28D458B3459C63EC3AEB4C3D447662FA; traceid=c1eb6c1990; TOK=c1eb6c19900ed328; hashkey=c1eb6c19; backup_cdn_domain=docs.gtimg.com; fingerprint=9e68f8c480134da3b35fa4f18092d75040; uid=144115231536952723; uid_key=EOP1mMQHGixzbkRxTnEvMGZQdUVCMFdUc0dHZFJ0Q1ZqRFpOcFF2S21wVnptVktCRDlRPSKBAmV5SmhiR2NpT2lKQlEwTkJURWNpTENKMGVYQWlPaUpLVjFRaWZRLmV5SlVhVzU1U1VRaU9pSXhORFF4TVRVeU16RTFNelk1TlRJM01qTWlMQ0pXWlhJaU9pSXhJaXdpUkc5dFlXbHVJam9pYzJGaGMxOTBiMk1pTENKU1ppSTZJbWRzWkhWMVNpSXNJbVY0Y0NJNk1UYzFORFl4TmpNd01Dd2lhV0YwSWpveE56VXlNREkwTXpBd0xDSnBjM01pT2lKVVpXNWpaVzUwSUVSdlkzTWlmUS5McXVMMmxVTUlmN2o1elNmZmJwNTNBcmlPT0p5WW5MemdJQlI0cXdySW9BKOyj1cQG; utype=wx; wx_appid=wx02b8ff0031cec148; openid=oy6SixCYIiW9CRAcCF74Mt90ERxA; access_token=94_b36pOlRHHZ_ndXg4xDzsYeoc16xf1U0mlR4tfyIHYY5uokH9bVWx7NxpUe7ZyMzYuB4YESrcXcCKqHXHi3tb6avp6VSd0_yrp6vrCXyp5kU; refresh_token=94_-AkCHLlIgU4kOjkNFOcsVMVG06VSMzOR4N0y91wbTGnotIxFirsh81GdnAszv8X5b6IiYMntduJbME_baJVFbxSjNyNLf03s5UFwsj0qXmc; env_id=gray-pct25; gray_user=true; DOC_SID=3ffe67b7f5f74c79b08aec1ae5e9dcff8d9fe1cfc8ae41158e3c4a529a47bbfa; SID=3ffe67b7f5f74c79b08aec1ae5e9dcff8d9fe1cfc8ae41158e3c4a529a47bbfa; optimal_cdn_domain=docs2.gtimg.com; dark_mode_setting=system; loginTime=1752135130686";
    }
}