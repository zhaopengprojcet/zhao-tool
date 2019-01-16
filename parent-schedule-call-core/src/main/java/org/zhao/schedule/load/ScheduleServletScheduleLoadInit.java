package org.zhao.schedule.load;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.zhao.core.common.model.PutThread;
import org.zhao.core.common.util.ThreadPoolUtils;
import org.zhao.schedule.annotation.ScheduleMethod;
import org.zhao.schedule.annotation.zhaoScheduleBean;
import org.zhao.schedule.model.ScheduleModel;

/**
 * 初始化项目中包含的schedule计划任务
 * @author zhao
 *
 */
public class ScheduleServletScheduleLoadInit implements ApplicationListener<ContextRefreshedEvent>{

	private Log logger = LogFactory.getLog(this.getClass());
	
	private static Map<String, ScheduleModel> schedules = new HashMap<String, ScheduleModel>();
	
	public static Map<String, ScheduleModel> getSchedules() {
		return schedules;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			logger.info("----------- 扫描定时任务开始 -----------------");
			Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(zhaoScheduleBean.class);
			for (Object cla : beans.values()) {
				Class<?> in = cla.getClass();
				Method[] methods = in.getDeclaredMethods();
				for (Method method : methods) {
					if(method.isAnnotationPresent(ScheduleMethod.class)) { 
						//加入定时任务本地 缓存 集合
						ScheduleMethod sm = method.getAnnotation(ScheduleMethod.class);
						if(StringUtils.isEmpty(sm.scheduleName())) throw new RuntimeException("定时任务关键字为空【"+cla.getClass().getName()+"."+method.getName()+"】");
						if(schedules.containsKey(sm.scheduleName())) throw new RuntimeException("定时任务关键字重复【"+sm.scheduleName()+"】");
						ScheduleModel model = new ScheduleModel();
						model.setCla(cla);
						model.setMethod(method);
						schedules.put(sm.scheduleName(), model);
					}
				}
			}
			logger.info("----------- 扫描定时任务完成 -----------------");
			if(schedules.keySet().size() > 0) {
				logger.info("向中心服务器注册任务");
				//每次发起注册服务都将删除原有本服务所提供的 定时任务
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("schedules", JSONArray.fromObject(ScheduleServletScheduleLoadInit.getSchedules().keySet()));
				ThreadPoolUtils.putThread("定时任务注册", new PutThread(map , "/server/putSchedule.html"));
			}
		}
	}

}
