package cn.enilu.Thread.bean;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

// ThreadPoolManager.ThreadPoolStatus 类的修复版本
@lombok.Data
public class ThreadPoolStatus {
    private final int poolSize;
    private final int corePoolSize;
    private final int maxPoolSize;
    private final int activeCount;
    private final int queueSize;
    private final long completedTaskCount;
    private final long taskCount;
    private final int customCompletedCount;
    private final int rejectedCount;
    private final int queueCapacity;  // 添加队列容量字段
    private final BlockingQueue<Runnable> workQueue;  // 添加队列引用
    private final long timestamp = System.currentTimeMillis();

    public ThreadPoolStatus(int poolSize, int corePoolSize, int maxPoolSize,
                            int activeCount, int queueSize, long completedTaskCount,
                            long taskCount, int customCompletedCount, int rejectedCount,
                            int queueCapacity, BlockingQueue<Runnable> workQueue) {  // 修改构造函数
        this.poolSize = poolSize;
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.activeCount = activeCount;
        this.queueSize = queueSize;
        this.completedTaskCount = completedTaskCount;
        this.taskCount = taskCount;
        this.customCompletedCount = customCompletedCount;
        this.rejectedCount = rejectedCount;
        this.queueCapacity = queueCapacity;
        this.workQueue = workQueue;
    }

    /**
     * 计算队列使用率
     */
    public double getQueueUsage() {
        if (workQueue != null && workQueue.size() > 0) {
            int totalCapacity = workQueue.size() + workQueue.remainingCapacity();
            if (totalCapacity > 0) {
                return (double) workQueue.size() / totalCapacity;
            }
        }
        return 0.0;
    }

    /**
     * 获取队列剩余容量
     */
    public int getQueueRemainingCapacity() {
        return workQueue != null ? workQueue.remainingCapacity() : 0;
    }

    /**
     * 是否队列已满
     */
    public boolean isQueueFull() {
        return workQueue != null && workQueue.remainingCapacity() == 0;
    }

    /**
     * 获取详细状态信息
     */
    public Map<String, Object> getDetailedStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("timestamp", new Date(timestamp));
        status.put("poolSize", poolSize);
        status.put("corePoolSize", corePoolSize);
        status.put("maxPoolSize", maxPoolSize);
        status.put("activeCount", activeCount);
        status.put("queueSize", queueSize);
        status.put("queueCapacity", queueCapacity);
        status.put("queueRemaining", getQueueRemainingCapacity());
        status.put("queueUsage", String.format("%.2f%%", getQueueUsage() * 100));
        status.put("completedTaskCount", completedTaskCount);
        status.put("taskCount", taskCount);
        status.put("customCompletedCount", customCompletedCount);
        status.put("rejectedCount", rejectedCount);
        status.put("isQueueFull", isQueueFull());
        status.put("isOverloaded", getQueueUsage() > 0.8);
        return status;
    }
}