package cn.enilu.config;

import cn.enilu.Thread.ThreadPoolManager;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置类
 */
@Configuration
public class ThreadPoolConfig {

    /**
     * 线程池配置属性
     */
    @Data
    @ConfigurationProperties(prefix = "thread.pool")
    public static class ThreadPoolProperties {
        private int coreSize = 10;
        private int maxSize = 50;
        private long keepAliveTime = 60;
        private int queueCapacity = 1000;
        private String threadPrefix = "app-thread";

        // 监控配置
        private boolean enableMonitor = true;
        private int monitorIntervalSeconds = 30;
        private double queueWarningThreshold = 0.8; // 队列使用率警告阈值
        private double activeThreadWarningThreshold = 0.8; // 活跃线程警告阈值

        // 关闭配置
        private int shutdownTimeoutSeconds = 60;
        private int shutdownForceTimeoutSeconds = 30;
    }

    /**
     * 创建线程池管理器Bean
     */
    @Bean
    public ThreadPoolManager threadPoolManager(ThreadPoolProperties properties) {
        // 可以使用反射设置属性，或者在ThreadPoolManager中添加setter方法
        ThreadPoolManager manager = new ThreadPoolManager();

        // 通过反射设置属性
        try {
            java.lang.reflect.Field coreSizeField = ThreadPoolManager.class
                    .getDeclaredField("corePoolSize");
            coreSizeField.setAccessible(true);
            coreSizeField.set(manager, properties.getCoreSize());

            java.lang.reflect.Field maxSizeField = ThreadPoolManager.class
                    .getDeclaredField("maxPoolSize");
            maxSizeField.setAccessible(true);
            maxSizeField.set(manager, properties.getMaxSize());

            java.lang.reflect.Field keepAliveTimeField = ThreadPoolManager.class
                    .getDeclaredField("keepAliveTime");
            keepAliveTimeField.setAccessible(true);
            keepAliveTimeField.set(manager, properties.getKeepAliveTime());

            java.lang.reflect.Field queueCapacityField = ThreadPoolManager.class
                    .getDeclaredField("queueCapacity");
            queueCapacityField.setAccessible(true);
            queueCapacityField.set(manager, properties.getQueueCapacity());

            java.lang.reflect.Field threadNamePrefixField = ThreadPoolManager.class
                    .getDeclaredField("threadNamePrefix");
            threadNamePrefixField.setAccessible(true);
            threadNamePrefixField.set(manager, properties.getThreadPrefix());

        } catch (Exception e) {
            throw new RuntimeException("设置线程池属性失败", e);
        }

        // 初始化
        manager.init();
        return manager;
    }

    /**
     * 创建ThreadPoolProperties Bean
     */
    @Bean
    @ConfigurationProperties(prefix = "thread.pool")
    public ThreadPoolProperties threadPoolProperties() {
        return new ThreadPoolProperties();
    }

    /**
     * 创建线程池监控配置
     */
    @Bean
    public ThreadPoolMonitorConfig threadPoolMonitorConfig(ThreadPoolProperties properties) {
        ThreadPoolMonitorConfig config = new ThreadPoolMonitorConfig();
        config.setEnableMonitor(properties.isEnableMonitor());
        config.setMonitorIntervalSeconds(properties.getMonitorIntervalSeconds());
        config.setQueueWarningThreshold(properties.getQueueWarningThreshold());
        config.setActiveThreadWarningThreshold(properties.getActiveThreadWarningThreshold());
        return config;
    }

    /**
     * 线程池监控配置
     */
    @Data
    public static class ThreadPoolMonitorConfig {
        private boolean enableMonitor = true;
        private int monitorIntervalSeconds = 30;
        private double queueWarningThreshold = 0.8;
        private double activeThreadWarningThreshold = 0.8;
    }

    /**
     * 创建默认的任务拒绝处理器
     */
    @Bean
    public TaskRejectionHandler taskRejectionHandler() {
        return new DefaultTaskRejectionHandler();
    }

    /**
     * 任务拒绝处理器接口
     */
    public interface TaskRejectionHandler {
        void handleRejection(Runnable task, ThreadPoolExecutor executor);
    }

    /**
     * 默认任务拒绝处理器
     */
    public static class DefaultTaskRejectionHandler implements TaskRejectionHandler {
        @Override
        public void handleRejection(Runnable task, ThreadPoolExecutor executor) {
            // 可以记录到数据库、发送告警等
            System.err.println("任务被拒绝，线程池状态: " +
                    "活跃线程数=" + executor.getActiveCount() +
                    ", 队列大小=" + executor.getQueue().size() +
                    ", 队列剩余容量=" + executor.getQueue().remainingCapacity());
        }
    }
}
