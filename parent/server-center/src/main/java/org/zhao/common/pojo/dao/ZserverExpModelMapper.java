package org.zhao.common.pojo.dao;

import org.zhao.common.pojo.model.ZserverExpModel;
public interface ZserverExpModelMapper extends MapperBase<ZserverExpModel>{

    /**
     @Results(value={
		@Result(column="query_server_name",property="queryServerName",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="query_ip",property="queryIp",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="create_time",property="createTime",javaType=java.util.Date.class,jdbcType=JdbcType.TIMESTAMP),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="exp_desc",property="expDesc",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="exp_state",property="expState",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR)
	 })
     */

}