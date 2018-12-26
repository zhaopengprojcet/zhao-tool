package org.zhao.common.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.pojo.dao.ZwhiteBlackIpListMapper;
import org.zhao.common.pojo.model.ZwhiteBlackIpList;
import org.zhao.common.service.ZwhiteBlackIpService;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.RandomUtils;
import org.zhao.common.util.view.ResultContent;

@Service
public class ZwhiteBlackIpServiceImpl implements ZwhiteBlackIpService{

	@Autowired
	private ZwhiteBlackIpListMapper zWhiteBlackIpListMapper;

	@Cacheable(value="wbIpSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<List<ZwhiteBlackIpList>> selectPageListByParameterRequire(
			PageContext page,
			Map<String, Map<String, String>> require) {
		ResultContent<List<ZwhiteBlackIpList>> result = new ResultContent<List<ZwhiteBlackIpList>>();
		result.setData(this.zWhiteBlackIpListMapper.selectPageListByParameterRequire( page, require));
		result.setCount(this.zWhiteBlackIpListMapper.selectPageListByParameterRequireCount(require));
		return BaseResultUtil.setCodeMsg(result);
	}
	@Cacheable(value="wbIpSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<ZwhiteBlackIpList> selectIpById(String id) {
		ResultContent<ZwhiteBlackIpList> result = new ResultContent<ZwhiteBlackIpList>();
		result.setData(this.zWhiteBlackIpListMapper.selectByPrimaryKey(id));
		return BaseResultUtil.setCodeMsg(result);
	}
	@CacheEvict(value="wbIpSelect" ,allEntries=true ,beforeInvocation=false)
	@Override
	public ResultContent<String> save(ZwhiteBlackIpList ip) {
		if(StringUtils.isEmpty(ip.getId())) {
			ip.setId(RandomUtils.getPrimaryKey());
			ip.setCreateTime(new Date());
			this.zWhiteBlackIpListMapper.insertSelective(ip);
		}
		else {
			this.zWhiteBlackIpListMapper.updateByPrimaryKeySelective(ip);
		}
		return new ResultContent<String>(ResultContent.SUCCESS, "更新完成");
	}
	@CacheEvict(value="wbIpSelect" ,allEntries=true ,beforeInvocation=false)
	@Override
	public ResultContent<String> delete(String id) {
		this.zWhiteBlackIpListMapper.deleteByPrimaryKey(id);
		return new ResultContent<String>(ResultContent.SUCCESS, "更新完成");
	}
	
	
}
