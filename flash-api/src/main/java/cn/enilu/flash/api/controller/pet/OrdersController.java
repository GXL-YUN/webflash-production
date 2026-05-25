package cn.enilu.flash.api.controller.pet;

import cn.enilu.flash.api.controller.CrudController;
import cn.enilu.flash.bean.page.RequertInfo;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.service.BaseService;
import cn.enilu.pet.bean.model.Orders;
import cn.enilu.pet.dao.DemandDao;
import cn.enilu.pet.dao.OrdersDao;
import cn.enilu.pet.server.OrdersServiceImpl;
import cn.enilu.sms.service.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 订单生成逻辑
 */
@Slf4j
@RestController
@RequestMapping("/api/pet/order")
public class OrdersController extends
        CrudController<Orders, String, OrdersDao, RequertInfo> {


    @Autowired
    private OrdersServiceImpl ordersServiceImpl;


    @Override
    protected BaseService<Orders, String, OrdersDao> getService() {
        return ordersServiceImpl;
    }


    /**
     * 创建订单  存在幂等
     */
    @PostMapping("/createOder")
    public Object create(@Valid @RequestBody Orders  date)  {
        try{
            ordersServiceImpl.createOrder(date);
            return Rets.success("订单创建创建成功");
        }catch(BizException e ){
            return Rets.failure("订单重复提交");
        }
    }

    /**
     * 支付订单
     */
    @PostMapping("/payOrder")
    public Object payOrder(@Valid @RequestBody Orders  date)  {
        try{
            ordersServiceImpl.createPay(date);
            return Rets.success("订单订单支付成功");
        }catch(BizException e ){
            return Rets.failure("订单支付成功");
        }
    }

    /**
     * 取消订单
     */





}
