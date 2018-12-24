package org.zhao.common.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhao.common.pojo.dao.ZmenuButtonModelMapper;
import org.zhao.common.pojo.dao.ZmenuFieldModelMapper;
import org.zhao.common.pojo.dao.ZmenuModelMapper;
import org.zhao.common.pojo.dao.ZmenuSearchModelMapper;
import org.zhao.common.pojo.dao.ZrolePowerModelMapper;
import org.zhao.common.pojo.model.ZmenuButtonModel;
import org.zhao.common.pojo.model.ZmenuFieldModel;
import org.zhao.common.pojo.model.ZmenuModel;
import org.zhao.common.pojo.model.ZmenuSearchModel;
import org.zhao.common.pojo.model.ZrolePowerModel;
import org.zhao.common.pojo.model.ZuserModel;
import org.zhao.common.service.ZmenuService;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.RandomUtils;
import org.zhao.common.util.view.ResultContent;

@Service
public class ZmenuServiceImpl implements ZmenuService {

	@Autowired
	private ZmenuModelMapper zmenuModelMapper;
	@Autowired
	private ZmenuButtonModelMapper zMenuButtonModelMapper;
	@Autowired
	private ZmenuFieldModelMapper zMenuFieldModelMapper;
	@Autowired
	private ZmenuSearchModelMapper zMenuSearchModelMapper;
	@Autowired
	private ZrolePowerModelMapper zRolePowerModelMapper;
	
	@Cacheable(value="menuBtnFieldSearchSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<List<ZmenuModel>> selectMenuListOfTree(String pid) {
		return new ResultContent<List<ZmenuModel>>(this.zmenuModelMapper.selectMenuListOfTree(pid));
	}

	
	@Cacheable(value="menuBtnFieldSearchSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<List<ZmenuModel>> selectAllResourcesList(String pid) {
		return new ResultContent<List<ZmenuModel>>(this.zmenuModelMapper.selectAllResourcesList(pid));
	}


	@Cacheable(value="menuBtnFieldSearchSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<ZmenuModel> selectMenuById(String id) {
		return new ResultContent<ZmenuModel>(this.zmenuModelMapper.selectByPrimaryKey(id));
	}


	@Cacheable(value="menuBtnFieldSearchSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<List<ZmenuModel>> selectcomboListMenuOfTree(String pid) {
		ZmenuModel base = new ZmenuModel();
		base.setId("0");
		base.setMenuName("根目录");
		base.setChildren(this.zmenuModelMapper.selectcomboListMenuOfTree(pid));
		List<ZmenuModel> list = new ArrayList<ZmenuModel>();
		list.add(base);
		return new ResultContent<List<ZmenuModel>>(list);
	}


	@Cacheable(value="menuBtnFieldSearchSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<List<ZmenuModel>> findResourcesByRoleId(String roleId) {
		return new ResultContent<List<ZmenuModel>>(this.zmenuModelMapper.selectResourcesByRoleId(roleId));
	}
	@Cacheable(value="menuBtnFieldSearchSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<List<ZmenuModel>> selectMenuListByRoleId(String roleId) {
		return new ResultContent<List<ZmenuModel>>(this.zmenuModelMapper.selectMenuListByRoleId(roleId));
	}


	@CacheEvict(value="menuBtnFieldSearchSelect" ,allEntries=true ,beforeInvocation=false)
	@Transactional
	@Override
	public ResultContent<String> save(ZmenuModel menu) {
		if(!StringUtils.isEmpty(menu.getParentMenuId())) {
			ZmenuModel parent = this.zmenuModelMapper.selectByPrimaryKey(menu.getParentMenuId());
		}
		if(StringUtils.isEmpty(menu.getId())) {
			menu.setId(RandomUtils.getPrimaryKey());
			this.zmenuModelMapper.insertSelective(menu);
		}
		else {
			this.zmenuModelMapper.updateByPrimaryKeySelective(menu);
		}
		return new ResultContent<String>(ResultContent.SUCCESS , "更新完成");
	}

