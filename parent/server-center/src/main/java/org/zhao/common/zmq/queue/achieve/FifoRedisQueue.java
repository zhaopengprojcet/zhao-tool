package org.zhao.common.zmq.queue.achieve;

import org.springframework.data.redis.core.RedisTemplate;
import org.zhao.common.util.SpringContextUtil;
import org.zhao.common.zmq.queue.Zqueue;

public class FifoRedisQueue<E> implements Zqueue<E> {

	private RedisTemplate redisTemplate;
	
	private String key = "zhao_fifo_redisqueue";
	
	public FifoRedisQueue() {
		try {
			redisTemplate = (RedisTemplate) SpringContextUtil.getBean("redisTemplate", RedisTemplate.class);
			if(redisTemplate == null) throw new RuntimeException("--");
		} catch (Exception e) {
			throw new RuntimeException("-FifoRedisQueue 未加载到redisTemplate Bean");
		}
	}
	
	@Override
	public E pop() {
		return (E) this.redisTemplate.opsForList().rightPop(key);
	}

	@Override
	public boolean flush(E model) {
		this.redisTemplate.opsForList().leftPush(key, model);
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
