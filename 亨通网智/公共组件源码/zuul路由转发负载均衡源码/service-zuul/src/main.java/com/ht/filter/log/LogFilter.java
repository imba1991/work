package com.ht.filter.log;

import com.ht.config.activemq.Producer;
import com.ht.plugin.redis.service.impl.UserRedisService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

/**
 * @Description:
 * @Auther: yq
 */
@Component
public class LogFilter extends ZuulFilter {
    private static final Logger log = LoggerFactory.getLogger(LogFilter.class);
    @Autowired
    private UserRedisService userRedisService;
    @Autowired
    private Producer producer;
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext().getBoolean("isOK");
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
//        HttpServletResponse response=ctx.getResponse();
//        String requestMethod=request.getMethod();
        log.info(String.format("HT Request from %s TO %s",request.getHeader("viewPath") ,request.getRequestURI()));
        System.out.println(request.getRequestURI());
        String url = request.getRequestURI();
        if(url.indexOf("api-a")==-1&&url.indexOf("api-b")==-1&&url.indexOf("api-e")==-1) {
        	return null;
        }
        System.out.println("1-----------"+url);
//        int i=url.lastIndexOf("/",url.lastIndexOf("/")-1) + 1;
//        url = url.substring(i);
//        System.out.println("2-----------"+url);
        String actionStr = (String) userRedisService.getActionByUrl(url);
        JSONObject action = JSONObject.fromObject(actionStr);
        if (StringUtils.isBlank(actionStr)) return null;
        JSONObject logInfo = new JSONObject();
        logInfo.put("id",UUID.randomUUID().toString().replace("-",""));
        String result = "";
        String reqBody ="";
        String ticket = getTicket();
        
        try {
            InputStream  in = ctx.getResponseDataStream();
            reqBody = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
            JSONObject json = JSONObject.fromObject(reqBody);
            result = json.getString("code");
            if(url.indexOf("login")!=-1) {
            	ticket=json.getString("data");
            }
        } catch (Exception e){
//            e.printStackTrace();
        	result="1005";
        }
        log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
        
        JSONObject user = (JSONObject) userRedisService.getUserByToken(ticket);
        if (user!=null){
            logInfo.put("userId",user.get("userId"));
            logInfo.put("userName",user.get("userName"));
            logInfo.put("scenicId",user.get("scenicId"));
            logInfo.put("companyId",user.get("companyId"));
        }
        logInfo.put("ip",getIpAddr(request));
        if ("1001".equals(result)){
            logInfo.put("result",1);
        } else {
            logInfo.put("result",0);
        }
        logInfo.put("operationName",action.get("actionName"));
        String centerId = action.getString("sysFlag");
        logInfo.put("centerId",centerId);
        logInfo.put("time",new Date());
        logInfo.put("level",action.get("actionType"));
        producer.send("log.queue",logInfo);
        ctx.setResponseBody(reqBody);
        return null;
    }


    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if(ip.equals("127.0.0.1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (inet != null) {
                    ip= inet.getHostAddress();
                }
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ip != null && ip.length() > 15){
            if(ip.indexOf(",")>0){
                ip = ip.substring(0,ip.indexOf(","));
            }
        }
        return ip;
    }

    public String getTicket() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest servletRequest = servletRequestAttributes.getRequest();
        String ticket=null;
        if (StringUtils.isBlank(ticket)) {
            Cookie[] cookies = servletRequest.getCookies();// 这样便可以获取一个cookie数组
            if (null != cookies) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("ticket")) {
                        ticket = cookie.getValue();
                        break;
                    }
                }
            }
        }
        if (StringUtils.isBlank(ticket)) {
            ticket = servletRequest.getParameter("ticket");
        }
        if (StringUtils.isBlank(ticket)) {
            ticket = servletRequest.getHeader("ticket");
        }

        return ticket;
    }

}
