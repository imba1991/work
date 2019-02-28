package com.ht.config.redis.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.ht.config.redis.RedisConfiguration;

@Configuration
@EnableRedisHttpSession
public class UserRedisConfiguration extends RedisConfiguration{

    @Bean(name = "userRedisTemplate")
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

}
