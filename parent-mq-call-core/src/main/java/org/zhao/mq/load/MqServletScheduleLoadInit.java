package org.zhao.mq.load;

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
import org.zhao.mq.annotation.MqMethod;
import org.zhao.mq.annotation.ZhaoMessageBean;
import org.zhao.mq.model.MqModel;

/**
 * 初始化项目中包含的消息队列订阅任务
 * @author zhao
 *
 */
public class MqServletScheduleLoadInit implements ApplicationListener<ContextRefreshedEvent>{

	private Log logger = LogFactory.getLog(this.getClass());
	
	private static Map<String, MqModel> mqServices = new HashMap<String, MqModel>();
	
	public static Map<String, MqModel> getMqServices() {
		return mqServices;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			logger.info("----------- 扫描消息订阅开始 -----------------");
			Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(ZhaoMessageBean.class);
			for (Object cla : beans.values()) {
				Class<?> in = cla.getClass();
				Method[] methods = in.getDeclaredMethods();
				for (Method method : methods) {
					if(method.isAnnotationPresent(MqMethod.class)) { 
						//加入定时任务本地 缓存 集合
						MqMethod sm = method.getAnnotation(MqMethod.class);
						if(StringUtils.isEmpty(sm.mqName())) throw new RuntimeException("消息mq关键字为空【"+cla.getClass().getName()+"."+method.getName()+"】");
						if(mqServices.containsKey(sm.mqName())) throw new RuntimeException("定时任务关键字重复【"+sm.mqName()+"】");
						MqModel model = new MqModel();
						model.setCla(cla);
						model.setMethod(method);
						mqServices.put(sm.mqName(), model);
					}
				}
			}
			logger.info("----------- 扫描消息订阅完成 -----------------");
			if(mqServices.keySet().size() > 0) {
				logger.info("向中心服务器注册任务");
				//每次发起注册服务都将删除原有本服务所提供的 定时任务
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("mqServices", JSONArray.fromObject(MqServletScheduleLoadInit.getMqServices().keySet()));
				ThreadPoolUtils.putThread("消息订阅注册", new PutThread(map , "/server/putMqClient.html"));
			}
		}
	}

}
