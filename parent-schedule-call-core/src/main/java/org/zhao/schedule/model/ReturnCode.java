package org.zhao.schedule.model;

import java.util.HashMap;
import java.util.Map;

import org.zhao.core.common.util.RegiestServer;

import net.sf.json.JSONObject;

/**
 * 定时任务返回结构
 * @author zhao
 *
 */
public class ReturnCode {

	/**
	 * 回馈调度任务 状态 
	 */
	public static String SUCCESS = "{\"code\":\"0\",\"message\":\"success\"}";
	
	public static String ERROR = "{\"code\":\"-1\",\"message\":\"error\"}";
	
	public static String error(String data) {
		return "{\"code\":\"-1\",\"message\":\"error\",\"data\":\""+data+"\"}";
	}
	
	/**
	 * 胡奎任务执行状态
	 */
	public static String result(String scheduleId ,String code) {
		JSONObject map = new JSONObject();
		map.put("token", RegiestServer.getToken(false));
		map.put("scheduleId", scheduleId);
		map.put("result", code);
		return map.toString();
	}
}
