package org.zhao.common.util.view;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * http 请求返回结构
 * @author zhao
 * @param <E>
 *
 */
public class ResultContent<T> implements Serializable{
	
	private static final long serialVersionUID = 6226503634546466417L;

	public static final String ERROR = "ERROR";
	
	public static final String SUCCESS = "SUCCESS";

	public static final String LOGIN_ERROR = "LOGINERROR";
	
	private String code;//返回码
	
	private String message; //消息
	
	private T data; //返回数据

	private int count;
	
	public ResultContent() {
	}

	
	
	public ResultContent(String code, String message) {
		this.code = code;
		this.message = message;
		this.data = null;
		this.count = 0;
	}



	public ResultContent(String code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public ResultContent(T data) {
		this.data = data;
		if(this.data == null) {
			this.message = "查询失败";
			this.code = ERROR;
		}
		else {
			this.message = "查询成功";
			this.code = SUCCESS;
		}
	}

	/** 方便查询时调用json格式数据 **/
	public String getJsonString(String key) {
		JSONObject json = JSONObject.fromObject(data);
		if(json.containsKey(key)) return json.getString(key);
		return null;
	}
	
	public Integer getJsonInt(String key) {
		JSONObject json = (JSONObject) data;
		if(json.containsKey(key)) return json.getInt(key);
		return null;
	}
	
	public List getJsoinArray(String key , Class c) {
		JSONObject json = (JSONObject) data;
		System.out.println(JSONArray.fromObject(json.getString(key)));
		if(json.containsKey(key)) return JSONArray.toList(JSONArray.fromObject(json.getString(key)), c);
		return null;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}



	public int getCount() {
		return count;
	}



	public void setCount(int count) {
		this.count = count;
	}
	
	
}
