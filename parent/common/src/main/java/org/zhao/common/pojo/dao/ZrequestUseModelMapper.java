package org.zhao.common.pojo.dao;

import org.zhao.common.pojo.model.ZrequestUseModel;
public interface ZrequestUseModelMapper extends MapperBase<ZrequestUseModel>{

    /**
     @Results(value={
		@Result(column="request_time",property="requestTime",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="times",property="times",javaType=java.lang.Double.class,jdbcType=JdbcType.DOUBLE),
		@Result(column="name",property="name",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="count",property="count",javaType=java.lang.Integer.class,jdbcType=JdbcType.INTEGER),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR)
	 })
     */

}