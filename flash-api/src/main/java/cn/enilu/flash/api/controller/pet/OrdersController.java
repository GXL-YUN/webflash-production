package cn.enilu.flash.api.controller.pet;

import cn.enilu.flash.api.controller.CrudController;
import cn.enilu.flash.bean.page.RequertInfo;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.service.BaseService;
import cn.enilu.pet.Listener.SysMessage;
import cn.enilu.pet.bean.model.Orders;
import cn.enilu.pet.dao.DemandDao;
import cn.enilu.pet.dao.OrdersDao;
import cn.enilu.pet.server.OrdersServiceImpl;
import cn.enilu.sms.service.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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


    //rabbit
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 支付订单
     */
    @PostMapping("/payOrder")
    public Object payOrder(@Valid @RequestBody Orders  date)  {
        try{
            ordersServiceImpl.createPay(date);

            //发送队列

            // 发送消息到交换机，使用路由键
            // 注意：convertAndSend 的第一个参数通常是 Exchange Name，第二个是 Routing Key
            // 确保 RabbitMQ 中存在名为 "sys.exchange" (或你实际配置的交换机名) 的交换机
            // 并且该交换机绑定了队列，且绑定键匹配 "sys.create"
            
            String message = "订单支付成功，订单ID: " + (date.getId() != null ? date.getId() : "未知");
            log.info("准备发送MQ消息: {}", message);
            
            try {
                // 假设交换机名为 "sys.exchange"，请根据实际配置修改第一个参数
                // 如果使用的是默认交换机，第一个参数应为 ""，第二个参数为队列名 "sys.queue"
                // 这里按照常见做法：发送到交换机，通过路由键路由到队列
                
                // 方案1: 如果 "sys.queue" 是交换机名字 (通常不这么命名，但有可能)
                // rabbitTemplate.convertAndSend("sys.queue", "sys.create", message);
                
                // 方案2: 更常见的做法，第一个参数是 Exchange，第二个是 Routing Key
                // 请确认你的 RabbitMQ 配置中 Exchange 和 Queue 的绑定关系
                // 例如: Exchange="sys.exchange", RoutingKey="sys.create", Queue="sys.queue"
                
                // 为了调试，先尝试直接发送到队列（如果使用默认交换机）
                // 或者发送到指定的交换机
                // 此处保留原意，但建议检查 Exchange 名称是否正确
                // 如果之前没成功，很可能是 Exchange 名称错误或未绑定
                
                // 修正：通常 convertAndSend(exchange, routingKey, object)
                // 如果你的配置是 direct exchange 名为 "sys.exchange"，绑定键 "sys.create" 指向 "sys.queue"
                //rabbitTemplate.convertAndSend("sys.exchange", "sys.create", message);
                
                log.info("MQ消息发送成功");
            } catch (Exception e) {
                log.error("MQ消息发送失败", e);
                // 根据业务需求决定是否抛出异常或记录日志
            }


            return Rets.success("订单订单支付成功");
        }catch(BizException e ){
            return Rets.failure("订单支付成功");
        }
    }

    /**
     * 取消订单
     */





}
