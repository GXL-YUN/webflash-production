package cn.enilu.util.CouponCode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码工具类（增强版）
 */
public class QrCodeUtil {

    // ========================= 原有方法（不变） =========================

    /**
     * 生成文字二维码
     */
    public static BufferedImage generateQrCode(
            String content,
            int width,
            int height) throws Exception {

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        BitMatrix bitMatrix = new MultiFormatWriter()
                .encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * 生成核销二维码（输出文件）
     */
    public static void generateToFile(
            String content,
            int width,
            int height,
            Path path) throws Exception {

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);

        BitMatrix matrix = new MultiFormatWriter()
                .encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        MatrixToImageWriter.writeToPath(matrix, "PNG", path);
    }

    // ========================= 新增方法 =========================

    // ==================== 1️⃣ 常见可跳转内容格式 ====================

    /**
     * 生成网页跳转二维码（最常用）
     */
    public static BufferedImage generateWebUrlQrCode(
            String url,
            int width,
            int height) throws Exception {

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new IllegalArgumentException("URL 必须以 http:// 或 https:// 开头");
        }
        return generateQrCode(url, width, height);
    }

    /**
     * 生成拨打电话二维码
     */
    public static BufferedImage generateTelQrCode(
            String phoneNumber,
            int width,
            int height) throws Exception {

        return generateQrCode("tel:" + phoneNumber, width, height);
    }

    /**
     * 生成发送短信二维码
     */
    public static BufferedImage generateSmsQrCode(
            String phoneNumber,
            String content,
            int width,
            int height) throws Exception {

        return generateQrCode("smsto:" + phoneNumber + ":" + content, width, height);
    }

    /**
     * 生成发送邮件二维码
     */
    public static BufferedImage generateEmailQrCode(
            String email,
            int width,
            int height) throws Exception {

        return generateQrCode("mailto:" + email, width, height);
    }

    // ==================== 2️⃣ 扫码跳转 App ====================

    /**
     * 扫码跳转 App（Scheme）
     *
     * 示例：
     * myapp://open/detail?id=123
     */
    public static BufferedImage generateAppSchemeQrCode(
            String schemeUrl,
            int width,
            int height) throws Exception {

        if (!schemeUrl.contains("://")) {
            throw new IllegalArgumentException("Scheme 格式不正确，例如 myapp://xxx");
        }
        return generateQrCode(schemeUrl, width, height);
    }

    /**
     * 扫码跳转 App（Universal Link / App Link）
     *
     * 示例：
     * https://www.example.com/app/open?id=123
     */
    public static BufferedImage generateAppLinkQrCode(
            String universalLink,
            int width,
            int height) throws Exception {

        if (!universalLink.startsWith("https://")) {
            throw new IllegalArgumentException("Universal Link 必须以 https:// 开头");
        }
        return generateQrCode(universalLink, width, height);
    }

    // ==================== 3️⃣ 后端带参数跳转二维码 ====================

    /**
     * 生成带参数的业务跳转二维码（推荐）
     *
     * 示例：
     * https://www.example.com/coupon/detail?couponId=10001
     */
    public static BufferedImage generateWithParamsQrCode(
            String baseUrl,
            Map<String, String> params,
            int width,
            int height) throws Exception {

        StringBuilder url = new StringBuilder(baseUrl);
        if (params != null && !params.isEmpty()) {
            url.append("?");
            params.forEach((k, v) -> url.append(k).append("=").append(v).append("&"));
            url.deleteCharAt(url.length() - 1);
        }
        return generateWebUrlQrCode(url.toString(), width, height);
    }

    /**
     * 文件版本：带参数跳转二维码
     */
    public static void generateWithParamsToFile(
            String baseUrl,
            Map<String, String> params,
            int width,
            int height,
            Path path) throws Exception {

        BufferedImage image = generateWithParamsQrCode(baseUrl, params, width, height);
        MatrixToImageWriter.writeToPath(
                new MultiFormatWriter().encode(
                        baseUrl + buildParams(params),
                        BarcodeFormat.QR_CODE,
                        width,
                        height,
                        new HashMap<>() {{
                            put(EncodeHintType.CHARACTER_SET, "UTF-8");
                            put(EncodeHintType.MARGIN, 1);
                        }}
                ),
                "PNG",
                path
        );
    }

    private static String buildParams(Map<String, String> params) {
        StringBuilder sb = new StringBuilder("?");
        params.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 生成跳转小红书App的二维码（带参数，使用Universal Link兜底）
     * @param type   跳转类型：note(笔记) / user(用户主页)
     * @param id     笔记ID 或 用户ID
     * @param width  宽
     * @param height 高
     */
    public static BufferedImage generateXhsQrCode(
            String type,
            String id,
            int width,
            int height) throws Exception {

        String content;
        if ("note".equalsIgnoreCase(type)) {
            // 笔记：用网页链接，扫码后自动唤起App，没装App则打开网页
            content = "https://www.xiaohongshu.com/discovery/item/" + id;
        } else if ("user".equalsIgnoreCase(type)) {
            // 用户主页
            content = "https://www.xiaohongshu.com/user/profile/" + id;
        } else {
            throw new IllegalArgumentException("type only support 'note' or 'user'");
        }

        // 复用你已有的生成逻辑
        return generateQrCode(content, width, height);
    }



    /**
     * 1️⃣ 网页跳转二维码
     * BufferedImage img = QrCodeUtil.generateWebUrlQrCode(
     *         "https://www.example.com",
     *         300,
     *         300
     * );
     * ✅ 2️⃣ 扫码打开 App
     * // Scheme
     * QrCodeUtil.generateAppSchemeQrCode(
     *         "myapp://open/detail?id=123",
     *         300,
     *         300
     * );
     *
     * // Universal Link
     * QrCodeUtil.generateAppLinkQrCode(
     *         "https://www.example.com/app/open?id=123",
     *         300,
     *         300
     * );
     * ✅ 3️⃣ 带参数业务二维码（最推荐）
     * Map<String, String> params = new HashMap<>();
     * params.put("couponId", "10001");
     * params.put("userId", "888");
     *
     * BufferedImage img = QrCodeUtil.generateWithParamsQrCode(
     *         "https://www.example.com/coupon/detail",
     *         params,
     *         300,
     *         300
     * );
     * 扫码后访问：
     * https://www.example.com/coupon/detail?couponId=10001&userId=888
     */
}