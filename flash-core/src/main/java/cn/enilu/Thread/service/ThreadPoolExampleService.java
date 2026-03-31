package cn.enilu.Thread.service;

import cn.enilu.Thread.ThreadPoolManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 线程池使用示例服务
 */
@Slf4j
@Service
public class ThreadPoolExampleService {

    @Autowired
    private ThreadPoolManager threadPoolManager;

    /**
     * 示例1: 批量处理任务
     */
    public List<String> batchProcessTasks(List<Integer> taskIds) {
        List<Future<String>> futures = new ArrayList<>();
        List<String> results = new ArrayList<>();

        // 提交所有任务
        for (Integer taskId : taskIds) {
            Future<String> future = threadPoolManager.submit(() -> processTask(taskId));
            futures.add(future);
        }

        // 获取所有结果
        for (Future<String> future : futures) {
            try {
                String result = future.get(5, TimeUnit.SECONDS);
                results.add(result);
            } catch (TimeoutException e) {
                log.error("任务执行超时", e);
                results.add("任务超时");
                future.cancel(true);
            } catch (InterruptedException e) {
                log.error("任务被中断", e);
                Thread.currentThread().interrupt();
                results.add("任务中断");
            } catch (ExecutionException e) {
                log.error("任务执行异常", e);
                results.add("任务异常: " + e.getCause().getMessage());
            }
        }

        return results;
    }

