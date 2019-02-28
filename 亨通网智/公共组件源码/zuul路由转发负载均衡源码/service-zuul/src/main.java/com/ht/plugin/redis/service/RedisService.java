package com.ht.plugin.redis.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
/**
 * @author qiujunda
 * @date 2018年8月17日 
 * @description 
 */
public abstract class RedisService {
	protected RedisTemplate<Object, Object> temple;

	public void setObj(Object key, Object value, long timeout) {
		getTemple().opsForValue().set(key, value);
		getTemple().expire(key, timeout, TimeUnit.SECONDS);
	}

	public void setObj(Object key, Object value) {
		getTemple().opsForValue().set(key, value);
	}

	public Object getObj(Object key) {
		return getTemple().opsForValue().get(key);
	}

	public void delObj(Object key) {
		getTemple().delete(key);
	}

	public RedisTemplate<Object, Object> getTemple() {
		return temple;
	}

}