	@CacheEvict(value="menuBtnFieldSearchSelect" ,allEntries=true ,beforeInvocation=false)
	@Transactional
	@Override
	public ResultContent<String> delete(String id) {
		List<ZmenuModel> menuList = this.zmenuModelMapper.selectcomboListMenuOfTree(id);
		
		class MenuChange{
			List<String> ids = new ArrayList<String>();
			
			private ZmenuModelMapper zmenuModelMapper;
			private ZmenuButtonModelMapper zMenuButtonModelMapper;
			private ZmenuFieldModelMapper zMenuFieldModelMapper;
			private ZmenuSearchModelMapper zMenuSearchModelMapper;
			private ZrolePowerModelMapper zRolePowerModelMapper;
			
			public MenuChange(ZmenuModelMapper zmenuModelMapper,
								ZmenuButtonModelMapper zMenuButtonModelMapper,
								ZmenuFieldModelMapper zMenuFieldModelMapper,
								ZmenuSearchModelMapper zMenuSearchModelMapper,
								ZrolePowerModelMapper zRolePowerModelMapper) {
				this.zMenuButtonModelMapper = zMenuButtonModelMapper;
				this.zMenuFieldModelMapper = zMenuFieldModelMapper;
				this.zmenuModelMapper = zmenuModelMapper;
				this.zMenuSearchModelMapper = zMenuSearchModelMapper;
				this.zRolePowerModelMapper = zRolePowerModelMapper;
			}
			
			public List<String> delete(List<ZmenuModel> list) {
				if(list == null) return null;
				for (ZmenuModel zmenuModel : list) {
					ids.add(zmenuModel.getId());
					if(!CollectionUtils.isEmpty(zmenuModel.getChildren())) {
						delete(zmenuModel.getChildren());
					}
				}
				return ids;
			}
			
			public void deleteOthers(String menuId) {
				//关联按钮
				ZmenuButtonModel zbm = new ZmenuButtonModel();
				zbm.setFormMenuId(menuId);
				List<ZmenuButtonModel> buttons = this.zMenuButtonModelMapper.selectPageListByParameters(zbm, null);
				this.zMenuButtonModelMapper.deleteByParames(zbm);
				for (ZmenuButtonModel button : buttons) {
					ZrolePowerModel zpm = new ZrolePowerModel();
					zpm.setPowerId(button.getId());
					this.zRolePowerModelMapper.deleteByParames(zpm);
				}
				//关联列表字段
				ZmenuFieldModel zfm = new ZmenuFieldModel();
				zfm.setParentMenuId(menuId);
				List<ZmenuFieldModel> fields = this.zMenuFieldModelMapper.selectPageListByParameters(zfm, null);
				this.zMenuFieldModelMapper.deleteByParames(zfm);
				for (ZmenuFieldModel field : fields) {
					ZrolePowerModel zpm = new ZrolePowerModel();
					zpm.setPowerId(field.getId());
					this.zRolePowerModelMapper.deleteByParames(zpm);
				}
				//关联查询字段
				ZmenuSearchModel zsm = new ZmenuSearchModel();
				zsm.setParentMenuId(menuId);
				List<ZmenuSearchModel> searchs = this.zMenuSearchModelMapper.selectPageListByParameters(zsm, null);
				this.zMenuSearchModelMapper.deleteByParames(zsm);
				for (ZmenuSearchModel search : searchs) {
					ZrolePowerModel zpm = new ZrolePowerModel();
					zpm.setPowerId(search.getId());
					this.zRolePowerModelMapper.deleteByParames(zpm);
				}
				//关联权限
				ZrolePowerModel zpm = new ZrolePowerModel();
				zpm.setPowerId(menuId);
				this.zRolePowerModelMapper.deleteByParames(zpm);
				//自身
				this.zmenuModelMapper.deleteByPrimaryKey(menuId);
			}
		}
		MenuChange mc = new MenuChange(this.zmenuModelMapper,
									   this.zMenuButtonModelMapper,
									   this.zMenuFieldModelMapper,
									   this.zMenuSearchModelMapper,
									   this.zRolePowerModelMapper
		);
		
		List<String> childIds = mc.delete(menuList);
		if(!CollectionUtils.isEmpty(childIds)) {
			for (String string : childIds) {
				mc.deleteOthers(string);
			}
		}
		mc.deleteOthers(id);
		return new ResultContent<String>(ResultContent.SUCCESS, "删除完成");
	}


	@Cacheable(value="menuBtnFieldSearchSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<List<ZmenuButtonModel>> selectButtonsForMenuId(
			String menuId) {
		ResultContent<List<ZmenuButtonModel>> result = new ResultContent<List<ZmenuButtonModel>>();
		ZmenuButtonModel button = new ZmenuButtonModel();
		button.setFormMenuId(menuId);
		result.setData(this.zMenuButtonModelMapper.selectPageListByParameters(button, null));
		return BaseResultUtil.setCodeMsg(result);
	}


