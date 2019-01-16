package org.zhao.common.zmq.queue;

import java.util.HashMap;
import java.util.Map;

import org.zhao.common.zmq.queue.achieve.FifoQueue;
import org.zhao.common.zmq.queue.achieve.FifoRedisQueue;

/**
 * 队列实体创建
 * 
 * 创建时同函数传入同key时会使用同一个缓存
 * @author zhao
 *
 */
public class ZQueueConfig {

	private static Map<String, Zqueue> queues = new HashMap<String, Zqueue>();
	private static Map<String, Zqueue> redisQueues = new HashMap<String, Zqueue>();
	
	/**
	 * 单服务队列使用
	 * @param key
	 * @return
	 */
	public static Zqueue createQueue(String key) {
		synchronized (queues) {
			if(queues.containsKey(key)) return queues.get(key);
			System.out.println("new-------");
			FifoQueue fifo = new FifoQueue();
			queues.put(key, fifo);
		}
		return queues.get(key);
	}
	
	/**
	 * 多服务集群队列时使用
	 * @param key
	 * @return
	 */
	public static Zqueue createRedisQueue(String key) {
		synchronized (redisQueues) {
			if(redisQueues.containsKey(key)) return redisQueues.get(key);
			FifoRedisQueue fifo = new FifoRedisQueue(key);
			redisQueues.put(key, fifo);
		}
		return redisQueues.get(key);
	}
	
	/**
	 * 多服务集群队列时使用
	 * @param key
	 * @return
	 */
	public static Zqueue createRedisQueue(String key , long outTime) {
		synchronized (redisQueues) {
			if(redisQueues.containsKey(key)) return redisQueues.get(key);
			FifoRedisQueue fifo = new FifoRedisQueue(key , outTime);
			redisQueues.put(key, fifo);
		}
		return redisQueues.get(key);
	}
}
