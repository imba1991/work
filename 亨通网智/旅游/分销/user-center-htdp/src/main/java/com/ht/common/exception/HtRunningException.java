package com.ht.common.exception;

import com.ht.common.response.Result;

/**
 * @author qiujunda
 * @date 2019年1月3日
 * @description
 */
public class HtRunningException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1900L;
	private int code;
	private String msg;
	
	public HtRunningException(String msg) {
		this.code=Result.FAIL;
		this.msg=msg;
	}
	
	public HtRunningException(int code,String msg) {
		this.code=code;
		this.msg=msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
