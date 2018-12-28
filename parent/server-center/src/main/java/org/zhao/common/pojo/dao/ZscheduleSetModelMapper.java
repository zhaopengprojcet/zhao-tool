package org.zhao.common.pojo.dao;

import org.zhao.common.pojo.model.ZscheduleSetModel;
public interface ZscheduleSetModelMapper extends MapperBase<ZscheduleSetModel>{

	 /**
    @Results(value={
		@Result(column="schedule_name",property="scheduleName",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="schedule_state",property="scheduleState",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="schedule_type",property="scheduleType",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="create_time",property="createTime",javaType=java.util.Date.class,jdbcType=JdbcType.TIMESTAMP),
		@Result(column="schedule_key",property="scheduleKey",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="schedule_cron",property="scheduleCron",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="push_type",property="pushType",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR)
	 })
    */


}