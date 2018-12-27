package org.zhao.common.pojo.model;

import java.util.Date;

import org.zhao.common.databean.annotation.DataBean;

//@DataBean(tableName="R_SCHEDULE_REGIEST")
public class ZscheduleSetModel {

	private String id;
	private String scheduleType;
	private String scheduleState;
	private String scheduleCron;
	private Date createTime;
	private String scheduleDesc;
}