	@CacheEvict(value="menuBtnFieldSearchSelect" ,allEntries=true ,beforeInvocation=false)
	@Transactional
	@Override
	public ResultContent<String> buttonSave(String menuId,
			List<ZmenuButtonModel> buttons) {
		ZmenuButtonModel button = new ZmenuButtonModel();
		button.setFormMenuId(menuId);
		this.zMenuButtonModelMapper.deleteByParames(button);
		if(!CollectionUtils.isEmpty(buttons)) {
			ZmenuModel menu = this.zmenuModelMapper.selectByPrimaryKey(menuId);
			for (ZmenuButtonModel zmenuButtonModel : buttons) {
				zmenuButtonModel.setFormMenuId(menuId);
				if(StringUtils.isEmpty(zmenuButtonModel.getId())) zmenuButtonModel.setId(RandomUtils.getPrimaryKey());
			}
			this.zMenuButtonModelMapper.batchCreate(buttons);
		}
		return new ResultContent<String>(ResultContent.SUCCESS, "更新完成");
	}


	@Cacheable(value="menuBtnFieldSearchSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<List<ZmenuFieldModel>> selectFieldsForMenuId(
			String menuId) {
		ResultContent<List<ZmenuFieldModel>> result = new ResultContent<List<ZmenuFieldModel>>();
		ZmenuFieldModel field = new ZmenuFieldModel();
		field.setParentMenuId(menuId);
		result.setData(this.zMenuFieldModelMapper.selectPageListByParameters(field, null));
		return BaseResultUtil.setCodeMsg(result);
	}

	@CacheEvict(value="menuBtnFieldSearchSelect" ,allEntries=true ,beforeInvocation=false)
	@Transactional
	@Override
	public ResultContent<String> fieldSave(String menuId,
			List<ZmenuFieldModel> fields) {
		ZmenuFieldModel field = new ZmenuFieldModel();
		field.setParentMenuId(menuId);
		this.zMenuFieldModelMapper.deleteByParames(field);
		if(!CollectionUtils.isEmpty(fields)) {
			for (ZmenuFieldModel zMenuFieldModel : fields) {
				zMenuFieldModel.setParentMenuId(menuId);
				if(StringUtils.isEmpty(zMenuFieldModel.getId())) zMenuFieldModel.setId(RandomUtils.getPrimaryKey());
			}
			this.zMenuFieldModelMapper.batchCreate(fields);
		}
		return new ResultContent<String>(ResultContent.SUCCESS, "更新完成");
	}


	@Cacheable(value="menuBtnFieldSearchSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<List<ZmenuSearchModel>> selectSearchsForMenuId(
			String menuId) {
		ResultContent<List<ZmenuSearchModel>> result = new ResultContent<List<ZmenuSearchModel>>();
		ZmenuSearchModel search = new ZmenuSearchModel();
		search.setParentMenuId(menuId);
		result.setData(this.zMenuSearchModelMapper.selectPageListByParameters(search, null));
		return BaseResultUtil.setCodeMsg(result);
	}


	@CacheEvict(value="menuBtnFieldSearchSelect" ,allEntries=true ,beforeInvocation=false)
	@Transactional
	@Override
	public ResultContent<String> searchSave(String menuId,
			List<ZmenuSearchModel> searchs) {
		ZmenuSearchModel search = new ZmenuSearchModel();
		search.setParentMenuId(menuId);
		this.zMenuSearchModelMapper.deleteByParames(search);
		if(!CollectionUtils.isEmpty(searchs)) {
			for (ZmenuSearchModel zMenuSearchModel : searchs) {
				zMenuSearchModel.setParentMenuId(menuId);
				if(StringUtils.isEmpty(zMenuSearchModel.getId())) zMenuSearchModel.setId(RandomUtils.getPrimaryKey());
				if(StringUtils.isEmpty(zMenuSearchModel.getValueField())) zMenuSearchModel.setValueField("id");
				if(StringUtils.isEmpty(zMenuSearchModel.getTextField())) zMenuSearchModel.setTextField("text");
			}
			this.zMenuSearchModelMapper.batchCreate(searchs);
		}
		return new ResultContent<String>(ResultContent.SUCCESS, "更新完成");
	}


	@Cacheable(value="menuBtnFieldSearchSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<List<String>> selectAllPowerKeyByRoleId(String roleId) {
		ResultContent<List<String>> result = new ResultContent<List<String>>();
		result.setData(this.zmenuModelMapper.selectMeunAndInfosPowerKeyByRole(roleId));
		return BaseResultUtil.setCodeMsg(result);
	}
	
	
	
}
