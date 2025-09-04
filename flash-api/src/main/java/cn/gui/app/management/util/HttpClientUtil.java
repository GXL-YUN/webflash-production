package cn.gui.app.management.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HttpClientUtil {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    /**
     * 异步POST请求（非阻塞）
     */
    public static CompletableFuture<String> postAsync(String url, String json, Map<String, String> headers) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(15));

        // 添加请求头
        if (headers != null) {
            headers.forEach(builder::header);
        }

        return httpClient.sendAsync(builder.build(), BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    /**
     * 文件上传（multipart/form-data）
     */
    public static String uploadFile(String url, File file, Map<String, String> formData) throws Exception {
        String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();

        // 构建Multipart请求体
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            baos.write(("--" + boundary + "\r\n").getBytes());
            baos.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n").getBytes());
            baos.write((entry.getValue() + "\r\n").getBytes());
        }

        // 添加文件部分
        baos.write(("--" + boundary + "\r\n").getBytes());
        baos.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n").getBytes());
        baos.write(("Content-Type: " + Files.probeContentType(file.toPath()) + "\r\n\r\n").getBytes());
        baos.write(Files.readAllBytes(file.toPath()));
        baos.write(("\r\n--" + boundary + "--\r\n").getBytes());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(BodyPublishers.ofByteArray(baos.toByteArray()))
                .build();

        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        return response.body();
    }
}