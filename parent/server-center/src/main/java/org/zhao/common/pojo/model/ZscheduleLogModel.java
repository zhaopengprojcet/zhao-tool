package org.zhao.common.pojo.model;

import java.util.Date;

import org.zhao.common.databean.annotation.DataBean;
import org.zhao.common.databean.annotation.DataColum;
import org.zhao.common.databean.em.FieldTypeEnum;
import org.zhao.common.util.RandomUtils;

@DataBean(tableName="R_SCHEDULE_LOG")
public class ZscheduleLogModel {

	@DataColum(type=FieldTypeEnum.STRING ,length=40,isKey=true,comment="编号")
	private String id;
	@DataColum(type=FieldTypeEnum.STRING ,length=300,comment="定时任务名称")
	private String scheduleName;
	@DataColum(type=FieldTypeEnum.STRING ,length=100,comment="定时任务调度客户端")
	private String putServer;
	@DataColum(type=FieldTypeEnum.DATETIME ,comment="调度发起时间")
	private Date putTime;
	@DataColum(type=FieldTypeEnum.STRING ,length=2 ,comment="调度结果")
	private String putState;
	@DataColum(type=FieldTypeEnum.STRING ,length=100 ,comment="调度错误信息")
	private String putError;
	@DataColum(type=FieldTypeEnum.DATETIME ,comment="执行结果反馈时间")
	private Date doEndTime;
	@DataColum(type=FieldTypeEnum.STRING ,length=2 ,comment="执行结果")
	private String doState;
	@DataColum(type=FieldTypeEnum.STRING ,length=100 ,comment="执行错误信息")
	private String doError;
	
	public ZscheduleLogModel(){}
	
	public ZscheduleLogModel(String scheduleName,String putServer,String putState){
		this.id = RandomUtils.getPrimaryKey();
		this.scheduleName = scheduleName;
		this.putServer = putServer;
		this.putState = putState;
		this.putTime = new Date();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScheduleName() {
		return scheduleName;
	}
	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}
	public String getPutServer() {
		return putServer;
	}
	public void setPutServer(String putServer) {
		this.putServer = putServer;
	}
	public Date getPutTime() {
		return putTime;
	}
	public void setPutTime(Date putTime) {
		this.putTime = putTime;
	}
	public String getPutState() {
		return putState;
	}
	public void setPutState(String putState) {
		this.putState = putState;
	}
	public Date getDoEndTime() {
		return doEndTime;
	}
	public void setDoEndTime(Date doEndTime) {
		this.doEndTime = doEndTime;
	}
	public String getDoState() {
		return doState;
	}
	public void setDoState(String doState) {
		this.doState = doState;
	}

	public String getPutError() {
		return putError;
	}

	public void setPutError(String putError) {
		this.putError = putError;
	}

	public String getDoError() {
		return doError;
	}

	public void setDoError(String doError) {
		this.doError = doError;
	}
	
	
}
