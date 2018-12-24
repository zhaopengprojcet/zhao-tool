package org.zhao.common.pojo.model;

import java.io.Serializable;

import org.zhao.common.databean.annotation.DataBean;
import org.zhao.common.databean.annotation.DataColum;
import org.zhao.common.databean.em.FieldTypeEnum;
import org.zhao.common.util.view.UpdateTypeEm;
import org.zhao.common.util.view.UpdateView;

@DataBean(tableName="R_ROLE")
public class ZroleModel implements Serializable{
	
	@UpdateView(check="",name="id",text="编号",key= true ,type=UpdateTypeEm.HIDDEN)
	@DataColum(isKey=true,length=40,type=FieldTypeEnum.STRING,comment="编号")
    private String id;
	@UpdateView(check="{'required':true,'maxLength':10,'minLength':1}",name="roleName",text="角色名称",type=UpdateTypeEm.TEXT)
	@DataColum(length=20,type=FieldTypeEnum.STRING,comment="角色名称")
	private String roleName;

	
	
    @Override
	public String toString() {
		return id+roleName;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }
}