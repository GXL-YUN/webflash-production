package cn.enilu.mq.service;

import cn.enilu.flash.service.BaseService;
import cn.enilu.log.bean.model.ApiLog;
import cn.enilu.log.dao.ApiLogDao;
import cn.enilu.mq.bean.MqMainLog;
import cn.enilu.mq.dao.MqMainLogDao;
import cn.enilu.pet.bean.model.Orders;
import cn.enilu.util.StringUtil;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class MqMainLogService extends  BaseService<MqMainLog, String , MqMainLogDao> {
    // 使用线程池
    @Autowired
    private MqMainLogDao mqMainLogDao;


    //redis
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    //rabbit
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 创建一个“固定大小为 2 的线程池”，用来统一管理和执行异步任务。
     * 也就是说：
     * 👥 最多同时只有 2 个工作线程​ 在跑
     * 📥 超出的任务会排队等待
     * ✅ 线程复用，不会每次 new Thread()
     */
    private final ExecutorService executor = Executors.newFixedThreadPool(8);
    public boolean saveAsync(MqMainLog mqMainLog) {
        //新增mq消息数据
        executor.submit(() -> {
            try {
                MqMainLog orders=mqMainLogDao.save(mqMainLog);
               //将该数据推送到mq队列中去
               // pushToMq(orders.getFdId());
            } catch (Exception e) {
                log.error("MQ新增数据失败", e.getMessage());
            }
        });
        return true;
    }


    /**
     * 业务队列
     */
    public boolean pushToMq(String fdId){
       JSONObject map =new JSONObject();
        map.put("fdId", fdId);
        rabbitTemplate.convertAndSend(
                "sys.exchange",
                "sys.create",
                map.toString(),
                message -> {
                    // 设置消息ID便于追踪
                    message.getMessageProperties().setMessageId(UUID.randomUUID().toString());
                    // 尝试设置延迟 (需要 rabbitmq-delayed-message-exchange 插件支持)
                    // 如果没有插件，这行代码可能被忽略或导致异常，取决于客户端版本和配置
                    //message.getMessageProperties().setDelay(60*1000);
                    return message;
                }
        );
        return true;
    };
}
