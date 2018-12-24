package org.zhao.common.pojo.model;

import java.io.Serializable;
import java.util.List;

import org.zhao.common.databean.annotation.DataBean;
import org.zhao.common.databean.annotation.DataColum;
import org.zhao.common.databean.em.FieldTypeEnum;
import org.zhao.common.util.view.UpdateTypeEm;
import org.zhao.common.util.view.UpdateView;

/**
 * 菜单
 * @author zhao
 *
 */
@DataBean(tableName="R_MENU")
public class ZmenuModel implements Serializable{

	@UpdateView(check="",name="id",text="编号",type=UpdateTypeEm.HIDDEN)
	@DataColum(isKey=true,length=40,type=FieldTypeEnum.STRING,comment="编号")
    private String id;
	
	@UpdateView(check="{'required':true,'maxLength':10,'minLength':1}",name="menuName",text="菜单名称",type=UpdateTypeEm.TEXT)
	@DataColum(length=60,notNull=true,type=FieldTypeEnum.STRING,comment="菜单名称")
    private String menuName;

	@UpdateView(check="{'required':true}",name="parentMenuId",text="上级菜单",type=UpdateTypeEm.SELECT_TREE,loadValueUrl="/menu/comboList.html",textField="menuName")
	@DataColum(length=40,type=FieldTypeEnum.STRING,comment="上级菜单编号")
    private String parentMenuId;
	
	@UpdateView(check="",name="menuHerf",text="菜单链接",type=UpdateTypeEm.TEXT)
	@DataColum(length=300,type=FieldTypeEnum.STRING,comment="菜单链接")
    private String menuHerf;

	@UpdateView(check="",name="menuState",text="菜单状态",type=UpdateTypeEm.SELECT , loadValue=UpdateFinalModel.CAN_USE)
	@DataColum(length=2,type=FieldTypeEnum.STRING,comment="菜单状态1正常2禁用")
    private String menuState;

	@UpdateView(check="",name="powerKey",text="权限标记",type=UpdateTypeEm.TEXTARRAY)
	@DataColum(length=300,type=FieldTypeEnum.STRING,comment="权限标记")
    private String powerKey;
	
	@UpdateView(check="",name="listType",text="列表类型",type=UpdateTypeEm.SELECT , loadValue=UpdateFinalModel.VIEW_TYPE)
	@DataColum(length=15,type=FieldTypeEnum.STRING,comment="菜单中的列表类型 table/tree/other，用于区分跳转模版")
	private String listType;
	
	@UpdateView(check="",name="listInner",text="列表参数",type=UpdateTypeEm.TEXT)
	@DataColum(length=300,type=FieldTypeEnum.STRING,comment="菜单中的列表参数TEXT,逗号分隔，以easyui格式为准")
	private String listInner;
	
	@UpdateView(check="",name="orders",text="排序号",type=UpdateTypeEm.TEXT)
	@DataColum(length=2,type=FieldTypeEnum.STRING,comment="排序号")
	private String orders;
    
	private List<ZmenuButtonModel> buttons;
	
	private List<ZmenuFieldModel> fields;
	
	private List<ZmenuModel> children;
	
	private List<ZmenuSearchModel> searchs;
	
	@Override
	public String toString() {
		return id+listInner+listType+menuHerf+menuName+menuName+menuState+orders+parentMenuId+powerKey;
	}
	
	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public String getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(String parentMenuId) {
        this.parentMenuId = parentMenuId == null ? null : parentMenuId.trim();
    }

    public String getMenuHerf() {
        return menuHerf;
    }

    public void setMenuHerf(String menuHerf) {
        this.menuHerf = menuHerf == null ? null : menuHerf.trim();
    }

    public String getMenuState() {
        return menuState;
    }

    public void setMenuState(String menuState) {
        this.menuState = menuState == null ? null : menuState.trim();
    }

	public String getPowerKey() {
		return powerKey;
	}

	public void setPowerKey(String powerKey) {
		this.powerKey = powerKey;
	}
	public String getOrders() {
		return orders;
	}

	public void setOrders(String orders) {
		this.orders = orders;
	}

	public List<ZmenuButtonModel> getButtons() {
		return buttons;
	}

	public void setButtons(List<ZmenuButtonModel> buttons) {
		this.buttons = buttons;
	}

	public List<ZmenuFieldModel> getFields() {
		return fields;
	}

	public void setFields(List<ZmenuFieldModel> fields) {
		this.fields = fields;
	}

	public List<ZmenuModel> getChildren() {
		return children;
	}

	public void setChildren(List<ZmenuModel> children) {
		this.children = children;
	}

	public List<ZmenuSearchModel> getSearchs() {
		return searchs;
	}

	public void setSearchs(List<ZmenuSearchModel> searchs) {
		this.searchs = searchs;
	}

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public String getListInner() {
		return listInner;
	}

	public void setListInner(String listInner) {
		this.listInner = listInner;
	}

}