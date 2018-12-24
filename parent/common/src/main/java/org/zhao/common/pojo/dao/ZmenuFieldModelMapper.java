package org.zhao.common.pojo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.zhao.common.pojo.model.ZmenuFieldModel;

public interface ZmenuFieldModelMapper extends MapperBase<ZmenuFieldModel>{

	@Select("SELECT m.* FROM r_menu_field m WHERE parent_menu_id = #{menuId} AND EXISTS(SELECT 1 FROM r_role_power rp WHERE rp.power_id = m.id AND rp.power_type = '3' AND role_id = #{roleId}) ORDER BY orders ")
	@Results(value={
		@Result(column="formatter",property="formatter",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="field_name",property="fieldName",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="parent_menu_id",property="parentMenuId",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="field_key",property="fieldKey",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="width",property="width",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="orders",property="orders",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="align",property="align",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR)
	})
	List<ZmenuFieldModel> selectFieldByRoleAndMenuId(Map<String,Object> map);
	
	@Select("SELECT m.* FROM r_menu_field m WHERE parent_menu_id = #{id}  ORDER BY orders ")
	@ResultMap("BaseResultMap")
	List<ZmenuFieldModel> selectFieldByMenuId(@Param("id")String menuId);
    /**
     @Results(value={
		@Result(column="formatter",property="formatter",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="field_name",property="fieldName",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="parent_menu_id",property="parentMenuId",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="field_key",property="fieldKey",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="width",property="width",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="orders",property="orders",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="align",property="align",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR)
	 })
     */

}