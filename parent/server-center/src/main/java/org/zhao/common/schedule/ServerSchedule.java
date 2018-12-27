package org.zhao.common.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.zhao.common.aspect.query.QuerySchedules;
import org.zhao.common.interceptor.PublicServerKV;
import org.zhao.common.util.CacheUtil;
import org.zhao.common.util.DateUtil;
import org.zhao.common.util.ThreadPoolUtils;

@Component
@Order(value = 9)
public class ServerSchedule implements ApplicationRunner{

	private static boolean doit = true;
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		if(PublicServerKV.getBooleanVal("server.service.schedule")) { //定时任务推送
			Thread schedule = new Thread(){

				@Override
				public void run() {
					//每次运行时对当前时间做检查，查看当前时间是否有需要执行的任务，将任务发送到queryschedules中，并标记此时需要做的任务已经完成，避免同一个时间点的检查重复
					
					//-----待实现
					Map<String, List<String>> services = (Map<String, List<String>>) CacheUtil.getMapListCache("schedule_clients", "*");
					if(services == null) return;
					if(doit) {
						doit = false;
						
						ThreadPoolUtils.putThread("注册定时任务调用", new QuerySchedules(new ArrayList<String>(services.keySet()), null));
						
					}
				}
				
			};
			ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
			pool.scheduleWithFixedDelay(  
					schedule,  
					getWaitTime(PublicServerKV.getStringVal("server.service.schedule.wait")),  
					PublicServerKV.getIntVal("server.service.schedule.split"),  
					TimeUnit.SECONDS); 
		}
	}
	
	private long getWaitTime(String time) {
		if(StringUtils.isEmpty(time)) return 0;
		try {
			String day = DateUtil.getTimeStr(new Date(), DateUtil.yyyy_MM_dd);
			long tm = DateUtil.getTime(day + " " + time, DateUtil.yyyy_MM_dd_hh_mm_ss).getTime();
			tm = tm - System.currentTimeMillis();
			if(tm > 0) return tm / 1000;
			return tm / 1000 + 60 * 60 * 24;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
