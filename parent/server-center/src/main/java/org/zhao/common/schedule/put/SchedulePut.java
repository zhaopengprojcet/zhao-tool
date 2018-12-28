package org.zhao.common.schedule.put;

import org.springframework.beans.factory.annotation.Autowired;
import org.zhao.common.aspect.query.QueryTimeUse;
import org.zhao.common.mybatis.query.QueryParames;
import org.zhao.schedule.annotation.ScheduleMethod;
import org.zhao.schedule.annotation.zhaoScheduleBean;
import org.zhao.schedule.model.ReturnCode;
import org.zhao.service.ZuseTimeService;

//@zhaoScheduleBean
public class SchedulePut {

	@Autowired
	private ZuseTimeService zUseTimeService;
	
	@ScheduleMethod(scheduleName = "put_time_save")
	public String savePutTime() {
		try {
			/*zUseTimeService.save(QueryTimeUse.getSaveList());
			QueryTimeUse.clearAll();*/
			System.out.println("put_time_save 执行");
			zUseTimeService.selectPageListByParameterRequire(null, QueryParames.init().getParames());
			System.out.println("put_time_save 执行结束");
		} catch (Exception e) {
			e.printStackTrace();
			return ReturnCode.ERROR;
		}
		return ReturnCode.SUCCESS;
	}
}
