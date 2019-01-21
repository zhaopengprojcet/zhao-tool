package org.zhao.mcs.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.zhao.core.common.model.PutThread;
import org.zhao.core.common.util.ConfigProperties;
import org.zhao.core.common.util.ThreadPoolUtils;
import org.zhao.mcs.util.MemoryCpuUtil;

/**
 * 推送信息 
 * @author zhao
 *
 */
public class RegiestClientFullDataConfig implements SchedulingConfigurer{

	private static String cron ; 

	private final static String MCC_KEY = "MCC";
	
	public RegiestClientFullDataConfig() {
		cron = ConfigProperties.instance().getPropertiesVal("server.mcs.put.cron");
	}
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.addTriggerTask(new Runnable() {

            @Override
            public void run() {
            	Map<String, Object> map = new HashMap<String, Object>();
            	map.put("mcs", MemoryCpuUtil.getMemoryCupData());		
            	ThreadPoolUtils.putThread("服务器状态记录", new PutThread(map , "/server/putMcs.html" , MCC_KEY));
            }
        }, new Trigger() {

            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                // 任务触发，可修改任务的执行周期
                CronTrigger trigger = new CronTrigger(cron);
                Date nextExecutionTime = trigger.nextExecutionTime(triggerContext);
                return nextExecutionTime;
            }
        });
		
	}
	
	public static void setCron(String cron) {
		RegiestClientFullDataConfig.cron = cron;
	}
}
