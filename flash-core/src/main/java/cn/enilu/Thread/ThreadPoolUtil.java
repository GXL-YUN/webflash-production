package cn.enilu.Thread;

import cn.enilu.Thread.ThreadPoolManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 线程池使用工具类
 */
@Slf4j
@Component
public class ThreadPoolUtil {

    @Autowired
    private ThreadPoolManager threadPoolManager;

    /**
     * 批量执行任务
     */
    public <T> List<T> executeBatch(List<Callable<T>> tasks) {
        List<Future<T>> futures = new ArrayList<>();
        List<T> results = new ArrayList<>();

        try {
            // 提交所有任务
            for (Callable<T> task : tasks) {
                Future<T> future = threadPoolManager.submit(task);
                futures.add(future);
            }

            // 获取所有结果
            for (Future<T> future : futures) {
                try {
                    T result = future.get(30, TimeUnit.SECONDS);
                    results.add(result);
                } catch (TimeoutException e) {
                    log.error("获取任务结果超时", e);
                    future.cancel(true);
                } catch (Exception e) {
                    log.error("获取任务结果异常", e);
                }
            }
        } catch (Exception e) {
            log.error("批量执行任务异常", e);
        }

        return results;
    }

    /**
     * 并行处理列表数据
     */
    public <T, R> List<R> parallelProcess(List<T> dataList, Function<T, R> processor) {
        List<Callable<R>> tasks = dataList.stream()
                .map(data -> (Callable<R>) () -> processor.apply(data))
                .collect(Collectors.toList());

        return executeBatch(tasks);
    }

    /**
     * 并行消费列表数据
     */
    public <T> void parallelConsume(List<T> dataList, Consumer<T> consumer) {
        List<Callable<Void>> tasks = dataList.stream()
                .map(data -> (Callable<Void>) () -> {
                    consumer.accept(data);
                    return null;
                })
                .collect(Collectors.toList());

        executeBatch(tasks);
    }

    /**
     * 带超时的任务执行
     */
    public <T> T executeWithTimeout(Callable<T> task, long timeout, TimeUnit timeUnit)
            throws TimeoutException, ExecutionException, InterruptedException {
        Future<T> future = threadPoolManager.submit(task);

        try {
            return future.get(timeout, timeUnit);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw e;
        }
    }

    /**
     * 获取线程池状态
     */
    public ThreadPoolManager.ThreadPoolStatus getThreadPoolStatus() {
        return threadPoolManager.getThreadPoolStatus();
    }

    /**
     * 健康检查
     */
    public boolean isHealthy() {
        ThreadPoolManager.ThreadPoolStatus status = getThreadPoolStatus();

        // 检查队列是否已满
        if (status.getQueueUsage() > 0.9) {
            log.error("线程池队列使用率超过90%，当前: {}", status.getQueueUsage());
            return false;
        }

        // 检查活跃线程数是否过高
        if (status.getActiveCount() > status.getMaxPoolSize() * 0.8) {
            log.warn("线程池活跃线程数超过80%，当前: {}/{}",
                    status.getActiveCount(), status.getMaxPoolSize());
        }

        return true;
    }
}