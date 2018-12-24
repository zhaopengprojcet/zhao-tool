package org.zhao.common.service;

import java.util.List;

import org.zhao.common.pojo.model.ZmenuButtonModel;
import org.zhao.common.pojo.model.ZmenuFieldModel;
import org.zhao.common.pojo.model.ZmenuModel;
import org.zhao.common.pojo.model.ZmenuSearchModel;
import org.zhao.common.pojo.model.ZuserModel;
import org.zhao.common.util.view.ResultContent;

public interface ZmenuService {

	ResultContent<ZmenuModel> selectMenuById(String id);
	
	ResultContent<List<ZmenuModel>> selectMenuListOfTree(String pid);
	
	ResultContent<List<ZmenuModel>> selectAllResourcesList(String pid);
	
	ResultContent<List<ZmenuModel>> selectcomboListMenuOfTree(String pid);
	
	ResultContent<List<ZmenuModel>> findResourcesByRoleId(String roleId);
	
	ResultContent<List<ZmenuModel>> selectMenuListByRoleId(String roleId);
	
	ResultContent<String> save(ZmenuModel menu);
	
	ResultContent<String> delete(String id);
	
	
	/** button **/
	ResultContent<List<ZmenuButtonModel>> selectButtonsForMenuId(String menuId);
	
	ResultContent<String> buttonSave(String menuId , List<ZmenuButtonModel> buttons);
	
	
	/** field **/
	ResultContent<List<ZmenuFieldModel>> selectFieldsForMenuId(String menuId);
	
	ResultContent<String> fieldSave(String menuId , List<ZmenuFieldModel> fields);
	
	
	/** searchs **/
	ResultContent<List<ZmenuSearchModel>> selectSearchsForMenuId(String menuId);
	
	ResultContent<String> searchSave(String menuId , List<ZmenuSearchModel> searchs);
	
	
	/** power **/
	ResultContent<List<String>> selectAllPowerKeyByRoleId(String roleId);
}
