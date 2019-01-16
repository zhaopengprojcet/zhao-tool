package org.zhao.common.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.zhao.common.aspect.query.QuerySchedules;
import org.zhao.common.interceptor.PublicServerKV;
import org.zhao.common.mybatis.query.QueryParames;
import org.zhao.common.pojo.model.ZscheduleSetModel;
import org.zhao.common.util.CacheUtil;
import org.zhao.common.util.DateUtil;
import org.zhao.common.util.ThreadPoolUtils;
import org.zhao.common.util.view.ResultContent;
import org.zhao.service.ZscheduleService;

@Component
@Order(value = 9)
public class ServerSchedule implements ApplicationRunner{
	
	private static final String SCHEDULE_MAP_KEY = "SCHEDULE_MAP_KEY";
	
	private static final String SIGN_SCHEDULE_MAP_KEY = "SIGN_SCHEDULE_MAP_KEY";
	
	private static String loadTime = "";
	
	private static boolean fush = false;
	
	private Log logger = LogFactory.getLog(this.getClass());
	@Autowired
	private ZscheduleService zScheduleService;
	
	public static void reload() {
		CacheUtil.removeCache(SCHEDULE_MAP_KEY);
		CacheUtil.removeCache(SIGN_SCHEDULE_MAP_KEY);
		loadTime = null;
		fush = false;
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		if(PublicServerKV.getBooleanVal("server-center.service.schedule")) { //定时任务推送
			Thread schedule = new Thread(){

				@Override
				public void run() {
					Date now = new Date();
					if(StringUtils.isEmpty(loadTime) || !DateUtil.getTimeStr(now, DateUtil.yyyy_MM_dd).equals(loadTime)) { //今日未加载过
						//加载全部生效的设置定时任务，并解析时间逻辑到缓存
						QueryParames queryParames = QueryParames.init();
						queryParames.addEquality("scheduleState", "1");
						ResultContent<List<ZscheduleSetModel>> load = zScheduleService.selectPageListByParameterRequire(null, queryParames.getParames());
						if(CollectionUtils.isNotEmpty(load.getData())) {
							//结构  时间-->[调用逻辑-->[调用集合]]
							Map<String, Map<String, List<String>>> plans = new HashMap<String, Map<String, List<String>>>();
							for (ZscheduleSetModel doSc : load.getData()) {
								if(doSc.getScheduleType().equals("FIXTIME")) {
									String[] crons = doSc.getScheduleCron().split("\\|");
									if(crons == null || crons.length < 1) {
										logger.error("定时任务【"+doSc.getScheduleKey()+"】未解析到正确的周期逻辑，忽略");
										continue;
									}
									for (String time : crons) {
										if(!StringUtils.isEmpty(time)) {
											plans = setSchedule(plans, time, doSc.getPushType(), doSc.getScheduleKey());
										}
									}
								}
								else if(doSc.getScheduleType().equals("FIXWEEK")) {
									String[] crons = doSc.getScheduleCron().split("\\|");
									if(crons == null || crons.length < 1) {
										logger.error("定时任务【"+doSc.getScheduleKey()+"】未解析到正确的周期逻辑，忽略");
										continue;
									}
									for (String time : crons) {
										if(!StringUtils.isEmpty(time)) {
											String[] wt = time.split("-");
											if(wt.length < 2) {
												logger.error("定时任务【"+doSc.getScheduleKey()+"】未解析到正确的周期逻辑，忽略");
												break;
											}
											if(Integer.parseInt(wt[0]) == DateUtil.getWeekDay()) {
												plans = setSchedule(plans, wt[1], doSc.getPushType(), doSc.getScheduleKey());
											}
										}
									}
								}
								else if(doSc.getScheduleType().equals("FIXDAY")) {
									String[] crons = doSc.getScheduleCron().split("\\|");
									if(crons == null || crons.length < 1) {
										logger.error("定时任务【"+doSc.getScheduleKey()+"】未解析到正确的周期逻辑，忽略");
										continue;
									}
									for (String time : crons) {
										if(!StringUtils.isEmpty(time)) {
											String[] wt = time.split("-");
											if(wt.length < 2) {
												logger.error("定时任务【"+doSc.getScheduleKey()+"】未解析到正确的周期逻辑，忽略");
												break;
											}
											if(Integer.parseInt(wt[0]) == DateUtil.getMonthDay()) {
												plans = setSchedule(plans, wt[1], doSc.getPushType(), doSc.getScheduleKey());
											}
										}
									}
								}
								else if(doSc.getScheduleType().equals("EC")) {
									List<String> times = DateUtil.getSplitTime(now, DateUtil.getDateEnd(now), Integer.parseInt(doSc.getScheduleCron()));
									for (String string : times) {
										plans = setSchedule(plans, string, doSc.getPushType(), doSc.getScheduleKey());
									}
									
								}
							}

							CacheUtil.saveSingleCache(SCHEDULE_MAP_KEY, plans);
						}
						
						loadTime = DateUtil.getTimeStr(new Date(), DateUtil.yyyy_MM_dd);
					}
					
					Map<String, Map<String, List<String>>> plans = (Map<String, Map<String, List<String>>>) CacheUtil.getSingleCache(SCHEDULE_MAP_KEY);
					if(plans == null) return;
					if(plans.containsKey(DateUtil.getTimeStr(now, DateUtil.hh_mm))) {
						Map<String, List<String>> services = plans.get(DateUtil.getTimeStr(now, DateUtil.hh_mm));
						for(String key : services.keySet()) {
							ThreadPoolUtils.putThread("定时任务调用扫描", new QuerySchedules(services.get(key) , null ,key ,zScheduleService , true));
						}
					}
				}
				
			};
			ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
			pool.scheduleWithFixedDelay(  
					schedule,  
					getWaitTime(PublicServerKV.getStringVal("server-center.service.schedule.wait")),  
					PublicServerKV.getIntVal("server-center.service.schedule.split"),  
					TimeUnit.SECONDS); 
			Thread scheduleFush = new Thread(){

				@Override
				public void run() {
					if(!fush) { //今日未加载过
						//加载全部生效的设置定时任务，并解析时间逻辑到缓存
						QueryParames queryParames = QueryParames.init();
						queryParames.addEquality("scheduleState", "1");
						ResultContent<List<ZscheduleSetModel>> load = zScheduleService.selectPageListByParameterRequire(null, queryParames.getParames());
						if(CollectionUtils.isNotEmpty(load.getData())) {
							//结构  时间-->[调用逻辑-->[调用集合]]
							Map<String , List<String>> plans = new HashMap<String, List<String>>();
							for (ZscheduleSetModel doSc : load.getData()) {
								if(doSc.getScheduleType().equals("FUSH")) {
									List<String> l = null;
									if(plans.containsKey(doSc.getScheduleType())) {
										l = plans.get(doSc.getScheduleType());
									}
									else {
										l = new ArrayList<String>();
									}
									l.add(doSc.getScheduleKey());
									plans.put(doSc.getScheduleType(), l);
								}
							}

							CacheUtil.saveSingleCache(SIGN_SCHEDULE_MAP_KEY, plans);
						}
						
						fush = true;
					}
					
					Map<String , List<String>> plans = (Map<String , List<String>>) CacheUtil.getSingleCache(SIGN_SCHEDULE_MAP_KEY);
					if(plans == null) return;
					for(String key : plans.keySet()) {
						ThreadPoolUtils.putThread("推送定时任务调用扫描", new QuerySchedules(plans.get(key) , null ,key ,zScheduleService , false));
					}
					
				}
				
			};
			pool.scheduleWithFixedDelay(  
					scheduleFush,  
					1,  
					1,  
					TimeUnit.SECONDS); 
		}
	}
	
	private Map<String, Map<String, List<String>>> setSchedule(Map<String, Map<String, List<String>>> map ,String time , String type , String service) {
		Map<String, List<String>> ds = null;
		List<String> keys = null;
		if(map.containsKey(time)) {
			ds = map.get(time);
		}
		else {
			ds = new HashMap<String, List<String>>();
		}
		if(ds.containsKey(type)) {
			keys = ds.get(type);
		}
		else {
			keys = new ArrayList<String>();
		}
		
		if(!keys.contains(service)) keys.add(service);
		
		ds.put(type, keys);
		map.put(time, ds);
		return map;
		
		
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
