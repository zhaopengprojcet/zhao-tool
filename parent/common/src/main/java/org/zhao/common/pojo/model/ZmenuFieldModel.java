package org.zhao.common.pojo.model;

import java.io.Serializable;

import org.zhao.common.databean.annotation.DataBean;
import org.zhao.common.databean.annotation.DataColum;
import org.zhao.common.databean.em.FieldTypeEnum;

/**
 * 列表页面列表字段集合
 * @author zhao
 *
 */
@DataBean(tableName="R_MENU_FIELD")
public class ZmenuFieldModel implements Serializable{

	@DataColum(isKey=true,length=40,type=FieldTypeEnum.STRING,comment="编号")
	private String id;
	@DataColum(length=15,notNull=true,type=FieldTypeEnum.STRING,comment="字段名称")
	private String fieldName;
	@DataColum(length=10 ,type=FieldTypeEnum.STRING,comment="宽度百分比")
	private String width;
	@DataColum(length=10,type=FieldTypeEnum.STRING,comment="格式位置/left/center/right")
	private String align;
	@DataColum(length=20,type=FieldTypeEnum.STRING,comment="格式化函数")
	private String formatter;
	@DataColum(length=20,type=FieldTypeEnum.STRING,comment="字段key")
	private String fieldKey;
	@DataColum(length=40,type=FieldTypeEnum.STRING,comment="菜单编号")
	private String parentMenuId;
	@DataColum(length=5,type=FieldTypeEnum.STRING,comment="字段排序")
	private String orders;

	
	
	@Override
	public String toString() {
		return align+fieldKey+fieldName+formatter+id+orders+parentMenuId+width;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getFormatter() {
		return formatter;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}

	public String getFieldKey() {
		return fieldKey;
	}

	public void setFieldKey(String fieldKey) {
		this.fieldKey = fieldKey;
	}

	public String getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
	}
	public String getOrders() {
		return orders;
	}
	public void setOrders(String orders) {
		this.orders = orders;
	}
	
	
}
