package org.zhao.common.pojo.model;

import org.zhao.common.databean.annotation.DataBean;
import org.zhao.common.databean.annotation.DataColum;
import org.zhao.common.databean.em.FieldTypeEnum;

@DataBean(tableName="R_REQUEST_USE")
public class ZrequestUseModel {

	@DataColum(type=FieldTypeEnum.STRING ,length=40,isKey=true,comment="编号")
	private String id;
	@DataColum(type=FieldTypeEnum.STRING ,length=100,comment="请求目标")
	private String name;
	@DataColum(type=FieldTypeEnum.STRING ,length=30,comment="请求ip")
	private String queryIp;
	@DataColum(type=FieldTypeEnum.STRING ,length=30,comment="请求服务")
	private String serviceName;
	@DataColum(type=FieldTypeEnum.DOUBLE ,length=100,comment="平均耗时")
	private double times;
	@DataColum(type=FieldTypeEnum.INT ,length=100,comment="访问次数")
	private int count;
	@DataColum(type=FieldTypeEnum.STRING ,length=100,comment="记录时间")
	private String requestTime;
	
	
	@Override
	public String toString() {
		return id+count+name+requestTime+times;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getTimes() {
		return times;
	}
	public void setTimes(double times) {
		this.times = times;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	public String getQueryIp() {
		return queryIp;
	}
	public void setQueryIp(String queryIp) {
		this.queryIp = queryIp;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	
}
