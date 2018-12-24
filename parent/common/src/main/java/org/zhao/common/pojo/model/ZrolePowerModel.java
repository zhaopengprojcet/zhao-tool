package org.zhao.common.pojo.model;

import java.io.Serializable;

import org.zhao.common.databean.annotation.DataBean;
import org.zhao.common.databean.annotation.DataColum;
import org.zhao.common.databean.em.FieldTypeEnum;

@DataBean(tableName="R_ROLE_POWER")
public class ZrolePowerModel implements Serializable{

	@DataColum(isKey=true,length=40,type=FieldTypeEnum.STRING,comment="编号")
    private String id;

	@DataColum(length=40,type=FieldTypeEnum.STRING,comment="角色编号")
    private String roleId;

	@DataColum(length=40,type=FieldTypeEnum.STRING,comment="资源编号")
    private String powerId;

	@DataColum(length=2,type=FieldTypeEnum.STRING,comment="资源类型1菜单2按钮3列表字段")
    private String powerType;

	
	
    @Override
	public String toString() {
		return id+roleId+powerId+powerType;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public String getPowerId() {
        return powerId;
    }

    public void setPowerId(String powerId) {
        this.powerId = powerId == null ? null : powerId.trim();
    }

    public String getPowerType() {
        return powerType;
    }

    public void setPowerType(String powerType) {
        this.powerType = powerType == null ? null : powerType.trim();
    }
}