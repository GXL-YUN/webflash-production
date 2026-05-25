package cn.enilu.flash.api.controller.pet;

import cn.enilu.pet.server.OrdersServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 支付成功接口回调接口
 */

@Slf4j
@RestController
@RequestMapping("/api/pet/pay")
public class PayCallbackContriller {


    @Autowired
   public OrdersServiceImpl ordersServiceImpl;


    @PostMapping("/callback/alipay")
    public String alipayCallback(HttpServletRequest request) {
        String orderNo = request.getParameter("out_trade_no");
        String tradeStatus = request.getParameter("trade_status");
        ordersServiceImpl.handleCallback(orderNo, tradeStatus);
        return "success";
    }

}
