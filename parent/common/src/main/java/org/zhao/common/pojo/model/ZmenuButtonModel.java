package org.zhao.common.pojo.model;

import java.io.Serializable;

import org.zhao.common.databean.annotation.DataBean;
import org.zhao.common.databean.annotation.DataColum;
import org.zhao.common.databean.em.FieldTypeEnum;

/**
 * 菜单按钮
 * @author zhao
 *
 */
@DataBean(tableName="R_MENU_BUTTON")
public class ZmenuButtonModel implements Serializable{

	@DataColum(isKey=true,length=40,type=FieldTypeEnum.STRING,comment="编号")
	private String id;
	@DataColum(length=255,type=FieldTypeEnum.STRING,comment="按钮名称")
	private String buttonName;
	@DataColum(length=2000,type=FieldTypeEnum.STRING,comment="点击触发事件")
	private String buttonFunction;
	@DataColum(length=10,type=FieldTypeEnum.STRING,comment="打开dialog宽度")
	private String modelWidth;
	@DataColum(length=10,type=FieldTypeEnum.STRING,comment="打开dialog高度")
	private String modelHeight;
	@DataColum(length=10,type=FieldTypeEnum.STRING,comment="dialog标题")
	private String modelTitle;
	@DataColum(length=100,type=FieldTypeEnum.STRING,comment="dialog路径")
	private String modelUrl;
	@DataColum(length=2,type=FieldTypeEnum.STRING,comment="按钮状态1正常0禁用")
	private String buttonState;
	@DataColum(length=40,type=FieldTypeEnum.STRING,comment="来源菜单编号")
	private String formMenuId;
	@DataColum(length=3,type=FieldTypeEnum.STRING,comment="排序号")
	private String orders;
	@DataColum(length=40,type=FieldTypeEnum.STRING,comment="按钮图标")
	private String icos;
	@DataColum(length=300,type=FieldTypeEnum.STRING,comment="权限标记")
    private String powerKey;
	
	
	@Override
	public String toString() {
		return buttonFunction+buttonName+buttonState+formMenuId+icos+id+modelHeight+modelTitle
				+modelUrl+modelWidth+orders+powerKey;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getButtonName() {
		return buttonName;
	}
	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}
	public String getButtonFunction() {
		return buttonFunction;
	}
	public void setButtonFunction(String buttonFunction) {
		this.buttonFunction = buttonFunction;
	}
	public String getModelWidth() {
		return modelWidth;
	}
	public void setModelWidth(String modelWidth) {
		this.modelWidth = modelWidth;
	}
	public String getModelHeight() {
		return modelHeight;
	}
	public void setModelHeight(String modelHeight) {
		this.modelHeight = modelHeight;
	}
	public String getModelTitle() {
		return modelTitle;
	}
	public void setModelTitle(String modelTitle) {
		this.modelTitle = modelTitle;
	}
	public String getModelUrl() {
		return modelUrl;
	}
	public void setModelUrl(String modelUrl) {
		this.modelUrl = modelUrl;
	}
	public String getButtonState() {
		return buttonState;
	}
	public void setButtonState(String buttonState) {
		this.buttonState = buttonState;
	}
	public String getFormMenuId() {
		return formMenuId;
	}
	public void setFormMenuId(String formMenuId) {
		this.formMenuId = formMenuId;
	}
	public String getOrders() {
		return orders;
	}
	public void setOrders(String orders) {
		this.orders = orders;
	}
	public String getIcos() {
		return icos;
	}
	public void setIcos(String icos) {
		this.icos = icos;
	}
	public String getPowerKey() {
		return powerKey;
	}
	public void setPowerKey(String powerKey) {
		this.powerKey = powerKey;
	}
	
	
	
}
