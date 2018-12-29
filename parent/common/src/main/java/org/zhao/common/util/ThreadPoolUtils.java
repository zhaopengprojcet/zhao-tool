package org.zhao.common.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.zhao.common.exceptionListen.ThreadExectionHandler;
import org.zhao.common.interceptor.PublicServerKV;

/**
 * 线程池工具类
 * @author zhao
 *
 */
public class ThreadPoolUtils {

	private static Logger logger = Logger.getLogger(ThreadPoolUtils.class);
	private static ThreadPoolExecutor cachedThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	
	static{
		cachedThreadPool.setKeepAliveTime(5, TimeUnit.SECONDS); //默认线程空闲时间为5s
	}
	
	public static synchronized void putThread(String name ,Thread t) {
		t.setUncaughtExceptionHandler(new ThreadExectionHandler());
		while(cachedThreadPool.getPoolSize() > PublicServerKV.getIntVal("common.thread.pool.maxsize")) {}
		t.setName(name);
		cachedThreadPool.execute(t);
		logger.info("新线程任务【"+name+"】加入执行,当前任务数【"+(cachedThreadPool.getActiveCount())+"】,池内总线程数【"+cachedThreadPool.getPoolSize()+"】");
	}
}
