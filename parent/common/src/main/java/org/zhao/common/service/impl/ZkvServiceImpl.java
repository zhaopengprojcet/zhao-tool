package org.zhao.common.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.pojo.dao.ZkeyValueModelMapper;
import org.zhao.common.pojo.model.ZkeyValueModel;
import org.zhao.common.pojo.model.ZroleModel;
import org.zhao.common.pojo.model.ZuserModel;
import org.zhao.common.service.ZkvService;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.RandomUtils;
import org.zhao.common.util.view.ResultContent;

@Service
public class ZkvServiceImpl implements ZkvService {

	@Autowired
	private ZkeyValueModelMapper zKeyValueModelMapper;
	
	@Cacheable(value="kvSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<List<ZkeyValueModel>> selectPageListByParameterRequire(ZkeyValueModel kv,
			PageContext page, Map<String,Map<String,String>> param) {
		ResultContent<List<ZkeyValueModel>> result = new ResultContent<List<ZkeyValueModel>>();
		result.setData(this.zKeyValueModelMapper.selectPageListByParameterRequire(kv, page, param));
		result.setCount(this.zKeyValueModelMapper.selectPageListByParameterRequireCount(kv, param));
		return BaseResultUtil.setCodeMsg(result);
	}

	@Cacheable(value="kvSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<ZkeyValueModel> selectKvById(String id) {
		ResultContent<ZkeyValueModel> result = new ResultContent<ZkeyValueModel>();
		result.setData(this.zKeyValueModelMapper.selectByPrimaryKey(id));
		return BaseResultUtil.setCodeMsg(result);
	}

	@CacheEvict(value="kvSelect" ,allEntries=true ,beforeInvocation=false)
	@Transactional
	@Override
	public ResultContent<String> save(ZkeyValueModel kv) {
		if(StringUtils.isEmpty(kv.getId())) {
			ZkeyValueModel old = new ZkeyValueModel();
			old.setServerKey(kv.getServerKey());
			if(this.zKeyValueModelMapper.selectPageListByParametersCount(old) > 0) {
				return new ResultContent<String>(ResultContent.ERROR, "key已存在");
			}
			kv.setId(RandomUtils.getPrimaryKey());
			this.zKeyValueModelMapper.insertSelective(kv);
		}
		else {
			this.zKeyValueModelMapper.updateByPrimaryKeySelective(kv);
		}
		return new ResultContent<String>(ResultContent.SUCCESS, "更新完成");
	}

	@CacheEvict(value="kvSelect" ,allEntries=true ,beforeInvocation=false)
	@Transactional
	@Override
	public ResultContent<String> delete(String id) {
		this.zKeyValueModelMapper.deleteByPrimaryKey(id);
		return new ResultContent<String>(ResultContent.SUCCESS , "更新完成");
	}

	@Cacheable(value="kvSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<String> getStringValue(String key) {
		ZkeyValueModel zvm = new ZkeyValueModel();
		zvm.setServerKey(key);
		List<ZkeyValueModel> list = this.zKeyValueModelMapper.selectPageListByParameters(zvm, null);
		if(CollectionUtils.isEmpty(list)) return new ResultContent<String>(ResultContent.ERROR, "错误的key");
		return new ResultContent<String>(ResultContent.SUCCESS, "查询成功" ,list.get(0).getServerValue());
	}

	@Cacheable(value="kvSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<Integer> getIntValue(String key) {
		ZkeyValueModel zvm = new ZkeyValueModel();
		zvm.setServerKey(key);
		List<ZkeyValueModel> list = this.zKeyValueModelMapper.selectPageListByParameters(zvm, null);
		if(CollectionUtils.isEmpty(list)) return new ResultContent<Integer>(ResultContent.ERROR, "错误的key");
		try {
			int num = Integer.parseInt(list.get(0).getServerValue());
			return new ResultContent<Integer>(ResultContent.SUCCESS, "查询成功" , num);
		} catch (Exception e) {
			return new ResultContent<Integer>(ResultContent.ERROR, "value格式错误");
		}
	}

	@Cacheable(value="kvSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<Boolean> getBooleanValue(String key) {
		ZkeyValueModel zvm = new ZkeyValueModel();
		zvm.setServerKey(key);
		List<ZkeyValueModel> list = this.zKeyValueModelMapper.selectPageListByParameters(zvm, null);
		if(CollectionUtils.isEmpty(list)) return new ResultContent<Boolean>(ResultContent.ERROR, "错误的key");
		try {
			boolean bo = Boolean.valueOf(list.get(0).getServerValue());
			return new ResultContent<Boolean>(ResultContent.SUCCESS, "查询成功" , bo);
		} catch (Exception e) {
			return new ResultContent<Boolean>(ResultContent.ERROR, "value格式错误");
		}
	}
	
	
}
