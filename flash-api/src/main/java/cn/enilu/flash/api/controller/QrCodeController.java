package cn.enilu.flash.api.controller;


import cn.enilu.util.CouponCode.QrCodeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/qrcode")
public class QrCodeController extends BaseController {


    /**
     * 生成二维码
     * @param response
     * @param code
     * @throws Exception
     */

    @GetMapping("/verify/{code}")
    public void generate(HttpServletResponse response,
                         @PathVariable String code) throws Exception {
        response.setContentType("image/png");
        //文本二维码
//        BufferedImage image =
//                QrCodeUtil.generateQrCode(code, 300, 300);

        //生成跳转二维码
//        BufferedImage image = QrCodeUtil.generateWebUrlQrCode(
//                        "https://www.example.com", 300, 300);
        //生辰发送短信二维码
//        BufferedImage image = QrCodeUtil.generateTelQrCode(
//                "13720661531", 300, 300);

        //生成发送短信
//        BufferedImage image = QrCodeUtil.generateSmsQrCode("13720661531", "测试",300, 300);

        //生成发送邮件二维码
//        BufferedImage image = QrCodeUtil.generateTelQrCode(
//                "13720661531@163.com", 300, 300);



        // 生成小红书笔记跳转二维码
        BufferedImage image = QrCodeUtil.generateXhsQrCode(
                "note",
                "6483db67000000001303ab3f",
                300,
                300
        );
        //跳转小红书app
       // BufferedImage image = QrCodeUtil.generateAppSchemeQrCode("myapp://open/detail?id=123", 300, 300);
        //BufferedImage image = QrCodeUtil.generateAppLinkQrCode()


        //带参数跳转
//        Map<String, String> params = new HashMap<>();
//        params.put("couponId", "10001");
//        params.put("userId", "888");
//
//            BufferedImage image = QrCodeUtil.generateWithParamsQrCode(
//                        "https://www.example.com/coupon/detail",
//              params,
//              300,
//              300
//                 );
        ImageIO.write(image, "PNG", response.getOutputStream());
    }

    /**
     * 生成核销码
     */


    /**
     * 进行核销
     */


}