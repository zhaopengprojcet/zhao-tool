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
@DataBean(tableName="R_MQ")
public class ZmessageQueueModel {

	@DataColum(type=FieldTypeEnum.STRING ,length=40,isKey=true,comment="编号")
	private String id;
	@DataColum(type=FieldTypeEnum.STRING ,length=30,comment="发送方ip")
	private String sendIp;
	@DataColum(type=FieldTypeEnum.STRING ,length=10,comment="发送方端口")
	private String sendPort;
	@DataColum(type=FieldTypeEnum.STRING ,length=30,comment="发送方服务名")
	private String sendServerName;
	@DataColum(type=FieldTypeEnum.DATETIME ,comment="消息发送时间")
	private Date sendTime;
	@DataColum(type=FieldTypeEnum.STRING ,length=30,comment="调用服务注册名称")
	private String putServiceName;
	@DataColum(type=FieldTypeEnum.STRING ,length=10,comment="消息推送逻辑")
	private String pushType;
	@DataColum(type=FieldTypeEnum.TEST ,length=30,comment="消息内容")
	private String context;
	@DataColum(type=FieldTypeEnum.STRING ,length=2,comment="消息状态-1发送失败  0发送队列中  1正常已发送  2未订阅")
	private String msgState;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSendIp() {
		return sendIp;
	}
	public void setSendIp(String sendIp) {
		this.sendIp = sendIp;
	}
	public String getSendPort() {
		return sendPort;
	}
	public void setSendPort(String sendPort) {
		this.sendPort = sendPort;
	}
	public String getSendServerName() {
		return sendServerName;
	}
	public void setSendServerName(String sendServerName) {
		this.sendServerName = sendServerName;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	
	public String getMsgState() {
		return msgState;
	}
	public void setMsgState(String msgState) {
		this.msgState = msgState;
	}
	public String getPutServiceName() {
		return putServiceName;
	}
	public void setPutServiceName(String putServiceName) {
		this.putServiceName = putServiceName;
	}
	public String getPushType() {
		return pushType;
	}
	public void setPushType(String pushType) {
		this.pushType = pushType;
	}
	
	
}
