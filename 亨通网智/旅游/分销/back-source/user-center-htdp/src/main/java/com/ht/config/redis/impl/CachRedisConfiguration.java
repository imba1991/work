package com.ht.config.redis.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis缓存配置类
 * @author qiujunda
 *
 */
@Configuration
@EnableCaching
public class CachRedisConfiguration extends CachingConfigurerSupport{

    //缓存管理器
    @Bean 
    public CacheManager cacheManager(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        //设置缓存过期时间 
        cacheManager.setDefaultExpiration(10000);
        return cacheManager;
    }
    
    @Bean(name = "cachRedisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(
    		@Value("${spring.redis-user.host}") String hostName,
            @Value("${spring.redis-user.port}") int port, 
            @Value("${spring.redis-user.password}") String password,
            @Value("${spring.redis-user.testOnBorrow}") boolean testOnBorrow,
            @Value("${spring.redis-user.havePassword}") boolean havePassword,
            @Value("${spring.redis.pool.max-idle}") int maxIdle,
            @Value("${spring.redis.pool.max-active}") int maxTotal,
            @Value("${spring.redis.database}") int index,
            @Value("${spring.redis.pool.max-wait}") long maxWaitMillis) {
    	RedisTemplate<Object, Object> template = new RedisTemplate<Object,Object>();
    	template.setConnectionFactory(
        		connectionFactory(hostName, port, password, maxIdle, 
        				maxTotal, index, maxWaitMillis, testOnBorrow,havePassword));
    	template.setKeySerializer(new StringRedisSerializer());
        return template;
    }
    
    
    
    
	protected RedisConnectionFactory connectionFactory(String hostName, int port, String password, int maxIdle,
            int maxTotal, int index, long maxWaitMillis, boolean testOnBorrow,boolean havePassword) {
        JedisConnectionFactory jedis = new JedisConnectionFactory();
        jedis.setHostName(hostName);
        jedis.setPort(port);
        if (havePassword&&StringUtils.isNotEmpty(password)) {
            jedis.setPassword(password);
        }
        if (index != 0) {
            jedis.setDatabase(index);
        }
        jedis.setPoolConfig(poolCofig(maxIdle, maxTotal, maxWaitMillis, testOnBorrow));
        // 初始化连接pool
        jedis.afterPropertiesSet();
        RedisConnectionFactory factory = jedis;
        return factory;
    }

    protected JedisPoolConfig poolCofig(int maxIdle, int maxTotal, long maxWaitMillis, boolean testOnBorrow) {
        JedisPoolConfig poolCofig = new JedisPoolConfig();
        poolCofig.setMaxIdle(maxIdle);
        poolCofig.setMaxTotal(maxTotal);
        poolCofig.setMaxWaitMillis(maxWaitMillis);
        poolCofig.setTestOnBorrow(testOnBorrow);
        return poolCofig;
    }
}
