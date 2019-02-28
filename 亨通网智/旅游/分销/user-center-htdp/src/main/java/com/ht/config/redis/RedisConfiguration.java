package com.ht.config.redis;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.JedisPoolConfig;

public abstract class RedisConfiguration {
	public abstract RedisTemplate<Object, Object> redisTemplate(String hostName,
            int port, String password,
            boolean testOnBorrow,boolean havePassword,
            int maxIdle, int maxTotal,
            int index,  long maxWaitMillis);
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
