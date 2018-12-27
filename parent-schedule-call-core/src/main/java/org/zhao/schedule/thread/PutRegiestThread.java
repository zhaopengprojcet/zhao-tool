package org.zhao.schedule.thread;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zhao.core.common.util.HttpUtils;
import org.zhao.core.common.util.RegiestServer;
import org.zhao.core.common.util.SignUtil;
import org.zhao.schedule.load.ServletScheduleLoadInit;

/**
 * 将本地待注册定时任务推送到中心服务器
 * @author zhao
 *
 */
public class PutRegiestThread extends Thread{

	private Log logger = LogFactory.getLog(this.getClass());
	
	@Override
	public void run() {
		String url = RegiestServer.queryPutUrl("/server/putSchedule.html");
 		JSONObject obj = new JSONObject();
 		obj.put("token", RegiestServer.getToken(false));
 		obj.put("schedules", JSONArray.fromObject(ServletScheduleLoadInit.getSchedules().keySet()));
 		Map<String, String> obj2 = new HashMap<String, String>();
		obj2.put("_jr", SignUtil.getHttpContext(JSONObject.fromObject(obj).toString()));
		JSONObject result = HttpUtils.post(url, obj2);
		logger.info("定时任务注册返回结果：" + result);
	}

}
