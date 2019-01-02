package org.zhao.schedule.thread;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zhao.core.common.model.PutThread;
import org.zhao.core.common.util.ThreadPoolUtils;
import org.zhao.schedule.annotation.ScheduleMethod;
import org.zhao.schedule.annotation.zhaoScheduleBean;
import org.zhao.schedule.load.ServletScheduleLoadInit;
import org.zhao.schedule.model.ReturnCode;

/**
 * 初始化时加入定时任务
 * 
 * 用于中心服务器 监测服务存活状态
 * @author zhao
 *
 */
@zhaoScheduleBean
public class RefreshSchedule {
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	//监测存活，有返回值则表示当前服务可用
	@ScheduleMethod(scheduleName="zhao_schedule_live")
	public String refresh() {
		if(ServletScheduleLoadInit.getSchedules().keySet().size() > 0) {
			logger.info("向中心服务器注册任务");
			//每次发起注册服务都将删除原有本服务所提供的 定时任务
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("schedules", JSONArray.fromObject(ServletScheduleLoadInit.getSchedules().keySet()));
			ThreadPoolUtils.putThread("定时任务注册", new PutThread(map , "/server/putSchedule.html"));
		}
		return ReturnCode.SUCCESS;
	}
}
