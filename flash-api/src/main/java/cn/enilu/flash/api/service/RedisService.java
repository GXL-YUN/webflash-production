package cn.enilu.flash.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.connection.stream.StringRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 其他方法...

    //字符串设置
    // 设置值
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 设置值并设置过期时间
    public void setWithExpire(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    // 获取值
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 删除键
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    // 设置过期时间
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }


    //hsah设置

    // 设置Hash字段值
    public void hPut(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    // 获取Hash字段值
    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    // 获取整个Hash
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    //list操作
    // 从左边插入
    public Long lPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    // 获取列表范围
    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }



    //消息队列   生产者
    public void sendMessage(String streamKey, Map<String, Object> message) {
        MapRecord<String, String, Object> record = StreamRecords.newRecord()
                .in(streamKey)
                .ofMap(message);
        redisTemplate.opsForStream().add(record);
    }

    public void sendMessageWithId(String streamKey, String id, Map<String, Object> message) {
        MapRecord<String, String, Object> record = StreamRecords.newRecord()
                .in(streamKey)
                .withId(RecordId.of(id))
                .ofMap(message);
        redisTemplate.opsForStream().add(record);
    }


}