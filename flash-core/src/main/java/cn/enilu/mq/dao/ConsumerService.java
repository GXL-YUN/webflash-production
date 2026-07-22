package cn.enilu.mq.dao;


import cn.enilu.mq.bean.MqMainLog;

/**
 * 消息队列中间件消息主体
 */
public interface ConsumerService {
    void execute(MqMainLog msgLog)throws Exception;
}
