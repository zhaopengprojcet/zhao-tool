package org.zhao.common.pojo.model;

import java.util.Date;

import org.zhao.common.databean.annotation.DataBean;
import org.zhao.common.databean.annotation.DataColum;
import org.zhao.common.databean.em.FieldTypeEnum;

/**
 * 客户端计算机运行异常记录
 * @author zhao
 *
 */
@DataBean(tableName="R_SERVER_EXP")
public class ZserverExpModel {

	@DataColum(type=FieldTypeEnum.STRING ,length=40,isKey=true,comment="编号")
	private String id;
	@DataColum(type=FieldTypeEnum.STRING ,length=30,comment="请求ip")
	private String queryIp;
	@DataColum(type=FieldTypeEnum.STRING ,length=30,comment="请求服务")
	private String queryServerName;
	@DataColum(type=FieldTypeEnum.STRING ,length=200,comment="异常内容")
	private String expDesc;
	@DataColum(type=FieldTypeEnum.DATETIME ,comment="记录时间")
	private Date createTime;
	@DataColum(type=FieldTypeEnum.STRING ,length=2,comment="异常处理状态0已记录")
	private String expState;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQueryIp() {
		return queryIp;
	}
	public void setQueryIp(String queryIp) {
		this.queryIp = queryIp;
	}
	public String getQueryServerName() {
		return queryServerName;
	}
	public void setQueryServerName(String queryServerName) {
		this.queryServerName = queryServerName;
	}
	public String getExpDesc() {
		return expDesc;
	}
	public void setExpDesc(String expDesc) {
		this.expDesc = expDesc;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getExpState() {
		return expState;
	}
	public void setExpState(String expState) {
		this.expState = expState;
	}
	
	
}
