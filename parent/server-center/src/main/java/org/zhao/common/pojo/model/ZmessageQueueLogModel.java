package org.zhao.common.pojo.model;

import java.util.Date;

import org.zhao.common.databean.annotation.DataBean;
import org.zhao.common.databean.annotation.DataColum;
import org.zhao.common.databean.em.FieldTypeEnum;

/**
 * 消息队列 对象实体
 * @author zhao
 *
 */
@DataBean(tableName="R_MQ_LOG")
public class ZmessageQueueLogModel {

	@DataColum(type=FieldTypeEnum.STRING ,length=40,isKey=true,comment="编号")
	private String id;
	@DataColum(type=FieldTypeEnum.STRING ,length=40,comment="消息编号")
	private String messageId;
	@DataColum(type=FieldTypeEnum.STRING ,length=30,comment="接收方ip")
	private String putClientIp;
	@DataColum(type=FieldTypeEnum.STRING ,length=10,comment="接收方端口号")
	private String putClientPort;
	@DataColum(type=FieldTypeEnum.STRING ,length=30,comment="接收方服务名")
	private String putClientName;
	@DataColum(type=FieldTypeEnum.DATETIME ,comment="推送时间")
	private Date lastPuTime;
	@DataColum(type=FieldTypeEnum.STRING ,length=300,comment="推送返回消息")
	private String lastReturnContext;
	@DataColum(type=FieldTypeEnum.STRING ,length=300,comment="推送接口执行回馈")
	private String mqReturnContext;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getPutClientIp() {
		return putClientIp;
	}
	public void setPutClientIp(String putClientIp) {
		this.putClientIp = putClientIp;
	}
	public String getPutClientPort() {
		return putClientPort;
	}
	public void setPutClientPort(String putClientPort) {
		this.putClientPort = putClientPort;
	}
	public String getPutClientName() {
		return putClientName;
	}
	public void setPutClientName(String putClientName) {
		this.putClientName = putClientName;
	}
	public Date getLastPuTime() {
		return lastPuTime;
	}
	public void setLastPuTime(Date lastPuTime) {
		this.lastPuTime = lastPuTime;
	}
	public String getLastReturnContext() {
		return lastReturnContext;
	}
	public void setLastReturnContext(String lastReturnContext) {
		this.lastReturnContext = lastReturnContext;
	}
	public String getMqReturnContext() {
		return mqReturnContext;
	}
	public void setMqReturnContext(String mqReturnContext) {
		this.mqReturnContext = mqReturnContext;
	}
	
	
}
