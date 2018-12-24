package org.zhao.common.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhao.common.pojo.dao.ZLogModelMapper;
import org.zhao.common.pojo.model.ZLogModel;
import org.zhao.common.service.ZLogService;
import org.zhao.common.util.RandomUtils;

@Service
public class ZLogServiceImpl implements ZLogService {

	@Autowired
	private ZLogModelMapper zLogModelMapper;

	@Transactional
	@Override
	public void insertLog(String type, String key, String desc) {
		ZLogModel log = new ZLogModel();
		log.setId(RandomUtils.getPrimaryKey());
		log.setCreateTime(new Date());
		log.setLogDesc(desc);
		log.setLogKey(key);
		log.setLogType(type);
		this.zLogModelMapper.insertSelective(log);
	}
	
	
}
