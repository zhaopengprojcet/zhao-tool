package org.zhao.common.util.view;

import java.util.ArrayList;
import java.util.List;

import org.zhao.common.pojo.model.ZmenuButtonModel;
import org.zhao.common.pojo.model.ZmenuFieldModel;
import org.zhao.common.pojo.model.ZmenuModel;
import org.zhao.common.pojo.model.ZmenuSearchModel;
import org.zhao.common.pojo.model.ZrolePowerModel;
import org.zhao.common.util.RandomUtils;

public class RolePowerHtml {

	/**
	 * 菜单menu_check 按钮button_check 列表字段field_check
	 * @param menus
	 * @return
	 */
	public static String rolePowerHtmls(List<ZmenuModel> menus) {
		StringBuffer sbf = new StringBuffer();
		if(menus != null && menus.size() >0) {
			menus = new ArrayList<ZmenuModel>(menus);
			for (ZmenuModel projectViewMenu : menus) {
				sbf.append("<tr>");
				sbf.append("<td rowspan=\""+(projectViewMenu.getChildren() == null?1:projectViewMenu.getChildren().size()+1)+"\"><input type=\"checkbox\" value=\""+projectViewMenu.getId()+"\" pkey=\"0\" name=\"menu_check\"/>"+projectViewMenu.getMenuName()+"</td>");
				sbf.append("</tr>");
				if(projectViewMenu.getChildren() != null && projectViewMenu.getChildren().size() > 0) {
					for (ZmenuModel pc : projectViewMenu.getChildren()) {
						sbf.append("<tr>");
						sbf.append("<td><input type=\"checkbox\" value=\""+pc.getId()+"\" pkey=\""+projectViewMenu.getId()+"\" name=\"menu_check\"/>"+pc.getMenuName()+"</td>");
						sbf.append("<td>");
						if(pc.getButtons() != null && pc.getButtons().size() > 0) {
							sbf.append("<ul>");
							for (ZmenuButtonModel pb : pc.getButtons()) {
								sbf.append("<li><input type=\"checkbox\" value=\""+pb.getId()+"\" pkey=\""+pc.getId()+"\" name=\"button_check\"/>"+pb.getButtonName()+"</li>");
							}
							sbf.append("</ul>");
						}
						sbf.append("</td>");
						sbf.append("<td>");
						if(pc.getFields() != null && pc.getFields().size() > 0) {
							sbf.append("<ul>");
							for (ZmenuFieldModel pf : pc.getFields()) {
								sbf.append("<li><input type=\"checkbox\" value=\""+pf.getId()+"\" pkey=\""+pc.getId()+"\" name=\"field_check\"/>"+pf.getFieldName()+"</li>");
							}
							sbf.append("</ul>");
						}
						sbf.append("</td>");
						sbf.append("<td>");
						sbf.append("<button class=\"power-button\" type=\"button\">全选</button>");
						sbf.append("</td>");
						sbf.append("</tr>");
					}
				}
			}
		}
		return sbf.toString();
	}
	
	public static List<ZrolePowerModel> add(String type , String roleId , List<String> powersId) {
		List<ZrolePowerModel> list = new ArrayList<ZrolePowerModel>();
		for (String string : powersId) {
			ZrolePowerModel zp = new ZrolePowerModel();
			zp.setId(RandomUtils.getPrimaryKey());
			zp.setPowerId(string);
			zp.setPowerType(type);
			zp.setRoleId(roleId);
			list.add(zp);
		}
		return list;
	}
}
