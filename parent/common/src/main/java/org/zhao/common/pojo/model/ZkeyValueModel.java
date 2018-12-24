package org.zhao.common.pojo.model;

import org.zhao.common.databean.annotation.DataBean;
import org.zhao.common.databean.annotation.DataColum;
import org.zhao.common.databean.em.FieldTypeEnum;
import org.zhao.common.util.view.UpdateTypeEm;
import org.zhao.common.util.view.UpdateView;

/*
 * 保存系统一些kv值
 */
@DataBean(tableName="R_KEY_VALUE")
public class ZkeyValueModel {

	@UpdateView(check="",name="id",text="编号",key= true ,type=UpdateTypeEm.HIDDEN)
	@DataColum(type=FieldTypeEnum.STRING ,length=40,isKey=true,comment="编号")
	private String id;
	@UpdateView(check="{'required':true,'maxLength':100,'minLength':1}",name="serverKey",text="key键值",type=UpdateTypeEm.TEXT,updateType=UpdateTypeEm.ADD)
	@DataColum(type=FieldTypeEnum.STRING ,length=100,comment="key键值")
	private String serverKey;
	@UpdateView(check="{'required':true,'maxLength':2000,'minLength':1}",name="serverValue",text="value键值",type=UpdateTypeEm.TEXT)
	@DataColum(type=FieldTypeEnum.TEST ,length=2000,comment="value键值")
	private String serverValue;
	@UpdateView(check="{'required':true,'maxLength':100,'minLength':1}",name="servereyDesc",text="键值描述",type=UpdateTypeEm.TEXT)
	@DataColum(type=FieldTypeEnum.STRING ,length=200,comment="键值描述")
	private String servereyDesc;
	
	
	
	@Override
	public String toString() {
		return id+servereyDesc+serverKey+serverValue;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getServerKey() {
		return serverKey;
	}
	public void setServerKey(String serverKey) {
		this.serverKey = serverKey;
	}
	public String getServerValue() {
		return serverValue;
	}
	public void setServerValue(String serverValue) {
		this.serverValue = serverValue;
	}
	public String getServereyDesc() {
		return servereyDesc;
	}
	public void setServereyDesc(String servereyDesc) {
		this.servereyDesc = servereyDesc;
	}
	
	
}
