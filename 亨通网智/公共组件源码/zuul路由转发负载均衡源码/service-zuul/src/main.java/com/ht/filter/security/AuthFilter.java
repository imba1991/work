package com.ht.filter.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ht.common.constant.ResponseConstant;
import com.ht.plugin.redis.service.impl.UserRedisService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * @author qiujunda
 * @date 2018年8月17日 
 * @description 
 * 用户是否登录，访问路径是否有权限过滤器
 */
@Component
public class AuthFilter extends ZuulFilter{
	private static Logger log = LoggerFactory.getLogger(AuthFilter.class);
	@Autowired
    private UserRedisService redisService;
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

	@Override
	public Object run() {
		boolean login=true;
		String errorLogMsg="";;
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.set("isOK", true);
        HttpServletRequest request = ctx.getRequest();
        String requestMethod=request.getMethod();
        String requestUrl=request.getRequestURL().toString();
        System.out.println(String.format("%s >>> %s", requestMethod, requestUrl));
        log.info(String.format("%s >>> %s", requestMethod, requestUrl));
        if(!requestMethod.equals("POST")&&!requestMethod.equals("GET")) {
        	ctx.set("isOK", false);
            return null;
        }
        if(requestUrl.indexOf("login")!=-1||requestUrl.indexOf("appLogin")!=-1||requestUrl.indexOf("tsmLogin")!=-1||requestUrl.indexOf("bindingAccessCode")!=-1) {
        	return null;
        }
        if(requestUrl.indexOf("api-a")==-1&&requestUrl.indexOf("api-b")==-1&&requestUrl.indexOf("api-e")==-1) {
        	return null;
        }
//        if(requestUrl.indexOf("js")!=-1||requestUrl.indexOf("swagger")!=-1) {
//        	return null;
//        }
//        if(requestUrl.indexOf("doc")!=-1||requestUrl.indexOf("api")!=-1) {
//        	return null;
//        }
        String ticket=null;
        if(StringUtils.isBlank(ticket)) {
      	  Cookie[] cookies = request.getCookies();//这样便可以获取一个cookie数组  
            if (null!=cookies) {  
                for(Cookie cookie : cookies){
                	if(cookie.getName().equals("ticket")) {
                		ticket=cookie.getValue();
                		break;
                	}
                }  
            } 
        }
        if(StringUtils.isBlank(ticket)) {
        	ticket = request.getParameter("ticket");
        }
        if(StringUtils.isBlank(ticket)) {
        	ticket=request.getHeader("ticket");
        }
        
        if(StringUtils.isBlank(ticket)) {
        	//如果票据为空
        	errorLogMsg=ResponseConstant.NO_LOGIN;
        	login=false;
        }else {
        	//票据不为空，判断redis缓存是否存在，不存在则返回票据超时
        	Object redisUserInfo= redisService.getUserByToken(ticket);
        	if(redisUserInfo==null) {
        		errorLogMsg=ResponseConstant.TICKET_TIME_OUT;
        		login=false;
        	}
        }
        if(!login) {
        	//未登录的filter转发
        	ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(200);
            try {
            	ctx.getResponse().setCharacterEncoding("UTF-8");
            	System.out.println(ticket+"--------"+errorLogMsg);
                ctx.setResponseBody(errorLogMsg);
                ctx.set("isOK", false);
            }catch (Exception e){
            	log.error(e.getMessage(),e);
            }
        }
        return null;
	}
}

