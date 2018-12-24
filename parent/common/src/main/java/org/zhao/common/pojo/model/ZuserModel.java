package org.zhao.common.pojo.model;

import java.io.Serializable;
import java.util.Date;

import org.zhao.common.databean.annotation.DataBean;
import org.zhao.common.databean.annotation.DataColum;
import org.zhao.common.databean.em.FieldTypeEnum;
import org.zhao.common.util.view.UpdateTypeEm;
import org.zhao.common.util.view.UpdateView;

/**
 * 系统用户表
 * @author zhao
 *
 */
@DataBean(tableName="R_USER")
public class ZuserModel  implements Serializable{

	@UpdateView(check="",name="id",text="编号",key= true ,type=UpdateTypeEm.HIDDEN)
	@DataColum(type=FieldTypeEnum.STRING ,length=40,isKey=true,comment="编号")
	private String id; 
	@UpdateView(check="{'required':true,'maxLength':10,'minLength':1}",name="loginName",text="登录名",type=UpdateTypeEm.TEXT,updateType=UpdateTypeEm.ADD)
	@DataColum(type=FieldTypeEnum.STRING ,length=20,comment="登录名")
	private String loginName;
	@UpdateView(check="{'required':true,'maxLength':10,'minLength':1}",name="password",text="密码",type=UpdateTypeEm.PASSWORD,updateType=UpdateTypeEm.ADD)
	@DataColum(type=FieldTypeEnum.STRING ,length=40,comment="密码MD5")
	private String password;
	@UpdateView(check="{'required':true}",name="roleId",text="角色名称",type=UpdateTypeEm.SELECT,loadValueUrl="/role/comboList.html",textField="roleName")
	@DataColum(type=FieldTypeEnum.STRING ,length=40,comment="角色编号")
	private String roleId;
	@DataColum(type=FieldTypeEnum.STRING ,length=20,comment="角色名称")
	private String roleName;
	@DataColum(type=FieldTypeEnum.DATETIME ,comment="创建时间")
	private Date createTime;
	@DataColum(type=FieldTypeEnum.DATETIME ,comment="最后登录时间")
	private Date lastLoginTime;
	@UpdateView(check="",name="userState",text="帐号状态",type=UpdateTypeEm.SELECT , loadValue=UpdateFinalModel.CAN_USE)
	@DataColum(type=FieldTypeEnum.STRING ,length=2,comment="帐号状态1正常2禁用3需要修改密码")
	private String userState;
	@DataColum(type=FieldTypeEnum.STRING ,length=40,comment="附加信息编号")
	private String infoId;
	@DataColum(type=FieldTypeEnum.INT,comment="当日登录失败次数")
	private int loginErrorCount;
	
	
	
	@Override
	public String toString() {
		return id+infoId+loginErrorCount+loginName+password+roleId+roleName+userState;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getUserState() {
		return userState;
	}
	public void setUserState(String userState) {
		this.userState = userState;
	}
	public String getInfoId() {
		return infoId;
	}
	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}
	public int getLoginErrorCount() {
		return loginErrorCount;
	}
	public void setLoginErrorCount(int loginErrorCount) {
		this.loginErrorCount = loginErrorCount;
	}
	
	
}
