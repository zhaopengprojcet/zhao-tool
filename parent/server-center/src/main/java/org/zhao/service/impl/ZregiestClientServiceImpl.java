package org.zhao.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.mybatis.query.QueryParames;
import org.zhao.common.pojo.dao.ZregiestClientModelMapper;
import org.zhao.common.pojo.model.ZregiestClientModel;
import org.zhao.common.pojo.model.ZscheduleSetModel;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.RandomUtils;
import org.zhao.common.util.view.ResultContent;
import org.zhao.service.ZregiestClientService;

@Service
public class ZregiestClientServiceImpl implements ZregiestClientService {

	@Autowired
	private ZregiestClientModelMapper zRegiestClientModelMapper;
	
	@Cacheable(value="clientSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<List<ZregiestClientModel>> selectPageListByParameterRequire(
			PageContext page,
			Map<String, Map<String, String>> parames) {
		ResultContent<List<ZregiestClientModel>> result = new ResultContent<List<ZregiestClientModel>>();
		result.setData(this.zRegiestClientModelMapper.selectPageListByParameterRequireNoPass(page, parames));
		result.setCount(this.zRegiestClientModelMapper.selectPageListByParameterRequireCount(parames));
		return BaseResultUtil.setCodeMsg(result);
	}

	@Cacheable(value="clientSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<ZregiestClientModel> selectClientById(String id) {
		ResultContent<ZregiestClientModel> result = new ResultContent<ZregiestClientModel>();
		result.setData(this.zRegiestClientModelMapper.selectByPrimaryKey(id));
		return BaseResultUtil.setCodeMsg(result);
	}

	@CacheEvict(value="clientSelect" ,allEntries=true ,beforeInvocation=false)
	@Transactional
	@Override
	public ResultContent<String> save(ZregiestClientModel model) {
		if(StringUtils.isEmpty(model.getId())) {
			QueryParames qp = QueryParames.init();
			qp.addEquality("name", model.getName());
			if(this.zRegiestClientModelMapper.selectPageListByParameterRequire(null, qp.getParames()).size() > 0) {
				return new ResultContent<String>(ResultContent.ERROR, "帐号已存在");
			}
			model.setCreateTime(new Date());
			model.setId(RandomUtils.getPrimaryKey());
			this.zRegiestClientModelMapper.insertSelective(model);
		}
		else {
			this.zRegiestClientModelMapper.updateByPrimaryKeySelective(model);
		}
		return new ResultContent<String>(ResultContent.SUCCESS, "操作完成");
	}

	@CacheEvict(value="clientSelect" ,allEntries=true ,beforeInvocation=false)
	@Transactional
	@Override
	public ResultContent<String> delete(String id) {
		this.zRegiestClientModelMapper.deleteByPrimaryKey(id);
		return new ResultContent<String>(ResultContent.SUCCESS, "操作完成");
	}

}
