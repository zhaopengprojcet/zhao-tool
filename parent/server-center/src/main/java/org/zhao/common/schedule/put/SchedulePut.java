package org.zhao.common.schedule.put;

import org.springframework.beans.factory.annotation.Autowired;
import org.zhao.common.aspect.query.QueryTimeUse;
import org.zhao.common.mybatis.query.QueryParames;
import org.zhao.schedule.annotation.ScheduleMethod;
import org.zhao.schedule.annotation.zhaoScheduleBean;
import org.zhao.schedule.model.ReturnCode;
import org.zhao.service.ZuseTimeService;

/**
 * 耗时数据持久化
 * @author zhao
 *
 */
//@zhaoScheduleBean
public class SchedulePut {

	@Autowired
	private ZuseTimeService zUseTimeService;
	
	@ScheduleMethod(scheduleName = "put_time_save")
	public String savePutTime() {
		try {
			zUseTimeService.save(QueryTimeUse.getSaveList());
			QueryTimeUse.clearAll();
		} catch (Exception e) {
			e.printStackTrace();
			return ReturnCode.error(e.getLocalizedMessage());
		}
		return ReturnCode.SUCCESS;
	}
}
