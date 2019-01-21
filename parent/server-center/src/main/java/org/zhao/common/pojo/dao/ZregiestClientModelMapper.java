package org.zhao.common.pojo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.pojo.model.ZregiestClientModel;
public interface ZregiestClientModelMapper extends MapperBase<ZregiestClientModel>{

	List<ZregiestClientModel> selectPageListByParameterRequireNoPass(@Param("page") PageContext pageContext, @Param("require") Map<String, Map<String, String>> paramMap);
    /**
     @Results(value={
		@Result(column="client_desc",property="clientDesc",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="pass",property="pass",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="create_time",property="createTime",javaType=java.util.Date.class,jdbcType=JdbcType.TIMESTAMP),
		@Result(column="name",property="name",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="use_time",property="useTime",javaType=java.util.Date.class,jdbcType=JdbcType.TIMESTAMP),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="state",property="state",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="powers",property="powers",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="use_ips",property="useIps",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR)
	 })
     */

}