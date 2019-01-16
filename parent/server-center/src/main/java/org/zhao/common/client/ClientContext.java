package org.zhao.common.client;

import java.io.Serializable;
import java.util.Date;

/**
 * 注册服务时客户端参数实体
 * @author zhao
 *
 */
public class ClientContext implements Serializable{

	private String ip;
	private String port;
	private String serviceName; //注册客户端名
	private String loginName;
	private String password;
	private boolean fullServer = true; //转发到其他注册中心,默认为true ，注册中心转发注册消息时为false
	private String token = "";//当实体为主机转发到其他主机时，拥有token，则其他主机不再生成token
	private Date regiestTime;//注册事件，定时器定时清除超过指定时间的token
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isFullServer() {
		return fullServer;
	}
	public void setFullServer(boolean fullServer) {
		this.fullServer = fullServer;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getRegiestTime() {
		return regiestTime;
	}
	public void setRegiestTime(Date regiestTime) {
		this.regiestTime = regiestTime;
	}
	
}
