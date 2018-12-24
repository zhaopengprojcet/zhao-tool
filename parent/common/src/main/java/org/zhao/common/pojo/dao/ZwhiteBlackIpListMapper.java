package org.zhao.common.pojo.dao;

import org.zhao.common.pojo.model.ZwhiteBlackIpList;
public interface ZwhiteBlackIpListMapper extends MapperBase<ZwhiteBlackIpList>{

    /**
     @Results(value={
		@Result(column="bw_type",property="bwType",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="add_desc",property="addDesc",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="create_time",property="createTime",javaType=java.util.Date.class,jdbcType=JdbcType.TIMESTAMP),
		@Result(column="ip",property="ip",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR)
	 })
     */

}