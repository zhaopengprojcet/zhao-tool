package org.zhao.common.pojo.model;

import java.util.Date;

import org.zhao.common.databean.annotation.DataBean;
import org.zhao.common.databean.annotation.DataColum;
import org.zhao.common.databean.em.FieldTypeEnum;
import org.zhao.common.util.view.UpdateTypeEm;
import org.zhao.common.util.view.UpdateView;

/**
 * ip黑白名单
 * @author zhao
 *
 */
@DataBean(tableName="R_IP_BW")
public class ZwhiteBlackIpList {

	@UpdateView(check="",name="id",text="编号",key= true ,type=UpdateTypeEm.HIDDEN)
	@DataColum(type=FieldTypeEnum.STRING ,length=40,isKey=true,comment="编号")
	private String id;
	@UpdateView(check="{'required':true,'maxLength':15,'minLength':7}",name="ip",text="ip地址",type=UpdateTypeEm.TEXT)
	@DataColum(type=FieldTypeEnum.STRING ,length=20,comment="ip地址")
	private String ip;
	@UpdateView(check="{'required':true}",name="bwType",text="名单类型",type=UpdateTypeEm.SELECT , loadValue=UpdateFinalModel.WHITE_BLACK)
	@DataColum(type=FieldTypeEnum.STRING ,length=2,comment="名单类型1白2黑")
	private String bwType;
	@DataColum(type=FieldTypeEnum.DATETIME ,comment="创建时间")
	private Date createTime;
	@UpdateView(check="{'maxLength':200}",name="addDesc",text="添加备注",type=UpdateTypeEm.TEXTARRAY)
	@DataColum(type=FieldTypeEnum.STRING ,length=400,comment="添加备注")
	private String addDesc;

	
	
	@Override
	public String toString() {
		return addDesc+bwType+id+ip;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getBwType() {
		return bwType;
	}

	public void setBwType(String bwType) {
		this.bwType = bwType;
	}
	

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAddDesc() {
		return addDesc;
	}

	public void setAddDesc(String addDesc) {
		this.addDesc = addDesc;
	}
	
	
}
