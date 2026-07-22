package demo;

import com.aliyun.auth.credentials.provider.DefaultCredentialProvider;
import com.aliyun.sdk.service.dypnsapi20170525.models.*;
import com.aliyun.sdk.service.dypnsapi20170525.*;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;

//import javax.net.ssl.KeyManager;
//import javax.net.ssl.X509TrustManager;
import java.util.concurrent.CompletableFuture;

public class NoteSendUtil {
    public static void main(String[] args) throws Exception {
//
//        enable = true
//        access_key_id = 你的AccessKeyId
//        access_key_secret = 你的AccessKeySecret

        //配置凭据认证信息
        DefaultCredentialProvider provider = DefaultCredentialProvider.builder().build();

        // Configure the Client 配置客户端
        try (AsyncClient client = AsyncClient.builder()
                .region("cn-shanghai") // Region ID
                .credentialsProvider(provider)
                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.客户端级配置重写，可设置端点、HTTP请求参数等。
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                // Endpoint 请参考 https://api.aliyun.com/product/Dypnsapi
                                .setEndpointOverride("dypnsapi.aliyuncs.com")
                )
                .build()) {

            // Parameter settings for API request
            SendSmsVerifyCodeRequest sendSmsVerifyCodeRequest = SendSmsVerifyCodeRequest.builder()
                    .signName("速通互联验证码")
                    .templateCode("100001")
                    .templateParam("{\"code\":\"1234\",\"min\":\"5\"}")
                    .phoneNumber("13720661531")
                    // Request-level configuration rewrite, can set Http request parameters, etc.
                    // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                    .build();

            CompletableFuture<SendSmsVerifyCodeResponse> response = client.sendSmsVerifyCode(sendSmsVerifyCodeRequest);
            // Synchronously get the return value of the API request
            SendSmsVerifyCodeResponse resp = response.get();
            System.out.println(new Gson().toJson(resp));
            // Asynchronous processing of return values
            /*response.thenAccept(resp -> {
                System.out.println(new Gson().toJson(resp));
            }).exceptionally(throwable -> { // Handling exceptions
                System.out.println(throwable.getMessage());
                return null;
            });*/

        }
    }

}