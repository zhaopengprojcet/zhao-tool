package org.zhao.mq.thread;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.zhao.core.common.util.HttpUtils;
import org.zhao.core.common.util.RegiestServer;
import org.zhao.core.common.util.SignUtil;
import org.zhao.mq.load.MqServletScheduleLoadInit;
import org.zhao.mq.model.MqModel;
import org.zhao.mq.model.ReturnCode;

/**
 * mq消息调用线程，用于在执行完成后反馈执行结果
 * @author zhao
 *
 */
public class MqDo extends Thread {

	private String logId; //任务编号
	
	private MqModel model;
	
	private String context;
	
	private ApplicationContext applicationContext;
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	public MqDo(){}
	
	public MqDo(String logId ,String context , MqModel model , ApplicationContext applicationContext){
		this.logId = logId;
		this.model = model;
		this.context = context;
		this.applicationContext = applicationContext;
	}

	@Override
	public void run() {
		Object cla = applicationContext.getBean(model.getCla().getClass());
		String code = ReturnCode.result(logId, ReturnCode.ERROR);
		try {
			Object result = model.getMethod().invoke(cla , context);
			code = ReturnCode.result(logId, JSONObject.fromObject(result).toString());
		} catch (Exception e) {
			logger.error("执行队列消息调度失败");
			e.printStackTrace();
			code = ReturnCode.result(logId, e.getLocalizedMessage());
		}
		
		String url = RegiestServer.queryPutUrl("/server/mqState.html");
 		Map<String, String> obj2 = new HashMap<String, String>();
		obj2.put("_jr", SignUtil.getHttpContext(code));
		JSONObject result = HttpUtils.post(url, obj2);
		logger.info("队列消息执行结果反馈返回结果：" + result);
		
	}
	
	
}
