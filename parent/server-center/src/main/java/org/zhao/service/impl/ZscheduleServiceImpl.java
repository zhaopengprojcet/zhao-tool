package org.zhao.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.mybatis.query.QueryParames;
import org.zhao.common.pojo.dao.ZscheduleLogModelMapper;
import org.zhao.common.pojo.dao.ZscheduleSetModelMapper;
import org.zhao.common.pojo.model.ZscheduleLogModel;
import org.zhao.common.pojo.model.ZscheduleSetModel;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.RandomUtils;
import org.zhao.common.util.view.ResultContent;
import org.zhao.service.ZscheduleService;

@Service
public class ZscheduleServiceImpl implements ZscheduleService{

	@Autowired
	private ZscheduleSetModelMapper zScheduleSetModelMapper;
	@Autowired
	private ZscheduleLogModelMapper zScheduleLogModelMapper;
	
	
	@Cacheable(value="scheduleSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<List<ZscheduleSetModel>> selectPageListByParameterRequire(
			PageContext page,
			Map<String, Map<String, String>> parames) {
		ResultContent<List<ZscheduleSetModel>> result = new ResultContent<List<ZscheduleSetModel>>();
		result.setData(this.zScheduleSetModelMapper.selectPageListByParameterRequire(page, parames));
		result.setCount(this.zScheduleSetModelMapper.selectPageListByParameterRequireCount(parames));
		return BaseResultUtil.setCodeMsg(result);
	}

	@Cacheable(value="scheduleSelect",keyGenerator="keyGenerator", unless="#result.data == null")
	@Override
	public ResultContent<ZscheduleSetModel> selectScheduleSetById(String id) {
		ResultContent<ZscheduleSetModel> result = new ResultContent<ZscheduleSetModel>();
		result.setData(this.zScheduleSetModelMapper.selectByPrimaryKey(id));
		return BaseResultUtil.setCodeMsg(result);
	}

	@CacheEvict(value="scheduleSelect" ,allEntries=true ,beforeInvocation=false)
	@Transactional
	@Override
	public ResultContent<String> save(ZscheduleSetModel model) {
		if(StringUtils.isEmpty(model.getId())) {
			QueryParames qp = QueryParames.init();
			qp.addEquality("scheduleKey", model.getScheduleKey());
			if(this.zScheduleSetModelMapper.selectPageListByParameterRequire(null, qp.getParames()).size() > 0) {
				return new ResultContent<String>(ResultContent.ERROR, "关键字已存在");
			}
			model.setCreateTime(new Date());
			model.setId(RandomUtils.getPrimaryKey());
			this.zScheduleSetModelMapper.insertSelective(model);
		}
		else {
			this.zScheduleSetModelMapper.updateByPrimaryKeySelective(model);
		}
		return new ResultContent<String>(ResultContent.SUCCESS, "操作完成");
	}

	@CacheEvict(value="scheduleSelect" ,allEntries=true ,beforeInvocation=false)
	@Transactional
	@Override
	public ResultContent<String> delete(String id) {
		this.zScheduleSetModelMapper.deleteByPrimaryKey(id);
		return new ResultContent<String>(ResultContent.SUCCESS, "操作完成");
	}

	
	@Override
	public ResultContent<ZscheduleSetModel> selectModelById(String id) {
		ResultContent<ZscheduleSetModel> result = new ResultContent<ZscheduleSetModel>();
		result.setData(this.zScheduleSetModelMapper.selectByPrimaryKey(id));
		return BaseResultUtil.setCodeMsg(result);
	}

	@Override
	public ResultContent<List<ZscheduleLogModel>> selectLogPageListByParameterRequire(
			PageContext page, Map<String, Map<String, String>> parames) {
		ResultContent<List<ZscheduleLogModel>> result = new ResultContent<List<ZscheduleLogModel>>();
		result.setData(this.zScheduleLogModelMapper.selectPageListByParameterRequire(page, parames));
		result.setCount(this.zScheduleLogModelMapper.selectPageListByParameterRequireCount(parames));
		return BaseResultUtil.setCodeMsg(result);
	}

	

	@Transactional
	@Override
	public ResultContent<String> saveLog(ZscheduleLogModel log) {
		this.zScheduleLogModelMapper.insertSelective(log);
		return new ResultContent<String>(ResultContent.SUCCESS, "操作完成");
	}

	@Transactional
	@Override
	public ResultContent<String> updateResultLog(String id, String msg) {
		ZscheduleLogModel log = this.zScheduleLogModelMapper.selectByPrimaryKey(id);
		if(log == null) return new ResultContent<String>(ResultContent.ERROR, "没有该任务");
		if(!StringUtils.isEmpty(log.getDoState())) return new ResultContent<String>(ResultContent.ERROR, "任务已完结");
		log.setDoEndTime(new Date());
		try {
			log.setDoState(JSONObject.fromObject(msg).getString("code"));
			log.setDoError(JSONObject.fromObject(msg).getString("message"));
		} catch (Exception e) {
			e.printStackTrace();
			log.setDoState("-1");
			log.setDoError(msg);
		}
		this.zScheduleLogModelMapper.updateByPrimaryKeySelective(log);
		return new ResultContent<String>(ResultContent.SUCCESS, "记录完成");
	}

	@Transactional
	@Override
	public ResultContent<String> updatePutLog(ZscheduleLogModel log) {
		this.zScheduleLogModelMapper.updateByPrimaryKeySelective(log);
		return new ResultContent<String>(ResultContent.SUCCESS, "操作完成");
	}

	
}
