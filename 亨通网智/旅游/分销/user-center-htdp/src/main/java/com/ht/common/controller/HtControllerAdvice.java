package com.ht.common.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ht.common.exception.HtRunningException;
import com.ht.common.response.Result;

/**
 * @author qiujunda
 * @date 2019年1月4日
 * @description controller异常处理类
 */
@RestControllerAdvice
public class HtControllerAdvice {

	private final Logger logger=LoggerFactory.getLogger(this.getClass());
	@ResponseBody
	@ExceptionHandler(value=HtRunningException.class)
	public Map errorHandler(HtRunningException ex) {
		Map map=new HashMap();
		map.put("code", ex.getCode());
		map.put("msg", ex.getMsg());
		logger.error(ex.getMessage());
		return map;
	}
	
	@ResponseBody
	@ExceptionHandler(value=Exception.class)
	public Map errorHandler(Exception ex) {
		Map map=new HashMap();
		map.put("code", Result.ERROR);
		map.put("msg", ex.getMessage());
		logger.error(ex.getMessage(),ex);
		return map;
	}
}