    /**
     * 示例2: 并行处理数据
     */
    public List<String> parallelProcessData(List<String> dataList) {
        return dataList.parallelStream()
                .map(data -> CompletableFuture.supplyAsync(() ->
                {
                    try {
                        return processData(data);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }, threadPoolManager.getThreadPoolExecutor()))
                .map(future -> {
                    try {
                        return future.get(3, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        log.error("处理数据失败", e);
                        return "处理失败: " + e.getMessage();
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 示例3: 异步回调处理
     */
    public void asyncProcessWithCallback(List<Integer> items, Callback callback) {
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(items.size());

        for (Integer item : items) {
            threadPoolManager.execute(() -> {
                try {
                    String result = processItem(item);
                    successCount.incrementAndGet();
                    callback.onSuccess(item, result);
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                    callback.onFailure(item, e);
                } finally {
                    latch.countDown();
                }
            });
        }

        // 等待所有任务完成
        try {
            boolean completed = latch.await(10, TimeUnit.SECONDS);
            if (!completed) {
                log.warn("部分任务未在10秒内完成");
            }

            log.info("异步处理完成: 成功={}, 失败={}", successCount.get(), failureCount.get());
            callback.onComplete(successCount.get(), failureCount.get());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("等待任务完成时被中断", e);
        }
    }

    /**
     * 示例4: 监控线程池状态
     */
    public void monitorAndControl() {
        ThreadPoolManager.ThreadPoolStatus status = threadPoolManager.getThreadPoolStatus();

        log.info("当前线程池状态: {}", status.toString());
        log.info("详细状态: {}", status.toMap());

        // 根据状态进行控制
        if (status.getQueueUsage() > 0.8) {
            log.warn("线程池队列使用率超过80%，当前: {:.1f}%", status.getQueueUsage() * 100);
            // 可以在这里添加流量控制逻辑
        }

        if (status.getActiveCount() > status.getCorePoolSize() * 2) {
            log.warn("线程池活跃线程数较多，当前: {}/{}",
                    status.getActiveCount(), status.getMaxPoolSize());
        }

        if (status.isQueueFull()) {
            log.error("线程池队列已满，可能需要调整配置或限流");
        }

        // 检查线程池健康状况
        if (!threadPoolManager.isRunning()) {
            log.error("线程池已停止运行");
        }
    }

    /**
     * 示例5: 处理任务并返回CompletableFuture
     */
    public CompletableFuture<String> processWithCompletableFuture(String input) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return processTaskAsync(input);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }, threadPoolManager.getThreadPoolExecutor());
    }

    /**
     * 示例6: 批量提交并获取所有结果
     */
    public List<String> batchSubmitAndWaitAll(List<String> inputs, long timeout, TimeUnit unit)  {
        List<CompletableFuture<String>> futures = inputs.stream()
                .map(input -> CompletableFuture.supplyAsync(() ->
                {
                    try {
                        return processInput(input);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }, threadPoolManager.getThreadPoolExecutor()))
                .collect(Collectors.toList());

        // 等待所有任务完成
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        try {
            allFutures.get(timeout, unit);
        } catch (Exception e) {
            log.error("等待所有任务完成时出错", e);
        }

        // 收集结果
        return futures.stream()
                .map(future -> {
                    try {
                        return future.getNow("默认值");
                    } catch (Exception e) {
                        return "获取失败: " + e.getMessage();
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 示例7: 使用线程池处理Web请求
     */
    public String processHttpRequest(String requestId) {
        // 检查线程池状态
        if (!threadPoolManager.isRunning()) {
            return "线程池不可用，请求ID: " + requestId;
        }

        if (threadPoolManager.getQueueRemainingCapacity() < 10) {
            log.warn("线程池队列剩余容量不足，当前: {}",
                    threadPoolManager.getQueueRemainingCapacity());
        }

        try {
            Future<String> future = threadPoolManager.submit(() -> {
                // 模拟HTTP请求处理
                Thread.sleep(100);
                return "处理请求: " + requestId + "，线程: " + Thread.currentThread().getName();
            });

            return future.get(2, TimeUnit.SECONDS);

        } catch (RejectedExecutionException e) {
            log.error("请求被拒绝，请求ID: {}", requestId, e);
            return "请求被拒绝，请稍后重试";
        } catch (TimeoutException e) {
            log.error("请求处理超时，请求ID: {}", requestId, e);
            return "请求处理超时";
        } catch (Exception e) {
            log.error("请求处理异常，请求ID: {}", requestId, e);
            return "请求处理异常";
        }
    }

    // 模拟的业务方法
    private String processTask(Integer taskId) throws InterruptedException {
        Thread.sleep(50);
        return "任务" + taskId + "处理完成";
    }

    private String processData(String data) throws InterruptedException {
        Thread.sleep(30);
        return "处理数据: " + data;
    }

    private String processItem(Integer item) throws InterruptedException {
        if (item % 5 == 0) {
            throw new RuntimeException("测试异常: 项目" + item);
        }
        Thread.sleep(20);
        return "项目" + item + "处理成功";
    }

    private String processTaskAsync(String input) throws InterruptedException {
        Thread.sleep(100);
        return "异步处理: " + input;
    }

    private String processInput(String input) throws InterruptedException {
        Thread.sleep(80);
        return "输入处理: " + input;
    }

    /**
     * 回调接口
     */
    public interface Callback {
        void onSuccess(Integer item, String result);
        void onFailure(Integer item, Exception e);
        void onComplete(int successCount, int failureCount);
    }

    /**
     * 测试主方法
     */
    public static void main(String[] args) throws Exception {
        // 创建配置
        ThreadPoolManager manager = new ThreadPoolManager();

        // 设置配置参数
        manager.getClass().getDeclaredField("corePoolSize").set(manager, 5);
        manager.getClass().getDeclaredField("maxPoolSize").set(manager, 10);
        manager.getClass().getDeclaredField("keepAliveTime").set(manager, 30L);
        manager.getClass().getDeclaredField("queueCapacity").set(manager, 50);
        manager.getClass().getDeclaredField("threadNamePrefix").set(manager, "demo-thread");

        // 初始化
        manager.init();

        // 创建示例服务
        ThreadPoolExampleService service = new ThreadPoolExampleService();
        service.threadPoolManager = manager;

        // 测试批量处理
        List<Integer> taskIds = IntStream.range(1, 11)
                .boxed()
                .collect(Collectors.toList());

        System.out.println("=== 测试批量处理 ===");
        List<String> results = service.batchProcessTasks(taskIds);
        results.forEach(System.out::println);

        // 测试监控
        System.out.println("\n=== 测试监控 ===");
        service.monitorAndControl();

        // 关闭线程池
        manager.shutdown();
    }
}
