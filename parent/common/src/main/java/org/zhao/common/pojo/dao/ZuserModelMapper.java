package org.zhao.common.pojo.dao;

import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Update;
import org.zhao.common.pojo.model.ZuserModel;

public interface ZuserModelMapper extends MapperBase<ZuserModel>{

	@Update("UPDATE R_USER SET login_error_count = login_error_count + 1 WHERE login_name = #{name} ")
	@ResultType(Integer.class)
	int addErrorLoginCount(String name);
    /**
     @Results(value={
		@Result(column="last_login_time",property="lastLoginTime",javaType=java.util.Date.class,jdbcType=JdbcType.TIMESTAMP),
		@Result(column="password",property="password",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="info_id",property="infoId",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="create_time",property="createTime",javaType=java.util.Date.class,jdbcType=JdbcType.TIMESTAMP),
		@Result(column="user_state",property="userState",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="role_id",property="roleId",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="login_name",property="loginName",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="role_name",property="roleName",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="login_error_count",property="loginErrorCount",javaType=java.lang.Integer.class,jdbcType=JdbcType.INTEGER)
	 })
     */

}