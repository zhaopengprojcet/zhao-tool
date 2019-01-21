package org.zhao.common.pojo.model;

import java.util.Date;

import org.zhao.common.databean.annotation.DataBean;
import org.zhao.common.databean.annotation.DataColum;
import org.zhao.common.databean.em.FieldTypeEnum;
import org.zhao.common.util.view.UpdateTypeEm;
import org.zhao.common.util.view.UpdateView;

/**
 * 客户端注册服务账号明细
 * @author zhao
 *
 */
@DataBean(tableName="R_CLIENT_KEY")
public class ZregiestClientModel {

	@UpdateView(check="",name="id",text="编号",key= true ,type=UpdateTypeEm.HIDDEN)
	@DataColum(type=FieldTypeEnum.STRING ,length=40,isKey=true,comment="编号")
	private String id;
	@UpdateView(check="{'required':true,'maxLength':70,'minLength':1}",name="name",text="账号",type=UpdateTypeEm.TEXT ,updateType=UpdateTypeEm.ADD)
	@DataColum(type=FieldTypeEnum.STRING ,length=150,comment="账号")
	private String name;
	@UpdateView(check="{'required':true,'maxLength':30,'minLength':1}",name="pass",text="密码",type=UpdateTypeEm.TEXT)
	@DataColum(type=FieldTypeEnum.STRING ,length=200,comment="密码")
	private String pass;
	@UpdateView(check="{'required':false,'maxLength':100}",name="clientDesc",text="简介",type=UpdateTypeEm.TEXTARRAY)
	@DataColum(type=FieldTypeEnum.STRING ,length=200,comment="简介")
	private String clientDesc;
	@DataColum(type=FieldTypeEnum.DATETIME,comment="创建时间")
	private Date createTime;
	@UpdateView(check="{'required':true}",name="state",text="状态",type=UpdateTypeEm.SELECT , loadValue=UpdateFinalModel.CAN_USE)
	@DataColum(type=FieldTypeEnum.STRING ,length=2,comment="状态")
	private String state;
	@UpdateView(check="{'required':true}",name="useTime",text="有效截止",type=UpdateTypeEm.DATETIME)
	@DataColum(type=FieldTypeEnum.DATETIME ,comment="有效截止时间")
	private Date useTime;
	@UpdateView(check="{'required':false,'maxLength':200}",name="useIps",text="IP白名单",type=UpdateTypeEm.TEXTARRAY)
	@DataColum(type=FieldTypeEnum.STRING ,length=300,comment="白名单")
	private String useIps;
	@UpdateView(check="{'required':false,'maxLength':100}",name="powers",text="服务",type=UpdateTypeEm.TEXT)
	@DataColum(type=FieldTypeEnum.STRING ,length=300,comment="服务")
	private String powers;
	
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
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getClientDesc() {
		return clientDesc;
	}
	public void setClientDesc(String clientDesc) {
		this.clientDesc = clientDesc;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Date getUseTime() {
		return useTime;
	}
	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}
	public String getUseIps() {
		return useIps;
	}
	public void setUseIps(String useIps) {
		this.useIps = useIps;
	}
	public String getPowers() {
		return powers;
	}
	public void setPowers(String powers) {
		this.powers = powers;
	}
	
	
}
