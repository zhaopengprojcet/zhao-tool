package org.zhao.common.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhao.common.interceptor.PublicServerKV;
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.pojo.dao.ZroleModelMapper;
import org.zhao.common.pojo.dao.ZuserModelMapper;
import org.zhao.common.pojo.model.ZroleModel;
import org.zhao.common.pojo.model.ZuserModel;
import org.zhao.common.properties.ConfigProperties;
import org.zhao.common.service.ZuserService;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.RandomUtils;
import org.zhao.common.util.view.ResultContent;

@Service
public class ZuserServiceImpl implements ZuserService {

	@Autowired
	private ZuserModelMapper zuserModelMapper;
	@Autowired
	private ZroleModelMapper zRoleModelMapper;
	
	
	@Cacheable(value="userSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<List<ZuserModel>> selectPageListByParameterRequire(PageContext page, Map<String,Map<String,String>> param) {
		ResultContent<List<ZuserModel>> result = new ResultContent<List<ZuserModel>>();
		result.setData(this.zuserModelMapper.selectPageListByParameterRequire(page, param));
		result.setCount(this.zuserModelMapper.selectPageListByParameterRequireCount(param));
		return BaseResultUtil.setCodeMsg(result);
	}

	@Cacheable(value="userSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<ZuserModel> selectUserById(String id) {
		ResultContent<ZuserModel> result = new ResultContent<ZuserModel>();
		result.setData(this.zuserModelMapper.selectByPrimaryKey(id));
		return BaseResultUtil.setCodeMsg(result);
	}

	@CacheEvict(value="userSelect" ,allEntries=true ,beforeInvocation=false)
	@Transactional
	@Override
	public ResultContent<String> save(ZuserModel user) {
		if(!StringUtils.isEmpty(user.getRoleId())) {
			ZuserModel old = new ZuserModel();
			old.setLoginName(user.getLoginName());
			if(this.zuserModelMapper.selectPageListByParametersCount(old) > 0) {
				return new ResultContent<String>(ResultContent.ERROR, "用户名已存在");
			}
			ZroleModel role = this.zRoleModelMapper.selectByPrimaryKey(user.getRoleId());
			if(role != null) user.setRoleName(role.getRoleName());
		}
		if(StringUtils.isEmpty(user.getId())) {
			user.setId(RandomUtils.getPrimaryKey());
			user.setCreateTime(new Date());
			user.setLoginErrorCount(0);
			user.setPassword(new Md5Hash(user.getPassword(), user.getId(), 2).toString());
			this.zuserModelMapper.insertSelective(user);
		}
		else {
			this.zuserModelMapper.updateByPrimaryKeySelective(user);
		}
		return new ResultContent<String>(ResultContent.SUCCESS, "更新完成");
	}

	@CacheEvict(value="userSelect" ,allEntries=true ,beforeInvocation=false)
	@Override
	public ResultContent<String> delete(String id) {
		this.zuserModelMapper.deleteByPrimaryKey(id);
		return new ResultContent<String>(ResultContent.SUCCESS , "更新完成");
	}

	@Override
	public ResultContent<ZuserModel> checkUserLogin(String name, String pass) {
		ZuserModel user = new ZuserModel();
		user.setLoginName(name);
		user.setUserState("1");
		List<ZuserModel> list = this.zuserModelMapper.selectPageListByParameters(user, null);
		if(CollectionUtils.isEmpty(list)) {
			return new ResultContent<ZuserModel>(ResultContent.ERROR, "用户名不存在");
		}
		user = list.get(0);
		if(user.getLoginErrorCount() >= PublicServerKV.getIntVal("login.error.max")) {
			return new ResultContent<ZuserModel>(ResultContent.ERROR, "今日登录错误次数已超过,请明日再次尝试或联系管理员");
		}
		
		if(user.getPassword().equals(new Md5Hash(pass, user.getId(), 2).toString())) {
			
			user.setLastLoginTime(new Date());
			this.zuserModelMapper.updateByPrimaryKeySelective(user);
			
			return new ResultContent<ZuserModel>(ResultContent.SUCCESS, "验证通过",user);
		}
		else {
			user.setLoginErrorCount(user.getLoginErrorCount() + 1);
			this.zuserModelMapper.updateByPrimaryKeySelective(user);
			
			return new ResultContent<ZuserModel>(ResultContent.ERROR, "密码错误");
		}
	}

	
}
