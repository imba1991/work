package com.ht.common.response;

import java.util.Arrays;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class Result {

	/**
	 * 请求成功，成功返回结果
	 */
	public final static int SUCCESS=1001;
	/**
	 * 请求成功，返回失败的结果
	 */
	public final static int FAIL=5001;
	/**
	 * 请求失败，返回不确定的未知结果
	 */
	public final static int ERROR=4001;
	
	public static String successObj(Object object,String[] excludes) {
		JSONObject json=new JSONObject();
		json.element("code", SUCCESS);
		JsonConfig config=initJsonConfig(excludes);
        json.element("datas", JSONObject.fromObject(object,config));
        return json.toString();
	}
	public static String successArray(Object object,String[] excludes) {
		JSONObject json=new JSONObject();
		json.element("code", SUCCESS);
		JsonConfig config=initJsonConfig(excludes);
        json.element("datas", JSONArray.fromObject(object,config));
        return json.toString();
	}
	public static String success() {
		return "{'code':"+SUCCESS+"}";
	}
	
	public static String error(String msg) {
		return "{'code':"+FAIL+",'msg':'"+msg+"'}";
	}
	
	public static JsonConfig initJsonConfig(String[] excludes) {
		JsonConfig config=new JsonConfig ();
		String [] nomalExcludes= {"handler","hibernateLazyInitializer"};
		if(excludes!=null&&excludes.length>0) {
			nomalExcludes=mergeArray(nomalExcludes, excludes);
		}
		config.setExcludes(nomalExcludes); 
        config.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        return config;
	}
	
	public static String[] mergeArray(String[] str1,String[] str2) {
		int strLen1 = str1.length;// 保存第一个数组长度
        int strLen2 = str2.length;// 保存第二个数组长度
        str1 = Arrays.copyOf(str1, strLen1 + strLen2);// 扩容
        System.arraycopy(str2, 0, str1, strLen1, strLen2);// 将第二个数组与第一个数组合并
       return str1;
	}
}
