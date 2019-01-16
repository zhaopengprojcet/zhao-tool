package org.zhao.common.pojo.model;

import java.util.Date;

import org.zhao.common.databean.annotation.DataBean;
import org.zhao.common.databean.annotation.DataColum;
import org.zhao.common.databean.em.FieldTypeEnum;
import org.zhao.common.util.view.UpdateTypeEm;
import org.zhao.common.util.view.UpdateView;

@DataBean(tableName="R_SCHEDULE_REGIEST")
public class ZscheduleSetModel {

	@UpdateView(check="",name="id",text="编号",key= true ,type=UpdateTypeEm.HIDDEN)
	@DataColum(type=FieldTypeEnum.STRING ,length=40,isKey=true,comment="编号")
	private String id;
	@UpdateView(check="{'required':true}",name="scheduleType",text="任务类型",type=UpdateTypeEm.SELECT , loadValue=ServerCenterUpdateFinalModel.SCHEDULE_TYPE)
	@DataColum(type=FieldTypeEnum.STRING ,length=10,comment="定时任务类型  /FIXTIME 固定时间点  /FIXWEEK  固定星期x  /FIXDAY 固定日期（每月第几天） /EC  第一次运行后每隔x分钟执行一次    /FUSH 推送轮询无间隔")
	private String scheduleType;
	@UpdateView(check="{'required':true}",name="scheduleState",text="任务状态",type=UpdateTypeEm.SELECT , loadValue=UpdateFinalModel.CAN_USE)
	@DataColum(type=FieldTypeEnum.STRING ,length=2,comment="定时任务状态  1 正常 2 禁用")
	private String scheduleState;
	/**
	 * FIXTIME    10:00|11:00|....
	 * FIXWEEK    1-10:00|3-12:00...
	 * FIXDAY     12-10:00|17-12:00...
	 * EC         30
	 * FUSH       
	 */
	@UpdateView(check="{'required':true,'maxLength':200,'minLength':1}",name="scheduleCron",text="定时周期",type=UpdateTypeEm.TEXTARRAY)
	@DataColum(type=FieldTypeEnum.STRING ,length=300,comment="定时任务周期逻辑")
	private String scheduleCron;
	@DataColum(type=FieldTypeEnum.DATETIME ,comment="创建时间")
	private Date createTime;
	@UpdateView(check="{'required':true,'maxLength':50,'minLength':1}",name="scheduleKey",text="任务关键字",type=UpdateTypeEm.TEXT,updateType=UpdateTypeEm.ADD)
	@DataColum(type=FieldTypeEnum.STRING ,length=50,comment="定时任务关键字")
	private String scheduleKey;
	@UpdateView(check="{'required':true,'maxLength':200,'minLength':1}",name="scheduleName",text="任务描述",type=UpdateTypeEm.TEXTARRAY)
	@DataColum(type=FieldTypeEnum.STRING ,length=300,comment="定时任务描述")
	private String scheduleName;
	@UpdateView(check="{'required':true}",name="pushType",text="执行逻辑",type=UpdateTypeEm.SELECT , loadValue=ServerCenterUpdateFinalModel.SCHEDULE_PUT_TYPE)
	@DataColum(type=FieldTypeEnum.STRING ,length=10,comment="定时任务执行逻辑")
	private String pushType;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScheduleType() {
		return scheduleType;
	}
	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}
	public String getScheduleState() {
		return scheduleState;
	}
	public void setScheduleState(String scheduleState) {
		this.scheduleState = scheduleState;
	}
	public String getScheduleCron() {
		return scheduleCron;
	}
	public void setScheduleCron(String scheduleCron) {
		this.scheduleCron = scheduleCron;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getScheduleName() {
		return scheduleName;
	}
	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}
	public String getScheduleKey() {
		return scheduleKey;
	}
	public void setScheduleKey(String scheduleKey) {
		this.scheduleKey = scheduleKey;
	}
	public String getPushType() {
		return pushType;
	}
	public void setPushType(String pushType) {
		this.pushType = pushType;
	}
	
	
}
