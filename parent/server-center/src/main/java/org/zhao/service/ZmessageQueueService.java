package org.zhao.service;

import org.zhao.common.pojo.model.ZmessageQueueLogModel;
import org.zhao.common.pojo.model.ZmessageQueueModel;
import org.zhao.common.util.view.ResultContent;

public interface ZmessageQueueService {

	void save(ZmessageQueueModel model);
	
	/**
	 * 将过期或失败消息重新加入队列
	 * @param id
	 * @return
	 */
	ResultContent<String> roldMessageToSend(String id);
	
	
	void saveLog(ZmessageQueueLogModel model);
	
	ResultContent<String> updateLog(String id,  String context);
}
