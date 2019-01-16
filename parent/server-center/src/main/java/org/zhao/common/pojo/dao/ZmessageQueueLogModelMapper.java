package org.zhao.common.pojo.dao;

import org.zhao.common.pojo.model.ZmessageQueueLogModel;
public interface ZmessageQueueLogModelMapper extends MapperBase<ZmessageQueueLogModel>{

    /**
     @Results(value={
		@Result(column="last_return_context",property="lastReturnContext",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="put_client_ip",property="putClientIp",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="put_client_port",property="putClientPort",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="mq_return_context",property="mqReturnContext",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="last_pu_time",property="lastPuTime",javaType=java.util.Date.class,jdbcType=JdbcType.TIMESTAMP),
		@Result(column="message_id",property="messageId",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="put_client_name",property="putClientName",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR)
	 })
     */

}