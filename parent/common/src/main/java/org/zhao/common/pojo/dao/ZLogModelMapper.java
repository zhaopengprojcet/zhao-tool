package org.zhao.common.pojo.dao;

import org.zhao.common.pojo.model.ZLogModel;
public interface ZLogModelMapper extends MapperBase<ZLogModel>{

    /**
     @Results(value={
		@Result(column="log_type",property="logType",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="log_desc",property="logDesc",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="create_time",property="createTime",javaType=java.util.Date.class,jdbcType=JdbcType.TIMESTAMP),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="log_key",property="logKey",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR)
	 })
     */

}