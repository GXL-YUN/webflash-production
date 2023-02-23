package com.thread.function;

import edu.emory.mathcs.backport.java.util.concurrent.ExecutorService;
import edu.emory.mathcs.backport.java.util.concurrent.Executors;

public class OneThread {
	
	/**
	 * 多线程开发
	 *  什么是线程
		线程就是进程当中的一条执行路径，线程之间共享一个内存空间， 线程之间可以自由的切换， 并发执行。一个进程中可以多个线程，但是最少得有一个线程，
		
		例子： 在电子厂流水线中， 一条流水线就表示一个进程， 而流水线上的人 就是线程， 也就是真正来组装产品的人， 在映射过来的话就是 线程就是真正来执行代码的。
		
		3) 并行与并发
		并行： 当两个cpu核心运行两个任务得时候， 那么这个两个任务是同时运行，那么就是并行。（cpu核心数大于或等于运行任务数）
		
		并发： 是指两个任务同时请求运行， 而处理器只有一个核心只能接收一个任务， 就会把两个任务轮流运行， 由于
		
		处理器时间片(CPU运行该程序的时间)运行时间较短， 就感觉两个程序同时运行。
		
		
	  两种方式创建线程
	    1.继承类  Thread
	    2.实现接口  Runnable 
	 */
	
	
	class InClassThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			int i=0;
			while (true) {
				i++;
				System.out.println(Thread.currentThread().getName() + ":" + i);
				
			}
		}
	}
	/**
	 * 三种 同步代码快 同步方法 locak 锁
	 */
	public synchronized static void syncFun() {
		while (true) {

			System.out.println(
					"线程开启线程名称" + Thread.currentThread());
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 线程池 创建

	ExecutorService threadData = Executors.newCachedThreadPool();

	public static void main(String[] args) {

		// 单线程数据
		Runnable thread = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				syncFun();
			}
		};
		
		try {
			thread.run();
			thread.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		OneThread t1 = new OneThread();
		// 运行线程(先是就绪状态， 最终是JVM开启线程)
		InClassThread inClassThread= t1.new InClassThread();
		System.out.println("线程启动");
		inClassThread.start();
	}

}
