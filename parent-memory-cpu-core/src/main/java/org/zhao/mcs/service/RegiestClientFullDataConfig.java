package org.zhao.mcs.service;

import java.util.Date;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.zhao.mcs.model.PutThread;
import org.zhao.mcs.util.ConfigProperties;
import org.zhao.mcs.util.ThreadPoolUtils;

/**
 * 推送信息 
 * @author zhao
 *
 */
public class RegiestClientFullDataConfig implements SchedulingConfigurer{

	private static String cron ; 

	public RegiestClientFullDataConfig() {
		cron = ConfigProperties.instance().getPropertiesVal("server.mcs.put.cron");
	}
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.addTriggerTask(new Runnable() {

            @Override
            public void run() {
            	ThreadPoolUtils.putThread("服务器状态记录", new PutThread());
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
