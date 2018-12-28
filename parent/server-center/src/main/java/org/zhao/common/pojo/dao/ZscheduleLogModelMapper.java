package org.zhao.common.pojo.dao;

import org.zhao.common.pojo.model.ZscheduleLogModel;
public interface ZscheduleLogModelMapper extends MapperBase<ZscheduleLogModel>{

    /**
     @Results(value={
		@Result(column="schedule_name",property="scheduleName",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="put_state",property="putState",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="do_end_time",property="doEndTime",javaType=java.util.Date.class,jdbcType=JdbcType.TIMESTAMP),
		@Result(column="put_server",property="putServer",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="do_state",property="doState",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="put_time",property="putTime",javaType=java.util.Date.class,jdbcType=JdbcType.TIMESTAMP),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR)
	 })
     */

}