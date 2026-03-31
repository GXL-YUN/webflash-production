//package cn.enilu.Thread;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.concurrent.*;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * 线程池管理器测试类
// */
//@SpringBootTest
//class ThreadPoolManagerTest {
//
//    private ThreadPoolManager threadPoolManager;
//    private final AtomicInteger completedTasks = new AtomicInteger(0);
//
//    @BeforeEach
//    void setUp() {
//        threadPoolManager = new ThreadPoolManager();
//        // 设置配置参数
//        ReflectionTestUtils.setField(threadPoolManager, "corePoolSize", 2);
//        ReflectionTestUtils.setField(threadPoolManager, "maxPoolSize", 4);
//        ReflectionTestUtils.setField(threadPoolManager, "keepAliveTime", 10L);
//        ReflectionTestUtils.setField(threadPoolManager, "queueCapacity", 5);
//        ReflectionTestUtils.setField(threadPoolManager, "threadNamePrefix", "test-thread");
//
//        // 初始化线程池
//        threadPoolManager.init();
//    }
//
//    @AfterEach
//    void tearDown() throws InterruptedException {
//        if (threadPoolManager != null) {
//            // 等待任务完成
//            Thread.sleep(1000);
//            threadPoolManager.shutdown();
//        }
//    }
//
//    @Test
//    void testThreadPoolInitialization() {
//        assertNotNull(threadPoolManager.getThreadPoolExecutor());
//        assertTrue(threadPoolManager.isRunning());
//
//        ThreadPoolManager.ThreadPoolStatus status = threadPoolManager.getThreadPoolStatus();
//        assertNotNull(status);
//        assertEquals(2, status.getCorePoolSize());
//        assertEquals(4, status.getMaxPoolSize());
//        assertEquals(0, status.getActiveCount());
//        assertFalse(status.isShutdown());
//    }
//
//    @Test
//    void testSubmitTask() throws Exception {
//        Future<String> future = threadPoolManager.submit(() -> {
//            Thread.sleep(100);
//            return "任务完成";
//        });
//
//        String result = future.get(2, TimeUnit.SECONDS);
//        assertEquals("任务完成", result);
//
//        ThreadPoolManager.ThreadPoolStatus status = threadPoolManager.getThreadPoolStatus();
//        assertTrue(status.getCompletedTaskCount() > 0);
//    }
//
//    @Test
//    void testExecuteTask() throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(1);
//
//        threadPoolManager.execute(() -> {
//            try {
//                Thread.sleep(100);
//                completedTasks.incrementAndGet();
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            } finally {
//                latch.countDown();
//            }
//        });
//
//        assertTrue(latch.await(2, TimeUnit.SECONDS));
//        assertEquals(1, completedTasks.get());
//    }
//
//    @Test
//    void testBatchTasks() throws Exception {
//        int taskCount = 10;
//        CountDownLatch latch = new CountDownLatch(taskCount);
//
//        for (int i = 0; i < taskCount; i++) {
//            final int taskId = i;
//            threadPoolManager.execute(() -> {
//                try {
//                    Thread.sleep(50);
//                    System.out.println("任务 " + taskId + " 执行完成");
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        assertTrue(latch.await(5, TimeUnit.SECONDS));
//
//        ThreadPoolManager.ThreadPoolStatus status = threadPoolManager.getThreadPoolStatus();
//        assertTrue(status.getCompletedTaskCount() >= taskCount);
//    }
//
//    @Test
//    void testThreadPoolStatus() {
//        ThreadPoolManager.ThreadPoolStatus status = threadPoolManager.getThreadPoolStatus();
//
//        // 验证状态信息
//        assertEquals(2, status.getCorePoolSize());
//        assertEquals(4, status.getMaxPoolSize());
//        assertEquals(5, status.getQueueCapacity());
//        assertEquals(0, status.getActiveCount());
//        assertEquals(0, status.getQueueSize());
//        assertFalse(status.isQueueFull());
//
//        // 验证队列使用率计算
//        double queueUsage = status.getQueueUsage();
//        assertTrue(queueUsage >= 0.0 && queueUsage <= 1.0);
//
//        // 验证Map转换
//        var statusMap = status.toMap();
//        assertNotNull(statusMap);
//        assertTrue(statusMap.containsKey("poolSize"));
//        assertTrue(statusMap.containsKey("queueUsage"));
//    }
//
//    @Test
//    void testQueueUsageCalculation() {
//        // 模拟队列使用率计算
//        ThreadPoolManager.ThreadPoolStatus status = new ThreadPoolManager.ThreadPoolStatus(
//                2, 2, 4, 0, 3, 0L, 0L, 0, 0, 5, 2
//        );
//
//        assertEquals(3, status.getQueueSize());
//        assertEquals(5, status.getQueueCapacity());
//        assertEquals(2, status.getQueueRemainingCapacity());
//
//        double queueUsage = status.getQueueUsage();
//        assertEquals(0.6, queueUsage, 0.01); // 3/(3+2) = 0.6
//
//        assertFalse(status.isQueueFull());
//
//        // 测试队列满的情况
//        ThreadPoolManager.ThreadPoolStatus fullQueueStatus = new ThreadPoolManager.ThreadPoolStatus(
//                2, 2, 4, 0, 5, 0L, 0L, 0, 0, 5, 0
//        );
//
//        assertTrue(fullQueueStatus.isQueueFull());
//        assertEquals(1.0, fullQueueStatus.getQueueUsage(), 0.01);
//    }
//
//    @Test
//    void testShutdownStatus() {
//        ThreadPoolManager.ThreadPoolStatus shutdownStatus =
//                ThreadPoolManager.ThreadPoolStatus.createShutdownStatus();
//
//        assertTrue(shutdownStatus.isShutdown());
//        assertEquals(0, shutdownStatus.getPoolSize());
//        assertEquals(0, shutdownStatus.getCorePoolSize());
//        assertEquals(0, shutdownStatus.getActiveCount());
//    }
//
//    @Test
//    void testRejectionPolicy() throws InterruptedException {
//        // 提交大量任务，触发拒绝策略
//        int overloadTasks = 20; // 超过队列容量+最大线程数
//
//        CountDownLatch latch = new CountDownLatch(overloadTasks);
//        AtomicInteger rejectedCount = new AtomicInteger(0);
//        AtomicInteger completedCount = new AtomicInteger(0);
//
//        for (int i = 0; i < overloadTasks; i++) {
//            try {
//                threadPoolManager.execute(() -> {
//                    try {
//                        Thread.sleep(1000); // 长时间任务
//                        completedCount.incrementAndGet();
//                    } catch (InterruptedException e) {
//                        Thread.currentThread().interrupt();
//                    } finally {
//                        latch.countDown();
//                    }
//                });
//            } catch (RejectedExecutionException e) {
//                rejectedCount.incrementAndGet();
//                latch.countDown();
//            }
//        }
//
//        // 等待所有任务处理完成（或拒绝）
//        latch.await(3, TimeUnit.SECONDS);
//
//        // 验证有任务被拒绝
//        assertTrue(rejectedCount.get() > 0 || completedCount.get() < overloadTasks);
//
//        ThreadPoolManager.ThreadPoolStatus status = threadPoolManager.getThreadPoolStatus();
//        System.out.println("最终状态: " + status);
//    }
//
//    @Test
//    void testThreadPoolMetrics() throws InterruptedException {
//        // 提交一些任务
//        for (int i = 0; i < 3; i++) {
//            threadPoolManager.execute(() -> {
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            });
//        }
//
//        // 等待任务开始执行
//        Thread.sleep(100);
//
//        ThreadPoolManager.ThreadPoolStatus status = threadPoolManager.getThreadPoolStatus();
//
//        // 验证监控指标
//        assertTrue(status.getActiveCount() >= 0);
//        assertTrue(status.getActiveCount() <= status.getMaxPoolSize());
//        assertTrue(status.getPoolSize() >= 0);
//        assertTrue(status.getPoolSize() <= status.getMaxPoolSize());
//        assertTrue(status.getQueueSize() >= 0);
//        assertTrue(status.getQueueSize() <= status.getQueueCapacity());
//
//        // 验证时间戳
//        assertTrue(status.getTimestamp() > 0);
//        assertTrue(System.currentTimeMillis() - status.getTimestamp() < 10000);
//    }
//}
