package com.util.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description 线程池工具类
 * @Author jie.zhao
 * @Date 2019/8/21 9:26
 */
public class ThreadPoolUtil {

    /**
     * 核心线程数，会一直存活，即使没有任务，线程池也会维护线程的最少数量
     */
    private static final int SIZE_CORE_POOL = 5;
    /**
     * 线程池维护线程的最大数量
     */
    private static final int SIZE_MAX_POOL = 10;
    /**
     * 线程池维护线程所允许的空闲时间
     */
    private static final long ALIVE_TIME = 2000;
    /**
     * 线程缓冲队列
     */
    private static BlockingQueue<Runnable> bqueue = new ArrayBlockingQueue<Runnable>(100);
    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(SIZE_CORE_POOL, SIZE_MAX_POOL, ALIVE_TIME, TimeUnit.MILLISECONDS, bqueue, new ThreadPoolExecutor.CallerRunsPolicy());

    static {
        pool.prestartAllCoreThreads();
    }

    public static ThreadPoolExecutor getPool() {
        return pool;
    }
    
    public static void main(String[] args) {
    	/**
    	 * 线程池的使用
    	 */
    	//产生死锁的情况   
    	for(int i=0;i<10;i++) {
 
    		ThreadPoolUtil.getPool().execute(new Runnable() {
        		@Override
        		public void run() {
        			
        			while(true){
        				try {
        					System.out.println("线程池的使用   "+Thread.currentThread());
        					
    						Thread.sleep(1000);
    					} catch (InterruptedException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
        				
        			}
        			
        		}
        	});	
    	}    	
	}
}
