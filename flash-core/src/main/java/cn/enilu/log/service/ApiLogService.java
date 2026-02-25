package cn.enilu.log.service;

import cn.enilu.flash.service.BaseService;
import cn.enilu.log.bean.model.ApiLog;
import cn.enilu.log.dao.ApiLogDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class ApiLogService extends BaseService<ApiLog, Long, ApiLogDao> {
    // 使用线程池
    @Autowired
    private ApiLogDao apiLogDao;

    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    public void saveAsync(ApiLog apiLog) {
        executor.submit(() -> {
            try {
                // 临时只打印日志
                log.info("API日志记录: 请求ID={}, 方法={}, URI={}, 耗时={}ms",
                        apiLog.getRequestId(), apiLog.getMethod(), apiLog.getUri(), apiLog.getDuration());

                // 等 Dao 配置好后再取消注释
                if (apiLogDao != null) {
                    apiLogDao.save(apiLog);
                }

            } catch (Exception e) {
                log.error("保存API日志失败", e);
            }
        });
    }
}
