package org.zhao.common.pojo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.zhao.common.pojo.model.ZmenuButtonModel;

public interface ZmenuButtonModelMapper extends MapperBase<ZmenuButtonModel>{

	@Select("SELECT m.* FROM r_menu_button m WHERE button_state = '1' AND form_menu_id = #{menuId} AND EXISTS(SELECT 1 FROM r_role_power rp WHERE rp.power_id = m.id AND rp.power_type = '2' AND role_id = #{roleId}) ORDER BY orders ")
	 @Results(value={
		@Result(column="button_name",property="buttonName",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="button_state",property="buttonState",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="icos",property="icos",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="power_key",property="powerKey",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="model_title",property="modelTitle",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="button_function",property="buttonFunction",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="model_height",property="modelHeight",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="form_menu_id",property="formMenuId",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="orders",property="orders",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="model_url",property="modelUrl",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="model_width",property="modelWidth",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR)
	})
	List<ZmenuButtonModel> selectButtonByRoleAndMenuId(Map<String,Object> map);
	
	@Select("SELECT m.* FROM r_menu_button m WHERE  form_menu_id = #{id} ORDER BY orders ")
	@ResultMap("BaseResultMap")
	List<ZmenuButtonModel> selectButtonByMenuId(@Param("id")String menuId);
    /**
     @Results(value={
		@Result(column="button_name",property="buttonName",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="power_key",property="powerKey",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="model_title",property="modelTitle",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="button_state",property="buttonState",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="button_function",property="buttonFunction",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="model_height",property="modelHeight",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="form_menu_id",property="formMenuId",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="orders",property="orders",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="model_url",property="modelUrl",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="model_width",property="modelWidth",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="icos",property="icos",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR)
	 })
     */

}