package org.zhao.common.zmq.queue.achieve;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.zhao.common.interceptor.PublicServerKV;
import org.zhao.common.util.SpringContextUtil;
import org.zhao.common.zmq.queue.Zqueue;

/**
 * redis缓存队列实现  
 * 
 * 键队列以实例化时传入的参数为key
 * 
 * 需要开启reids配置 
 * 确保 server.redis.open 参数为true
 * 并配置 redis-config.properties 
 *
 * @author zhao
 *
 * @param <E>
 */
public class FifoRedisQueue<E> implements Zqueue<E> {

	private RedisTemplate redisTemplate;

	private long cacheTime = 1800;//s
	private String key;
	
	public FifoRedisQueue(String key) {
		init(key);
	}
	
	public FifoRedisQueue(String key , long time) {
		init(key);
		//覆盖键值缓存过期时间
		this.cacheTime = time;
	}
	
	private void init(String key) {
		try {
			redisTemplate = (RedisTemplate) SpringContextUtil.getBean("redisTemplate", RedisTemplate.class);
			if(redisTemplate == null) throw new RuntimeException("--");
		} catch (Exception e) {
			throw new RuntimeException("-FifoRedisQueue 未加载到redisTemplate Bean");
		}
		if(StringUtils.isEmpty(key)) throw new RuntimeException("-FifoRedisQueue 键值不能为空");
		this.key = key;
		//如果正确设置了redis缓存过期时间，默认使用全局过期时间
		try {
			int h = PublicServerKV.getIntVal("server-center.redis.outTime");
			this.cacheTime = h * 60 * 60;
		} catch (Exception e) {
		}
	}
	
	@Override
	public E pop() {
		return (E) this.redisTemplate.opsForList().rightPop(key);
	}

	@Override
	public boolean flush(E model) {
		this.redisTemplate.opsForList().leftPush(key, model);
		this.redisTemplate.expire(key, cacheTime, TimeUnit.SECONDS);
		return true;
	}

	@Override
	public long size() {
		return this.redisTemplate.opsForList().size(key);
	}

	@Override
	public boolean clean() {
		this.redisTemplate.delete(key);
		return true;
	}

}
