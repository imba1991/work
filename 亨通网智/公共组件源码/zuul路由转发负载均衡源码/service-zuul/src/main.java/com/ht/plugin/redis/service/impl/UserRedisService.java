package com.ht.plugin.redis.service.impl;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ht.plugin.redis.service.RedisService;

/**
 * @author qiujunda
 * @date 2018年8月17日 
 * @description 
 */
@Service
public class UserRedisService extends RedisService{
    @Resource(name = "userRedisTemplate")
    private RedisTemplate<Object, Object> temple;
    @Value("${spring.redis-user.timeout}")
    private long timeout;
    public RedisTemplate<Object, Object> getTemple() {
        return temple;
    }
    public Object getUserByToken(String token){
    	Object userAccount=this.getObj(token);
    	if(userAccount!=null){
    		temple.expire(token, timeout, TimeUnit.SECONDS);
    		return userAccount;
    	}else
    		return null;
    }
    public Object getActionByUrl(String url){
        Object actionInfo=this.getObj(url);
        if(actionInfo!=null){
            return actionInfo;
        }
        return null;
    }
}
