package org.zhao.common.server;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(5)
public class ServerLoad implements ApplicationRunner {

	private Logger logger = Logger.getLogger(ServerLoad.class);
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		ServerConfig.setServerPaths();
		logger.info("注册中心地址【"+JSONArray.fromObject(ServerConfig.getServerPaths()).toString()+"】");
	}
}
