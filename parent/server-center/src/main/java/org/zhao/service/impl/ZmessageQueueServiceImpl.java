package org.zhao.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhao.common.aspect.query.QueryMq;
import org.zhao.common.interceptor.PublicServerKV;
import org.zhao.common.pojo.dao.ZmessageQueueLogModelMapper;
import org.zhao.common.pojo.dao.ZmessageQueueModelMapper;
import org.zhao.common.pojo.model.ZmessageQueueLogModel;
import org.zhao.common.pojo.model.ZmessageQueueModel;
import org.zhao.common.util.RandomUtils;
import org.zhao.common.util.view.ResultContent;
import org.zhao.service.ZmessageQueueService;

@Service
public class ZmessageQueueServiceImpl implements ZmessageQueueService{

	@Autowired
	private ZmessageQueueModelMapper zMessageQueueModelMapper;
	@Autowired
	private ZmessageQueueLogModelMapper zMessageQueueLogModelMapper;
	
	@Transactional
	@Override
	public void save(ZmessageQueueModel model) {
		if(StringUtils.isEmpty(model.getId())) {
			//第一次失败或成功存入
			model.setId(RandomUtils.getPrimaryKey());
			this.zMessageQueueModelMapper.insertSelective(model);
		}
		else {
			//重新启动发送后存入
			this.zMessageQueueModelMapper.updateByPrimaryKeySelective(model);
		}
	}

	@Transactional
	@Override
	public ResultContent<String> roldMessageToSend(String id) {
		if(!PublicServerKV.getBooleanVal("server-center.service.mq")) {
			return new ResultContent<String>(ResultContent.ERROR, "系统未启用消息队列服务");
		}
		ZmessageQueueModel model = this.zMessageQueueModelMapper.selectByPrimaryKey(id);
		if(model == null) return new ResultContent<String>(ResultContent.ERROR, "未找到指定消息");
		model.setMsgState("0");
		QueryMq.flush(model);
		this.zMessageQueueModelMapper.updateByPrimaryKeySelective(model);
		
		return new ResultContent<String>(ResultContent.SUCCESS, "重新加入队列完成");
	}

	@Override
	public void saveLog(ZmessageQueueLogModel model) {
		if(StringUtils.isEmpty(model.getId())) {
			model.setId(RandomUtils.getPrimaryKey());
			this.zMessageQueueLogModelMapper.insertSelective(model);
		}
		else {
			this.zMessageQueueLogModelMapper.updateByPrimaryKeySelective(model);
		}
	}

	@Override
	public ResultContent<String> updateLog(String id, String context) {
		ZmessageQueueLogModel log = this.zMessageQueueLogModelMapper.selectByPrimaryKey(id);
		if(log == null) return new ResultContent<String>(ResultContent.ERROR, "未找到推送记录");
		log.setMqReturnContext(context);
		this.zMessageQueueLogModelMapper.updateByPrimaryKeySelective(log);
		return new ResultContent<String>(ResultContent.SUCCESS, "记录完成");
	}

	
}
