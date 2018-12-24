package org.zhao.common.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.zhao.common.properties.ConfigProperties;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@ConditionalOnExpression("${server.redis.open}")
public class RedisConfig {

	@Bean
    JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedis  = new JedisConnectionFactory();
		jedis.setDatabase(ConfigProperties.instance().getPropertiesInt("spring.redis.database"));
		jedis.setHostName(ConfigProperties.instance().getPropertiesVal("spring.redis.host"));
		jedis.setPort(ConfigProperties.instance().getPropertiesInt("spring.redis.port"));
		jedis.setUsePool(true);
		JedisPoolConfig pool = new JedisPoolConfig();
		pool.setMaxWaitMillis(ConfigProperties.instance().getPropertiesInt("spring.redis.pool.max-wait"));
		pool.setMaxIdle(ConfigProperties.instance().getPropertiesInt("spring.redis.pool.max-idle"));
		pool.setMaxTotal(ConfigProperties.instance().getPropertiesInt("spring.redis.pool.max-active"));
		pool.setMinIdle(ConfigProperties.instance().getPropertiesInt("spring.redis.pool.min-idle"));
		jedis.setPoolConfig(pool);
		jedis.setTimeout(ConfigProperties.instance().getPropertiesInt("spring.redis.timeout"));
        return jedis;
    }

    @Bean
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, T> template = new RedisTemplate<String, T>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
       	template.setValueSerializer(new RedisObjectSerializer<T>());
        return template;
    }
    
    
}
