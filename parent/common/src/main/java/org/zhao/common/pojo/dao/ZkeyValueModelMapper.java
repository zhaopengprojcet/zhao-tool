package org.zhao.common.pojo.dao;

import org.zhao.common.pojo.model.ZkeyValueModel;
public interface ZkeyValueModelMapper extends MapperBase<ZkeyValueModel>{

    /**
    @Results(value={
		@Result(column="server_key",property="serverKey",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="server_value",property="serverValue",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="serverey_desc",property="servereyDesc",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR)
	 })
     */

}