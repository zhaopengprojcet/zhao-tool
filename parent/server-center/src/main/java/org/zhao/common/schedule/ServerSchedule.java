package org.zhao.common.schedule;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zhao.common.aspect.query.QueryTimeUse;
import org.zhao.service.ZuseTimeService;

@Component
public class ServerSchedule {

	@Autowired
	private ZuseTimeService zUseTimeService;
	@Autowired
	private ServletContext servletContext;
	
	@Scheduled(cron="0 0 1 * * ? ")
	public void insertRequestUse() {
		this.zUseTimeService.save(QueryTimeUse.getSaveList());
			QueryTimeUse.clearAll();
	}
}
