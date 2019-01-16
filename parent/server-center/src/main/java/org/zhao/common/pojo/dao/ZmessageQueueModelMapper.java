package org.zhao.common.pojo.dao;

import org.zhao.common.pojo.model.ZmessageQueueModel;
public interface ZmessageQueueModelMapper extends MapperBase<ZmessageQueueModel>{

    /**
     @Results(value={
		@Result(column="send_port",property="sendPort",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="send_server_name",property="sendServerName",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="msg_state",property="msgState",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="context",property="context",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="put_service_name",property="putServiceName",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="send_ip",property="sendIp",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="send_time",property="sendTime",javaType=java.util.Date.class,jdbcType=JdbcType.TIMESTAMP),
		@Result(column="push_type",property="pushType",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR)
	 })
     */

}