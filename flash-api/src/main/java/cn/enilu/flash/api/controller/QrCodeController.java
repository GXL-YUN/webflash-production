package cn.enilu.flash.api.controller;


import cn.enilu.util.CouponCode.QrCodeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

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
        BufferedImage image =
                QrCodeUtil.generateQrCode(code, 300, 300);

        ImageIO.write(image, "PNG", response.getOutputStream());
    }

    /**
     * 生成核销码
     */


    /**
     * 进行核销
     */


}