package org.zhao.mq.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zhao.core.common.util.HttpUtils;
import org.zhao.core.common.util.RegiestServer;
import org.zhao.core.common.util.SignUtil;
import org.zhao.mq.annotation.em.MqTypeEm;
import org.zhao.mq.model.MqContext;

public class MessageUtil {

	private static Log logger = LogFactory.getLog(MessageUtil.class);
	
	public static final String MMC_KEY = "MMC";
	
	public static ResultContent<String> sendMsg(String key , String context , MqTypeEm type) {
		String url = RegiestServer.queryPutUrl("/server/putMsg.html");
 		Map<String, String> obj2 = new HashMap<String, String>();
 		MqContext code = new MqContext();
 		code.setContext(context);
 		code.setPushType(type.getValue());
 		code.setSendTime(new Date());
 		code.setService(key);
 		code.setToken(RegiestServer.getToken(false , MMC_KEY));
 		
		obj2.put("_jr", SignUtil.getHttpContext(JSONObject.fromObject(code).toString()));
		JSONObject result = HttpUtils.post(url, obj2);
		logger.info("发送队列消息返回结果：" + result);
		return (ResultContent<String>) JSONObject.toBean(result, ResultContent.class);
	}
}
