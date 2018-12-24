package org.zhao.common.pojo.model;

import java.io.Serializable;
import java.util.Date;

import org.zhao.common.databean.annotation.DataBean;
import org.zhao.common.databean.annotation.DataColum;
import org.zhao.common.databean.em.FieldTypeEnum;
import org.zhao.common.util.view.UpdateTypeEm;
import org.zhao.common.util.view.UpdateView;

@DataBean(tableName="R_LOG")
public class ZLogModel implements Serializable{
	
	@DataColum(isKey=true,length=40,type=FieldTypeEnum.STRING,comment="编号")
    private String id;
	@DataColum(length=20,type=FieldTypeEnum.STRING,comment="日志标记")
	private String logKey;
	@DataColum(length=20,type=FieldTypeEnum.STRING,comment="日志类型")
	private String logType;
	@DataColum(length=300,type=FieldTypeEnum.STRING,comment="日志内容")
	private String logDesc;
	@DataColum(type=FieldTypeEnum.DATETIME,comment="日志时间")
	private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

	public String getLogKey() {
		return logKey;
	}

	public void setLogKey(String logKey) {
		this.logKey = logKey;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getLogDesc() {
		return logDesc;
	}

	public void setLogDesc(String logDesc) {
		this.logDesc = logDesc;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}