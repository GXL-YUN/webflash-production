package cn.enilu.flash.api.utils.sdk;

import cn.enilu.flash.api.utils.DateUtil;

import java.security.MessageDigest;

public class WxSdkutil {

    public String generateSignature(String nonceStr, long timestamp, String url) {
        String jsapiTicket = "O3SMpm8bG7kJnF36aXbe84iRSyfoAovXc2t7a4b_QHZLWIUk1e9_a7Svr13o4tsD3IpOlUFxzmHJP53b6FFtcQ"; // 从微信获取
        String plainText = "jsapi_ticket=" + jsapiTicket +
                "&noncestr=" + nonceStr +
                "×tamp=" + timestamp +
                "&url=" + url;
        System.out.println(plainText);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(plainText.getBytes());
            return bytesToHex(digest);
        } catch (Exception e) {
            throw new RuntimeException("生成签名失败", e);
        }
    }
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    //测试
    public static void main(String[] args) {
        WxSdkutil wx=new WxSdkutil();
        String sig =wx.generateSignature("qwertyuiopasdfghj", DateUtil.getDateQueue().getTime(),"http://122.51.246.240/xhss.html");
        System.out.println(sig);
    }
}
