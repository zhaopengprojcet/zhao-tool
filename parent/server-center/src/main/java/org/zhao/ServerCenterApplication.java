package org.zhao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value="org.zhao.common.pojo.dao")
/// memonycpu
//@EnableScheduling
public class ServerCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerCenterApplication.class, args);
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
