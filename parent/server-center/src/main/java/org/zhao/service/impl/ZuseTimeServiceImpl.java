package org.zhao.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.pojo.dao.ZrequestUseModelMapper;
import org.zhao.common.pojo.model.ZrequestUseModel;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.DateUtil;
import org.zhao.common.util.RandomUtils;
import org.zhao.common.util.view.ResultContent;
import org.zhao.service.ZuseTimeService;

@Service
public class ZuseTimeServiceImpl implements ZuseTimeService {

	@Autowired
	private ZrequestUseModelMapper zRequestUseModelMapper;

	@Cacheable(value="useTimeSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<List<ZrequestUseModel>> selectPageListByParameterRequire(
			PageContext page , Map<String, Map<String, String>> paramMap) {
		ResultContent<List<ZrequestUseModel>> result = new ResultContent<List<ZrequestUseModel>>();
		result.setData(this.zRequestUseModelMapper.selectPageListByParameterRequire(page, paramMap));
		result.setCount(this.zRequestUseModelMapper.selectPageListByParameterRequireCount(paramMap));
		return BaseResultUtil.setCodeMsg(result);
	}
	
	@CacheEvict(value="useTimeSelect" ,allEntries=true ,beforeInvocation=false)
	@Transactional
	@Override
	public ResultContent<String> save(List<ZrequestUseModel> model) {
		if(model != null && model.size() > 0) {
			for (ZrequestUseModel zrequestUseModel : model) {
				zrequestUseModel.setId(RandomUtils.getPrimaryKey());
				zrequestUseModel.setRequestTime(DateUtil.getTimeStr(new Date(), DateUtil.yyyy_MM_dd));
				this.zRequestUseModelMapper.insertSelective(zrequestUseModel);
			}
		}
		return new ResultContent<String>(ResultContent.SUCCESS, "更新完成");
	}

	
}
