package cn.enilu.flash.api.controller.Thread;

import cn.enilu.Thread.BusinessService;
import cn.enilu.Thread.ThreadPoolManager;
import cn.enilu.Thread.ThreadPoolUtil;
import cn.enilu.flash.bean.vo.front.Rets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/monitor")
@Slf4j
public class ThreadPoolMonitorController {

    @Autowired
    private ThreadPoolUtil threadPoolUtil;



    @Autowired
    private BusinessService businessService;

    @GetMapping("/threadpool/status")
    public ResponseEntity<?> getThreadPoolStatus() {
        ThreadPoolManager.ThreadPoolStatus status = threadPoolUtil.getThreadPoolStatus();

        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("message", "success");
        result.put("data", status);
        result.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/threadpool/health")
    public Object checkThreadPoolHealth() {
        boolean healthy = threadPoolUtil.isHealthy();

        Map<String, Object> result = new HashMap<>();
        result.put("code", healthy ? 0 : 1);
        result.put("message", healthy ? "线程池健康" : "线程池异常");
        result.put("data", threadPoolUtil.getThreadPoolStatus());
        result.put("timestamp", System.currentTimeMillis());
        return Rets.failure(result.toString());
    }


    /**
     * 示例1：批量处理数据
     */
    @GetMapping("/threadpool/all")
    public Object processBatchData() {
        List<Integer> ids=new ArrayList<>();
        for (int i=0;i<10000;i++){
            ids.add(i);
        }
        List<String> results = threadPoolUtil.parallelProcess(ids, id -> {
            // 模拟业务处理
            try {
                Thread.sleep(100);
                return "运行-" + id;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "错误-" + id;
            }
        });
        return Rets.failure(results.toString());
    }

    /**
     * 示例2：异步执行任务
     */
    @GetMapping("/threadpool/one")
    public void asyncProcess() {
        threadPoolUtil.parallelConsume(List.of("测试数据"), item -> {
            // 异步处理逻辑
            processItem(item);
        });
    }

    /**
     * 示例3：带超时的任务
     */

    @GetMapping("/threadpool/time")
    public String executeWithTimeout() throws Exception {
        return threadPoolUtil.executeWithTimeout(() -> {
            // 模拟耗时操作
            Thread.sleep(5000);
            return "Task Completed";
        }, 3, TimeUnit.SECONDS);
    }

    /**
     * 示例4：获取监控信息
     */

    @GetMapping("/threadpool/massage")
    public Map<String, Object> getPoolMetrics() {
        ThreadPoolManager.ThreadPoolStatus status = threadPoolUtil.getThreadPoolStatus();

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("activeThreads", status.getActiveCount());
        metrics.put("queueSize", status.getQueueSize());
        metrics.put("completedTasks", status.getCompletedTaskCount());
        metrics.put("queueUsage", String.format("%.2f%%", status.getQueueUsage() * 100));
        metrics.put("timestamp", status.getTimestamp());

        return metrics;
    }

    private void processItem(String item) {
        // 业务处理逻辑
        log.info("测试"+item);
    }

}