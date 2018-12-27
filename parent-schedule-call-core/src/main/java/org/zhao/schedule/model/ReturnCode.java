package org.zhao.schedule.model;

/**
 * 定时任务返回结构
 * @author zhao
 *
 */
public class ReturnCode {

	public static String SUCCESS = "{\"code\":\"0\",\"message\":\"success\"}";
	
	public static String ERROR = "{\"code\":\"-1\",\"message\":\"error\"}";
	
	public static String error(String data) {
		return "{\"code\":\"-1\",\"message\":\"error\",\"data\":\""+data+"\"}";
	}
}
