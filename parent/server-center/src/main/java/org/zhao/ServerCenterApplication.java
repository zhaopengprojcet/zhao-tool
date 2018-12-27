package org.zhao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.zhao.schedule.load.ServletScheduleLoadInit;

@SpringBootApplication
@MapperScan(value="org.zhao.common.pojo.dao")
/// memonycpu
//@EnableScheduling
public class ServerCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerCenterApplication.class, args);
	}

	@Bean
	public ServletScheduleLoadInit getSchedule() {
		return new ServletScheduleLoadInit();
	}
	
	/// usetime
	/*@Bean
	public RequestAfterAspect get() {
		return new RequestAfterAspect();
	}*/
	
	/// memonycpu
	/*@Bean
	public RegiestClientFullDataConfig getConfig() {
		return new RegiestClientFullDataConfig();
	}*/
	
}
