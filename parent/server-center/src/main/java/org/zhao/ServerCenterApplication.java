package org.zhao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.zhao.mcs.service.RegiestClientFullDataConfig;
import org.zhao.mq.load.MqServletScheduleLoadInit;
import org.zhao.schedule.load.ScheduleServletScheduleLoadInit;
import org.zhao.usetime.aspect.RequestAfterAspect;

@SpringBootApplication
@MapperScan(value="org.zhao.common.pojo.dao")
/// memonycpu
@EnableScheduling
public class ServerCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerCenterApplication.class, args);
	}

	//schedule
	//另需要在拦截器中放行/schedule/response.html用于任务调度响应接口
	//后期如果使用其他嵌入容器，可以简化
	@Bean
	public ScheduleServletScheduleLoadInit getSchedule() {
		return new ScheduleServletScheduleLoadInit();
	}
	
	//mq
	//另需要在拦截器中放行/mq/response.html用于任务调度响应接口
	//后期如果使用其他嵌入容器，可以简化
	@Bean
	public MqServletScheduleLoadInit getmq() {
		return new MqServletScheduleLoadInit();
	}
	
	/// usetime
	@Bean
	public RequestAfterAspect get() {
		return new RequestAfterAspect();
	}
	
	/// memonycpu
	@Bean
	public RegiestClientFullDataConfig getConfig() {
		return new RegiestClientFullDataConfig();
	}
	
}
