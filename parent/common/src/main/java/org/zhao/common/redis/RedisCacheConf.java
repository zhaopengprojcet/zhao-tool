package org.zhao.common.redis;

import java.lang.reflect.Method;

import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
@ConditionalOnExpression("${server.redis.open}")
public class RedisCacheConf {

	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
     * 配置redis缓存管理对象
     * @return
     */
    @Bean(name = "cacheManager")
    public CacheManager cacheManager() {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        cacheManager.setDefaultExpiration(10000);//默认超时时间
        return cacheManager;
    }

    /**
     * 生成key的策略
     * 此方法将会根据类名+方法名+所有参数的值生成唯一的一个key,即使@Cacheable中的value属性一样，key也会不一样。
     * @return
     */
    @Bean(name="keyGenerator")
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                Cacheable ca = method.getAnnotation(Cacheable.class);
                if(ca != null && ca.value() != null && ca.value().length > 0) {
                	sb.append(ca.value()[0]);
                }
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                	if(obj instanceof Integer ||
                	   obj instanceof String)
                		sb.append(obj.toString());
                	else {
                		if(obj == null) sb.append("null");
                		else
                			sb.append("_"+obj.toString().hashCode());
                	}
                }
                return sb.toString();
            }
        };
    }
}
