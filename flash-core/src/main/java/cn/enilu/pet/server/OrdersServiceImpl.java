package cn.enilu.pet.server;

import cn.enilu.flash.service.BaseService;
import cn.enilu.pet.bean.model.Orders;
import cn.enilu.pet.bean.model.Payment_record;
import cn.enilu.pet.dao.OrdersDao;
import cn.enilu.pet.dao.Payment_recordDao;
import cn.enilu.pet.em.OrderStatus;
import cn.enilu.sms.service.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.IdGenerator;


import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * 订单服务类
 *
 * 用户发起支付
 * 系统调用第三方支付
 * 第三方回调通知
 * 更新订单状态
 * 发送 MQ 做后续处理
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersServiceImpl extends BaseService<Orders, String, OrdersDao> {
    @Autowired
    private OrdersDao ordersDao;

    @Autowired
    private Payment_recordDao payment_recordDao;


    //redis
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    //rabbit
    @Autowired
    private RabbitTemplate rabbitTemplate;



    /**
     * 创建订单   幂等（同一个请求，重复执行 N 次，结果永远是一样的）
     *并发支付 QPS > 1000
     * @param req
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Orders createOrder(Orders  req) throws BizException {
        String dedupKey = "order:dedup:" + req.getFdUserId() + ":" + req.getFdProductId();
        // 1️⃣ 防重（用户级）

        /*尝试往 Redis 中写入一个 key，只有当 key 不存在时才成功​
        ✅ 同时设置 10 秒后自动过期*/
        Boolean ok = redisTemplate.opsForValue()
                .setIfAbsent(dedupKey, "1", 20, TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(ok)) {
            throw new BizException("请勿重复提交");
        }

        // 2️⃣ 创建订单
        Orders order = new Orders();
        order.setFdOrderNo(UUID.randomUUID().toString().replace("-", ""));
        order.setFdUserId(req.getFdUserId());
        order.setFdAmount(req.getFdAmount());
        order.setFdStatus(OrderStatus.WITHPAYMENT.getCode());
        order.setCreateTime(new Date());

        order.setFdAddressId(req.getFdAddressId());
        order.setFdProductId(req.getFdProductId());

        Orders orders= ordersDao.save(order);
        log.info("获取到的id{}--队列发送成功", orders.getFdId());

        // 4️⃣ 发送延迟关单消息
        //15分钟订单未支付自动结束
        // 确保消息体被正确序列化，并设置延迟属性
        // 注意：RabbitMQ 原生不支持 delay 属性，通常需要安装 rabbitmq-delayed-message-exchange 插件
        // 或者使用 x-death/ttl 机制。这里假设已配置支持 delay 的 Exchange 或插件。
        // 如果控制台看不见，常见原因是：
        // 1. Exchange 名称错误或未绑定 Queue
        // 2. Routing Key 不匹配
        // 3. 消息被路由到死信队列或直接丢弃（如果没有消费者且未持久化等）
        // 4. setDelay 需要插件支持，否则无效
        
        // 为了调试，先尝试发送一条不带延迟的标准消息，确认链路通畅
        // 如果标准消息能看见，再排查延迟插件配置
         rabbitTemplate.convertAndSend(
                "order.delay.exchange",
                "order.close", 
                orders.getFdId(),
                message -> {
                    // 设置消息ID便于追踪
                    message.getMessageProperties().setMessageId(UUID.randomUUID().toString());
                    // 尝试设置延迟 (需要 rabbitmq-delayed-message-exchange 插件支持)
                    // 如果没有插件，这行代码可能被忽略或导致异常，取决于客户端版本和配置
                    message.getMessageProperties().setDelay(60*1000);
                    return message;
                }
        );
        return orders;
    }

    /**
     * 订单更新状态
     */


    /**
     * 支付成功并生成支付记录
     */
    @Transactional(rollbackFor = Exception.class)
    public Payment_record createPay(Orders req) throws BizException {
        Orders order = ordersDao.getOne(req.getFdId());
        String status=OrderStatus.WITHPAYMENT.getCode();
        String xfrom=order.getFdStatus();

        if (!xfrom .equals(status)) {
            throw new BizException("订单状态异常");
        }
        // 调用第三方支付
        Payment_record record = new Payment_record();
        record.setFdOrderNo("121");
        record.setFdTthirdPayNo(order.getFdOrderNo());
        record.setFdPayChannel("1");//判断是支付宝还是微信
        record.setFdPayStatus(OrderStatus.PAYMENT.getCode());
        record.setFdTthirdPayNo("22");
        record.setFdTthirdPayNo("21");
        payment_recordDao.save(record);
        // 发送发货/履约消息
        rabbitTemplate.convertAndSend(
                "order.exchange",
                "order.create",
                order.getFdId()
        );

        return record ;
    }


    /**
     * 回调处理
     * @param orderNo
     * @param status
     *
     * 必须校验签名
     * 必须幂等
     * 必须返回第三方要求的 success
     */
    @Transactional
    public void handleCallback(String orderNo, String status) {
        Orders order = ordersDao.getOne(orderNo);
        if (order.getFdStatus() == OrderStatus.PAYMENT.getCode()) {
            return; // 已处理过
        }
        order.setFdStatus(OrderStatus.PAYMENT.getCode());
        ordersDao.save(order);
        // 发送 MQ（发货 / 积分 / 通知）
        //mqSender.sendOrderPaidEvent(orderNo);
    }


    /**
     *
     * @param orderId
     */
    @Transactional
    public void closeOrderIfUnpaid(String orderId) {
        System.out.println("关闭相关：" + orderId);

//        Orders order = orderMapper.selectById(orderId);
//        if (order == null || order.getStatus() != OrderStatus.INIT) {
//            return;
//        }
//
//        order.setStatus(OrderStatus.CLOSED);
//        order.setUpdateTime(LocalDateTime.now());
//        orderMapper.updateById(order);

        // 回滚库存
       // stockMapper.releaseStock(order.getProductId(), order.getQuantity());
    }
}
