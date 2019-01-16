package org.zhao.common.zmq.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 服务器接收消息后转换的实体
 * @author zhao
 *
 */
public class MqContext implements Serializable{

	private String token; //消息来源
	private String context;//请求内容 
	private String service;//请求服务
	private Date sendTime;//请求时间
	private String pushType;//请求逻辑
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getPushType() {
		return pushType;
	}
	public void setPushType(String pushType) {
		this.pushType = pushType;
	}
	
	
}
