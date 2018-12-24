package org.zhao.common.pojo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.zhao.common.pojo.model.ZmenuSearchModel;

public interface ZmenuSearchModelMapper extends MapperBase<ZmenuSearchModel>{

	@Select("SELECT * FROM r_menu_search WHERE parent_menu_id = #{menuId} AND search_state = '1' ")
	@ResultMap("BaseResultMap")
	List<ZmenuSearchModel> selectListByMenuId(@Param("menuId")String menuId);
	
	
	@Select("SELECT * FROM r_menu_search WHERE parent_menu_id = #{id} ")
	@ResultMap("BaseResultMap")
	List<ZmenuSearchModel> selectAllListByMenuId(@Param("id")String menuId);
	 /**
  @Results(value={
		@Result(column="load_value",property="loadValue",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="search_text",property="searchText",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="load_value_url",property="loadValueUrl",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="parent_menu_id",property="parentMenuId",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="search_type",property="searchType",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="orders",property="orders",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="search_key",property="searchKey",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="search_state",property="searchState",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="value_field",property="valueField",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="text_field",property="textField",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR)
	 })
    */

}