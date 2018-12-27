package org.zhao.schedule.thread;

import org.zhao.schedule.annotation.ScheduleMethod;
import org.zhao.schedule.annotation.zhaoScheduleBean;
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
	
	//监测存活，有返回值则表示当前服务可用
	@ScheduleMethod(scheduleName="zhao_schedule_live")
	public String refresh() {
		System.out.println("zhao_schedule_live 执行");
		return ReturnCode.SUCCESS;
	}
}
