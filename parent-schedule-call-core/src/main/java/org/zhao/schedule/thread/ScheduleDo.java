package org.zhao.schedule.thread;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.zhao.core.common.model.ReturnCode;
import org.zhao.core.common.util.HttpUtils;
import org.zhao.core.common.util.RegiestServer;
import org.zhao.core.common.util.SignUtil;
import org.zhao.schedule.load.ScheduleServletScheduleLoadInit;
import org.zhao.schedule.model.ScheduleModel;

/**
 * 定时任务调用线程 ， 用于在调用任务后 向服务器返回 执行结果
 * @author zhao
 *
 */
public class ScheduleDo extends Thread {

	private String scheduleId; //定时任务编号
	
	private ScheduleModel model;
	
	private boolean writeLog = true;
	
	private ApplicationContext applicationContext;
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	public ScheduleDo(){}
	
	public ScheduleDo(String scheduleId ,ScheduleModel model , ApplicationContext applicationContext , boolean writeLog){
		this.scheduleId = scheduleId;
		this.model = model;
		this.applicationContext = applicationContext;
		this.writeLog = writeLog;
	}

	@Override
	public void run() {
		Object cla = applicationContext.getBean(model.getCla().getClass());
		String code = ReturnCode.result(scheduleId, ReturnCode.ERROR ,ScheduleServletScheduleLoadInit.SCC_KEY);
		try {
			Object result = model.getMethod().invoke(cla);
			code = ReturnCode.result(scheduleId, JSONObject.fromObject(result).toString() ,ScheduleServletScheduleLoadInit.SCC_KEY);
		} catch (Exception e) {
			logger.error("执行定时任务调度失败");
			e.printStackTrace();
			code = ReturnCode.result(scheduleId, e.getLocalizedMessage() ,ScheduleServletScheduleLoadInit.SCC_KEY);
		}
		
		if(writeLog) {
			String url = RegiestServer.queryPutUrl("/server/scheduleState.html");
	 		Map<String, String> obj2 = new HashMap<String, String>();
			obj2.put("_jr", SignUtil.getHttpContext(code));
			JSONObject result = HttpUtils.post(url, obj2);
			logger.info("定时任务执行结果反馈返回结果：" + result);
		}
		
	}
	
	
}
