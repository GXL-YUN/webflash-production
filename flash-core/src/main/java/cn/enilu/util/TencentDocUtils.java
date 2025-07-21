package cn.enilu.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class TencentDocUtils {
    private static final String API_URL = "https://docs.qq.com/openapi/..."; // 需要替换为实际API地址

    public static String getSheetData(String docId, String sheetId, String token) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(API_URL + "?docId=" + docId + "&sheetId=" + sheetId);
        httpGet.setHeader("Authorization", "Bearer " + token);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            return EntityUtils.toString(response.getEntity());
        }
    }
}