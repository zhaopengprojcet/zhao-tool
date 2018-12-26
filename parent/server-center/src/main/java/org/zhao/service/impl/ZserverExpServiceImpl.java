package org.zhao.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.pojo.dao.ZserverExpModelMapper;
import org.zhao.common.pojo.model.ZserverExpModel;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.RandomUtils;
import org.zhao.common.util.view.ResultContent;
import org.zhao.service.ZserverExpService;

@Service
public class ZserverExpServiceImpl implements ZserverExpService {

	@Autowired
	private ZserverExpModelMapper zServerExpModelMapper;

	@Transactional
	@Override
	public ResultContent<String> save(ZserverExpModel model) {
		model.setId(RandomUtils.getPrimaryKey());
		model.setCreateTime(new Date());
		model.setExpState("0");
		this.zServerExpModelMapper.insertSelective(model);
		return new ResultContent<String>(ResultContent.SUCCESS, "处理完成");
	}

	@Override
	public ResultContent<List<ZserverExpModel>> selectPageListByParameterRequire(
			PageContext page,
			Map<String, Map<String, String>> require) {
		ResultContent<List<ZserverExpModel>> result = new ResultContent<List<ZserverExpModel>>();
		result.setData(this.zServerExpModelMapper.selectPageListByParameterRequire(page, require));
		result.setCount(this.zServerExpModelMapper.selectPageListByParameterRequireCount( require));
		return BaseResultUtil.setCodeMsg(result);
	}
	
	
}
