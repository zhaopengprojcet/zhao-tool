package org.zhao.common.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhao.common.interceptor.RequestLoginInterceptor;
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.pojo.dao.ZroleModelMapper;
import org.zhao.common.pojo.dao.ZrolePowerModelMapper;
import org.zhao.common.pojo.model.ZroleModel;
import org.zhao.common.pojo.model.ZrolePowerModel;
import org.zhao.common.service.ZroleService;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.RandomUtils;
import org.zhao.common.util.view.ResultContent;
import org.zhao.common.util.view.RolePowerHtml;

@Service
public class ZroleServiceImpl implements ZroleService {
	
	@Autowired
	private ZroleModelMapper zRoleModelMapper;
	@Autowired
	private ZrolePowerModelMapper zRolePowerModelMapper;
	
	@Cacheable(value="roleSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<ZroleModel> selectRoleById(String id) {
		ResultContent<ZroleModel> result = new ResultContent<ZroleModel>();
		result.setData(this.zRoleModelMapper.selectByPrimaryKey(id));
		return BaseResultUtil.setCodeMsg(result);
	}
	@Cacheable(value="roleSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<List<ZroleModel>> selectComboList() {
		ResultContent<List<ZroleModel>> result = new ResultContent<List<ZroleModel>>();
		result.setData(this.zRoleModelMapper.selectPageListByParameters(new ZroleModel(), null));
		return BaseResultUtil.setCodeMsg(result);
	}
	@Cacheable(value="roleSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<List<ZroleModel>> selectPageListByParameterRequire(
			ZroleModel role, PageContext page,
			Map<String, Map<String, String>> require) {
		ResultContent<List<ZroleModel>> result = new ResultContent<List<ZroleModel>>();
		result.setData(this.zRoleModelMapper.selectPageListByParameterRequire(role, page, require));
		result.setCount(this.zRoleModelMapper.selectPageListByParameterRequireCount(role, require));
		return BaseResultUtil.setCodeMsg(result);
	}
	@CacheEvict(value="roleSelect" ,allEntries=true ,beforeInvocation=false)
	@Transactional
	@Override
	public ResultContent<String> save(ZroleModel role , HttpServletRequest request) {
		if(StringUtils.isEmpty(role.getId())) {
			role.setId(RandomUtils.getPrimaryKey());
			this.zRoleModelMapper.insertSelective(role);
		}
		else 
			this.zRoleModelMapper.updateByPrimaryKeySelective(role);
		RequestLoginInterceptor.updateServletPowerStatic(request, role.getId());
		
		return new ResultContent<String>(ResultContent.SUCCESS, "更新完成");
	}
	@CacheEvict(value="roleSelect" ,allEntries=true ,beforeInvocation=false)
	@Transactional
	@Override
	public ResultContent<String> delete(String id , HttpServletRequest request) {
		//删除权限关联
		ZrolePowerModel power = new ZrolePowerModel();
		power.setRoleId(id);
		this.zRolePowerModelMapper.deleteByParames(power);
		//删除角色
		this.zRoleModelMapper.deleteByPrimaryKey(id);
		
		RequestLoginInterceptor.deleteServletPowerStatic(request, id);
		return new ResultContent<String>(ResultContent.SUCCESS, "更新完成");
	}
	@Cacheable(value="roleSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<List<ZrolePowerModel>> selectRolePowers(String id) {
		ResultContent<List<ZrolePowerModel>> result = new ResultContent<List<ZrolePowerModel>>();
		ZrolePowerModel power = new ZrolePowerModel();
		power.setRoleId(id);
		result.setData(this.zRolePowerModelMapper.selectPageListByParameters(power, null));
		return BaseResultUtil.setCodeMsg(result);
	}
	@CacheEvict(value="roleSelect" ,allEntries=true ,beforeInvocation=false)
	@Transactional
	@Override
	public ResultContent<String> updatePowers(JSONObject json , HttpServletRequest request) {
		//删除原有
		String roleKey = json.getString("role_key");
		ZrolePowerModel power = new ZrolePowerModel();
		power.setRoleId(roleKey);
		this.zRolePowerModelMapper.deleteByParames(power);
		Set<ZrolePowerModel> list = new HashSet<ZrolePowerModel>();
		if(json.containsKey("menu_check")) {
			//菜单
			list.addAll(RolePowerHtml.add("1", roleKey, JSONArray.toList(json.getJSONArray("menu_check"), String.class)));
		}
		if(json.containsKey("button_check")) {
			//按钮
			list.addAll(RolePowerHtml.add("2", roleKey, JSONArray.toList(json.getJSONArray("button_check"), String.class)));
		}
		if(json.containsKey("field_check")) {
			//列表字段
			list.addAll(RolePowerHtml.add("3", roleKey, JSONArray.toList(json.getJSONArray("field_check"), String.class)));
		}
		if(list.size() > 0) {
			this.zRolePowerModelMapper.batchCreate(new ArrayList<ZrolePowerModel>(list));
		}
		
		RequestLoginInterceptor.updateServletPowerStatic(request, roleKey);
		return new ResultContent<String>(ResultContent.SUCCESS, "更新完成");
	}

	
}
