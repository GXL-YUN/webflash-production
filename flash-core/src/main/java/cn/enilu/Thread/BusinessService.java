package cn.enilu.Thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class BusinessService {

    @Autowired
    private ThreadPoolUtil threadPoolUtil;

    /**
     * 示例1：批量处理数据
     */
    public List<String> processBatchData(List<Integer> ids) {
        List<String> results = threadPoolUtil.parallelProcess(ids, id -> {
            // 模拟业务处理
            try {
                Thread.sleep(100);
                return "Processed-" + id;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Error-" + id;
            }
        });

        return results;
    }

    /**
     * 示例2：异步执行任务
     */
    public void asyncProcess(String data) {
        threadPoolUtil.parallelConsume(List.of(data), item -> {
            // 异步处理逻辑
            processItem(item);
        });
    }

    /**
     * 示例3：带超时的任务
     */
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
    }
}