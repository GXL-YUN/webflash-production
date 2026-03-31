package cn.enilu.Thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 标准线程池工具类 - 生产环境级别
 * 支持线程池监控、优雅关闭、异常处理等
 */
@Slf4j
//@Component
public class ThreadPoolManager {

    // 线程池实例
    private ThreadPoolExecutor threadPoolExecutor;

    // 监控线程执行器
    private ScheduledExecutorService monitorScheduler;

    // 配置参数（可通过application.yml配置）
    @Value("${thread.pool.coreSize:10}")
    private int corePoolSize;

    @Value("${thread.pool.maxSize:50}")
    private int maxPoolSize;

    @Value("${thread.pool.keepAliveTime:60}")
    private long keepAliveTime;

    @Value("${thread.pool.queueCapacity:1000}")
    private int queueCapacity;

    @Value("${thread.pool.threadPrefix:app-thread}")
    private String threadNamePrefix;

    // 监控指标
    private final AtomicInteger completedTaskCount = new AtomicInteger(0);
    private final AtomicInteger rejectedTaskCount = new AtomicInteger(0);
    private volatile boolean isShuttingDown = false;

    /**
     * 初始化线程池
     */
    @PostConstruct
    public void init() {
        if (threadPoolExecutor != null && !threadPoolExecutor.isShutdown()) {
            log.warn("线程池已初始化，跳过重复初始化");
            return;
        }

        log.info("初始化线程池，核心线程数: {}, 最大线程数: {}, 队列容量: {}",
                corePoolSize, maxPoolSize, queueCapacity);

        // 创建线程工厂
        ThreadFactory threadFactory = new CustomThreadFactory(threadNamePrefix);

        // 创建拒绝策略处理器
        RejectedExecutionHandler rejectionHandler = new CustomRejectedExecutionHandler();

        // 创建线程池
        threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity),
                threadFactory,
                rejectionHandler
        );

        // 允许核心线程超时
        threadPoolExecutor.allowCoreThreadTimeOut(true);

        // 启动监控线程
        startMonitor();

        log.info("线程池初始化完成");
    }

    /**
     * 提交任务
     */
    public <T> Future<T> submit(Callable<T> task) {
        validateThreadPoolState();
        try {
            return threadPoolExecutor.submit(wrapCallable(task));
        } catch (RejectedExecutionException e) {
            log.error("任务提交被拒绝，当前线程池状态: {}", getThreadPoolStatus().toString());
            throw new RejectedExecutionException("线程池任务队列已满，无法提交新任务", e);
        } catch (Exception e) {
            log.error("提交任务失败", e);
            throw new RuntimeException("提交任务失败", e);
        }
    }

    /**
     * 执行任务
     */
    public void execute(Runnable task) {
        validateThreadPoolState();
        try {
            threadPoolExecutor.execute(wrapRunnable(task));
        } catch (RejectedExecutionException e) {
            log.error("任务执行被拒绝，当前线程池状态: {}", getThreadPoolStatus().toString());
            throw new RejectedExecutionException("线程池任务队列已满，无法执行新任务", e);
        } catch (Exception e) {
            log.error("执行任务失败", e);
            throw new RuntimeException("执行任务失败", e);
        }
    }

    /**
     * 验证线程池状态
     */
    private void validateThreadPoolState() {
        if (threadPoolExecutor == null) {
            throw new IllegalStateException("线程池未初始化，请先调用init()方法");
        }

        if (threadPoolExecutor.isShutdown()) {
            throw new RejectedExecutionException("线程池已关闭，无法接受新任务");
        }

        if (threadPoolExecutor.isTerminated()) {
            throw new RejectedExecutionException("线程池已终止，无法接受新任务");
        }
    }

    /**
     * 包装Callable，添加异常处理
     */
    private <T> Callable<T> wrapCallable(Callable<T> task) {
        return () -> {
            try {
                T result = task.call();
                completedTaskCount.incrementAndGet();
                return result;
            } catch (Exception e) {
                log.error("任务执行异常", e);
                throw e;
            }
        };
    }

    /**
     * 包装Runnable，添加异常处理
     */
    private Runnable wrapRunnable(Runnable task) {
        return () -> {
            try {
                task.run();
                completedTaskCount.incrementAndGet();
            } catch (Exception e) {
                log.error("任务执行异常", e);
                throw e;
            }
        };
    }

    /**
     * 优雅关闭线程池
     */
    @PreDestroy
    public void shutdown() {
        if (isShuttingDown) {
            return;
        }

        isShuttingDown = true;
        log.info("开始优雅关闭线程池...");

        // 先关闭监控线程
        if (monitorScheduler != null && !monitorScheduler.isShutdown()) {
            monitorScheduler.shutdownNow();
            log.info("监控线程已关闭");
        }

        // 停止接收新任务
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();

            try {
                // 等待现有任务完成
                if (!threadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                    // 强制关闭
                    log.warn("线程池未在60秒内完成，尝试强制关闭...");
                    threadPoolExecutor.shutdownNow();

                    // 再次等待
                    if (!threadPoolExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                        log.error("线程池强制关闭失败");
                    } else {
                        log.info("线程池强制关闭成功");
                    }
                } else {
                    log.info("线程池已优雅关闭");
                }
            } catch (InterruptedException e) {
                threadPoolExecutor.shutdownNow();
                Thread.currentThread().interrupt();
                log.error("关闭线程池时被中断", e);
            }
        }

        log.info("线程池关闭完成");
    }

    /**
     * 立即关闭线程池
     */
    public void shutdownNow() {
        isShuttingDown = true;

        if (monitorScheduler != null && !monitorScheduler.isShutdown()) {
            monitorScheduler.shutdownNow();
        }

        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdownNow();
        }

        log.info("线程池立即关闭");
    }

    /**
     * 获取线程池状态
     */
    public ThreadPoolStatus getThreadPoolStatus() {
        if (threadPoolExecutor == null) {
            return ThreadPoolStatus.createShutdownStatus();
        }

        BlockingQueue<Runnable> queue = threadPoolExecutor.getQueue();
        int queueSize = queue.size();
        int queueRemainingCapacity = queue.remainingCapacity();

        return new ThreadPoolStatus(
                threadPoolExecutor.getPoolSize(),
                threadPoolExecutor.getCorePoolSize(),
                threadPoolExecutor.getMaximumPoolSize(),
                threadPoolExecutor.getActiveCount(),
                queueSize,
                threadPoolExecutor.getCompletedTaskCount(),
                threadPoolExecutor.getTaskCount(),
                completedTaskCount.get(),
                rejectedTaskCount.get(),
                queueCapacity,
                queueRemainingCapacity
        );
    }

    /**
     * 启动监控线程
     */
    private void startMonitor() {
        monitorScheduler = Executors.newSingleThreadScheduledExecutor(
                r -> {
                    Thread t = new Thread(r, threadNamePrefix + "-monitor");
                    t.setDaemon(true);
                    t.setPriority(Thread.MIN_PRIORITY);
                    return t;
                }
        );

        monitorScheduler.scheduleAtFixedRate(() -> {
            try {
                if (threadPoolExecutor == null || threadPoolExecutor.isShutdown()) {
                    return;
                }

                ThreadPoolStatus status = getThreadPoolStatus();

                // 监控队列使用率
                if (status.getQueueUsage() > 0.8) {
                    log.warn("线程池队列使用率超过80%，当前队列大小: {}/{} (使用率: {:.1f}%)",
                            status.getQueueSize(),
                            status.getQueueCapacity(),
                            status.getQueueUsage() * 100);
                }

                // 监控活跃线程数
                if (status.getActiveCount() > corePoolSize * 2) {
                    log.warn("线程池活跃线程数较多，当前活跃数: {} (核心线程数: {})",
                            status.getActiveCount(),
                            corePoolSize);
                }

                // 定期记录线程池状态
                if (log.isDebugEnabled()) {
                    log.debug("线程池状态: {}", status.toMapStr());
                }

            } catch (Exception e) {
                log.error("监控线程池状态异常", e);
            }
        }, 5, 30, TimeUnit.SECONDS);
    }

    /**
     * 获取线程池执行器实例
     */
    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    /**
     * 检查线程池是否运行中
     */
    public boolean isRunning() {
        return threadPoolExecutor != null &&
                !threadPoolExecutor.isShutdown() &&
                !threadPoolExecutor.isTerminated() &&
                !threadPoolExecutor.isTerminating();
    }

    /**
     * 获取队列剩余容量
     */
    public int getQueueRemainingCapacity() {
        if (threadPoolExecutor == null) {
            return 0;
        }
        return threadPoolExecutor.getQueue().remainingCapacity();
    }

    /**
     * 获取队列当前大小
     */
    public int getQueueSize() {
        if (threadPoolExecutor == null) {
            return 0;
        }
        return threadPoolExecutor.getQueue().size();
    }

    /**
     * 自定义线程工厂
     */
    private static class CustomThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public CustomThreadFactory(String prefix) {
            this.namePrefix = prefix + "-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, namePrefix + threadNumber.getAndIncrement());
            t.setDaemon(false);
            t.setPriority(Thread.NORM_PRIORITY);

            // 设置异常处理器
            t.setUncaughtExceptionHandler((thread, throwable) -> {
                log.error("线程 {} 发生未捕获异常: {}", thread.getName(), throwable.getMessage(), throwable);
            });

            return t;
        }
    }

    /**
     * 自定义拒绝策略
     */
    private class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            rejectedTaskCount.incrementAndGet();

            ThreadPoolStatus status = getThreadPoolStatus();
            log.warn("任务被拒绝，当前线程池状态: {}", status.toString());
            log.warn("活跃线程数={}, 队列大小={}/{}, 已完成任务数={}",
                    executor.getActiveCount(),
                    executor.getQueue().size(),
                    queueCapacity,
                    executor.getCompletedTaskCount());

            // 尝试使用降级策略
            try {
                if (!executor.isShutdown() && !executor.isTerminated()) {
                    // 如果队列未满，尝试重新放入队列
                    if (executor.getQueue().offer(r, 100, TimeUnit.MILLISECONDS)) {
                        log.info("任务重新入队成功");
                        return;
                    }
                }

                // 如果队列已满或线程池已关闭，执行默认的拒绝策略
                log.error("线程池已满，无法处理更多任务，执行默认拒绝策略");
                throw new RejectedExecutionException("线程池和队列已满，无法处理任务");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("重试提交任务时被中断", e);
                throw new RejectedExecutionException("任务提交被中断", e);
            }
        }
    }

    /**
     * 线程池状态信息类
     */
    public static class ThreadPoolStatus {
        private final int poolSize;
        private final int corePoolSize;
        private final int maxPoolSize;
        private final int activeCount;
        private final int queueSize;
        private final long completedTaskCount;
        private final long taskCount;
        private final int customCompletedCount;
        private final int rejectedCount;
        private final int queueCapacity;
        private final int queueRemainingCapacity;
        private final long timestamp = System.currentTimeMillis();

        // 用于标识线程池是否已关闭
        private final boolean isShutdown;

        public ThreadPoolStatus(int poolSize, int corePoolSize, int maxPoolSize,
                                int activeCount, int queueSize, long completedTaskCount,
                                long taskCount, int customCompletedCount, int rejectedCount,
                                int queueCapacity, int queueRemainingCapacity) {
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
            this.queueRemainingCapacity = queueRemainingCapacity;
            this.isShutdown = false;
        }

        /**
         * 创建线程池关闭时的状态对象
         */
        public static ThreadPoolStatus createShutdownStatus() {
            return new ThreadPoolStatus(true);
        }

        private ThreadPoolStatus(boolean isShutdown) {
            this.poolSize = 0;
            this.corePoolSize = 0;
            this.maxPoolSize = 0;
            this.activeCount = 0;
            this.queueSize = 0;
            this.completedTaskCount = 0;
            this.taskCount = 0;
            this.customCompletedCount = 0;
            this.rejectedCount = 0;
            this.queueCapacity = 0;
            this.queueRemainingCapacity = 0;
            this.isShutdown = isShutdown;
        }

        /**
         * 计算队列使用率
         */
        public double getQueueUsage() {
            int totalCapacity = queueSize + queueRemainingCapacity;
            if (totalCapacity > 0) {
                return (double) queueSize / totalCapacity;
            }
            return 0.0;
        }

        /**
         * 获取队列总容量
         */
        public int getQueueCapacity() {
            return queueCapacity;
        }

        /**
         * 获取队列剩余容量
         */
        public int getQueueRemainingCapacity() {
            return queueRemainingCapacity;
        }

        /**
         * 检查队列是否已满
         */
        public boolean isQueueFull() {
            return queueRemainingCapacity == 0;
        }

        /**
         * 检查线程池是否已关闭
         */
        public boolean isShutdown() {
            return isShutdown;
        }

        /**
         * 转换为Map格式
         */
        public Map<String, Object> toMap() {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("timestamp", new Date(timestamp));
            map.put("isShutdown", isShutdown);
            map.put("poolSize", poolSize);
            map.put("corePoolSize", corePoolSize);
            map.put("maxPoolSize", maxPoolSize);
            map.put("activeCount", activeCount);
            map.put("queueSize", queueSize);
            map.put("queueCapacity", queueCapacity);
            map.put("queueRemainingCapacity", queueRemainingCapacity);
            map.put("queueUsage", String.format("%.2f%%", getQueueUsage() * 100));
            map.put("completedTaskCount", completedTaskCount);
            map.put("taskCount", taskCount);
            map.put("customCompletedCount", customCompletedCount);
            map.put("rejectedCount", rejectedCount);
            map.put("isQueueFull", isQueueFull());
            return map;
        }

        public Map<String, Object> toMapStr() {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("timestamp", new Date(timestamp));
            map.put("线程池是否已被关闭,false表示线程池运行正常:", isShutdown);
            map.put("当前线程池中存活的线程总数（包括空闲线程和活动线程）:", poolSize);
            map.put("核心线程数:", corePoolSize);
            map.put("最大线程数:", maxPoolSize);
            map.put("正在执行任务的活跃线程数:", activeCount);
            map.put("任务队列中当前积压等待执行的任务数量", queueSize);
            map.put("任务队列的最大容量", queueCapacity);
            map.put("任务队列剩余可用容量", queueRemainingCapacity);
            map.put("任务队列当前使用率", String.format("%.2f%%", getQueueUsage() * 100));
            map.put("线程池自启动以来已完成执行的任务总数", completedTaskCount);
            map.put("线程池自启动以来被提交（并计划执行）的任务总数", taskCount);
            map.put("自定义的已完成任务计数", customCompletedCount);
            map.put("被拒绝的任务数量", rejectedCount);
            map.put("当前任务队列是否已满", isQueueFull());
            return map;
        }

        @Override
        public String toString() {
            return String.format(
                    "ThreadPoolStatus{isShutdown=%s, poolSize=%d, activeCount=%d, queue=%d/%d (%.1f%%)}",
                    isShutdown, poolSize, activeCount, queueSize, queueCapacity, getQueueUsage() * 100
            );
        }

        // Getters
        public int getPoolSize() { return poolSize; }
        public int getCorePoolSize() { return corePoolSize; }
        public int getMaxPoolSize() { return maxPoolSize; }
        public int getActiveCount() { return activeCount; }
        public int getQueueSize() { return queueSize; }
        public long getCompletedTaskCount() { return completedTaskCount; }
        public long getTaskCount() { return taskCount; }
        public int getCustomCompletedCount() { return customCompletedCount; }
        public int getRejectedCount() { return rejectedCount; }
        public long getTimestamp() { return timestamp; }
    }
}
