package cn.enilu.mq.job;


import cn.enilu.flash.bean.entity.system.Cfg;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.system.CfgService;
import cn.enilu.flash.service.task.JobExecuter;
import cn.enilu.flash.utils.DateUtil;
import cn.enilu.flash.utils.JsonUtil;
import cn.enilu.mq.bean.MqMainLog;
import cn.enilu.mq.service.MqMainLogService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 当乐舞表里面存在未投递或者失败数据  则进行重新投递  直至消费成功
 */

@Component
@Slf4j
public class MqSendJob extends JobExecuter {
    @Autowired
    private MqMainLogService mqMainLogService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private   String GXL_USER_LOGIN_TOKE="WEB-FLASH:MQ:PREVIEW:";

    private final ExecutorService executor = Executors.newFixedThreadPool(8);

    @Override
    public void execute(Map<String, Object> dataMap) throws Exception {
        //存储用户登录用户toke  用于后续接口响应
        /**
         * 批量生成mq消息
         */
//        for(int i=0;i<50;i++){
//            MqMainLog mqMainLog=new MqMainLog();
//            mqMainLog.setFdRequestBody(JsonUtil.toJson("{\n" +
//                    "    \"fdGroupId\": \"1jpf45qe6w5qwerafw125lj0529t0rcr1tw0\", \n" +
//                    "    \"fdRuleSetId\": \"1jpf45qatw5qweradw3ivqi2g1gel4u02fw0\", \n" +
//                    "    \"fdContent\": \"{\\\"content\\\":[{\\\"fdCode\\\":\\\"code\\\",\\\"fdType\\\":\\\"condition\\\",\\\"fdDataType\\\":\\\"string\\\",\\\"fdValue\\\":\\\"1\\\",\\\"fdIsDefault\\\":false},{\\\"fdCode\\\":\\\"fdCode\\\",\\\"fdType\\\":\\\"result\\\",\\\"fdDataType\\\":\\\"person\\\",\\\"fdValue\\\":[{\\\"fdId\\\":\\\"1j440htnmw12w14hriwckrt113j5hfi914w0\\\",\\\"fdName\\\":\\\"宋哲11\\\",\\\"fdNo\\\":\\\"ghceshi0002\\\",\\\"fdLoginName\\\":\\\"lky_songzhe\\\",\\\"fdOrgType\\\":8,\\\"fdParentId\\\":\\\"1i31u3dfuwcw60duw39hu6b33tlrliu3u1w0\\\",\\\"fdInner\\\":true,\\\"eosShowType\\\":false,\\\"fdIsAvailable\\\":true,\\\"fdParentName\\\":\\\"北方华创集团/半导体装备事业群/北方华创微电子/流程与数字化体系\\\"},{\\\"fdId\\\":\\\"1j09o00ucw4w7a60owin9nhl15e73qf2q5w0\\\",\\\"fdName\\\":\\\"浦同乐\\\",\\\"fdNo\\\":\\\"NMC40519\\\",\\\"fdLoginName\\\":\\\"putongle\\\",\\\"fdOrgType\\\":8,\\\"fdParentId\\\":\\\"1jkhqvsulw44w1u07jw11dd5ll1uifn292w0\\\",\\\"fdInner\\\":true,\\\"eosShowType\\\":false,\\\"fdIsAvailable\\\":true,\\\"fdParentName\\\":\\\"北方华创集团/半导体装备事业群/北方华创微电子/技术研究院/电气控制中心/三刻电气技术部\\\"}],\\\"fdIsDefault\\\":false}]}\",\n" +
//                    "    \"fdId\": \"1jpf46btmw5qwerjtw3lgta672m56lavfhw0\" \n" +
//                    "}"));
//            mqMainLog.setFdService("类别名称"+DateUtil.getAllTime());
//            mqMainLog.setFdStatus(0);
//            boolean name =mqMainLogService.saveAsync(mqMainLog);
//        }
        //发送订阅消息
        this.publishPendingMessages();
    }


    /**
     * 投递消息数据
     */
    @Scheduled(fixedDelay = 1000)
    public void publishPendingMessages() {
        List<SearchFilter> filters =new ArrayList<>();
        filters.add(SearchFilter.build("fdStatus", SearchFilter.Operator.EQ, 0, SearchFilter.Join.or));
        filters.add(SearchFilter.build("fdStatus", SearchFilter.Operator.EQ, 2, SearchFilter.Join.or));
        List<MqMainLog> msgs = mqMainLogService.queryAll(filters);
        for (MqMainLog msg : msgs) {
            executor.submit(() -> {
                try {
                    boolean flage= mqMainLogService. pushToMq(msg.getFdId());
                    if(flage){
                        msg.setFdStatus(1);//投递成功
                        redisTemplate.opsForValue().set(GXL_USER_LOGIN_TOKE+msg.getFdId(), flage);
                        log.info("MQ消息发送成功 已存储：{}"+ GXL_USER_LOGIN_TOKE+DateUtil.getAllTime());

                    }else{
                        msg.setFdStatus(2);//投递失败
                    }

                } catch (Exception e) {
                    // 留着下次重试，不更新状态
                    msg.setFdStatus(2);//投递失败
                    log.error("MQ消息发送失败{}", e.getMessage());
                }finally {
                    mqMainLogService.update( msg);
                }
            });
        }
    }
}