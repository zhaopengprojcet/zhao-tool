package org.zhao.common.pojo.model;

import java.io.Serializable;

import org.zhao.common.databean.annotation.DataBean;
import org.zhao.common.databean.annotation.DataColum;
import org.zhao.common.databean.em.FieldTypeEnum;

@DataBean(tableName="R_MENU_SEARCH")
public class ZmenuSearchModel implements Serializable{

	@DataColum(isKey=true,length=40,type=FieldTypeEnum.STRING,comment="编号")
    private String id;
	@DataColum(length=40,notNull=true,type=FieldTypeEnum.STRING,comment="查询字段所属菜单编号")
	private String parentMenuId;
	@DataColum(length=15,notNull=true,type=FieldTypeEnum.STRING,comment="查询字段名称")
	private String searchText;
	@DataColum(length=15,notNull=true,type=FieldTypeEnum.STRING,comment="表单名称")
	private String searchKey;
	@DataColum(length=15,notNull=true,type=FieldTypeEnum.STRING,comment="查询类型")
	private String searchType;
	@DataColum(length=300,type=FieldTypeEnum.STRING,comment="初始加载数据")
	private String loadValue;
	@DataColum(length=100,type=FieldTypeEnum.STRING,comment="初始加载数据请求地址/用于可以使用easyui加载的控件")
	private String loadValueUrl;
	@DataColum(length=2,type=FieldTypeEnum.STRING,comment="排序号")
	private String orders;
	@DataColum(length=15,notNull=true,type=FieldTypeEnum.STRING,comment="查询字段状态1可用2禁用")
	private String searchState;
	@DataColum(length=15,notNull=true,type=FieldTypeEnum.STRING,comment="值字段")
	private String valueField;
	@DataColum(length=15,notNull=true,type=FieldTypeEnum.STRING,comment="显示字段")
	private String textField;
	
	
	
	@Override
	public String toString() {
		return id+loadValue+loadValueUrl+orders+parentMenuId+searchKey+searchState+searchText
				+searchType+textField+valueField;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentMenuId() {
		return parentMenuId;
	}
	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
	}
	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	public String getSearchKey() {
		return searchKey;
	}
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getLoadValue() {
		return loadValue;
	}
	public void setLoadValue(String loadValue) {
		this.loadValue = loadValue;
	}
	public String getLoadValueUrl() {
		return loadValueUrl;
	}
	public void setLoadValueUrl(String loadValueUrl) {
		this.loadValueUrl = loadValueUrl;
	}
	public String getSearchState() {
		return searchState;
	}
	public void setSearchState(String searchState) {
		this.searchState = searchState;
	}
	public String getOrders() {
		return orders;
	}
	public void setOrders(String orders) {
		this.orders = orders;
	}
	public String getValueField() {
		return valueField;
	}
	public void setValueField(String valueField) {
		this.valueField = valueField;
	}
	public String getTextField() {
		return textField;
	}
	public void setTextField(String textField) {
		this.textField = textField;
	}
	
	
}
