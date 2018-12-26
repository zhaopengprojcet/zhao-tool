package org.zhao.common.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.pojo.model.ZroleModel;
import org.zhao.common.pojo.model.ZrolePowerModel;
import org.zhao.common.util.view.ResultContent;

public interface ZroleService {

	ResultContent<ZroleModel> selectRoleById(String id);
	
	ResultContent<List<ZroleModel>> selectComboList();
	
	ResultContent<List<ZroleModel>> selectPageListByParameterRequire(PageContext page ,  Map<String, Map<String, String>> require);
	
	ResultContent<String> save(ZroleModel role , HttpServletRequest request);
	
	ResultContent<String> delete(String id , HttpServletRequest request);
	
	ResultContent<List<ZrolePowerModel>> selectRolePowers(String id);
	
	ResultContent<String> updatePowers(JSONObject json , HttpServletRequest request);
}
