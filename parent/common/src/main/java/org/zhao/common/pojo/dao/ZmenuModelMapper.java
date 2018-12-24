package org.zhao.common.pojo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.zhao.common.pojo.model.ZmenuModel;

public interface ZmenuModelMapper extends MapperBase<ZmenuModel>{

	/**
	 * 查询角色下属 菜单权限键、按钮权限键、字段编号集合
	 * @param roleId
	 * @return
	 */
	@Select("SELECT m.power_key FROM r_menu m WHERE menu_state = '1' AND EXISTS(SELECT 1 FROM r_role_power rp WHERE rp.power_id = m.id AND rp.power_type = '1' AND role_id = #{roleId}) "
		   +" UNION ALL "
		   +"SELECT m.power_key FROM r_menu_button m WHERE button_state = '1' AND EXISTS(SELECT 1 FROM r_role_power rp WHERE rp.power_id = m.id AND rp.power_type = '2' AND role_id = #{roleId})"
		   +" UNION ALL "
		   +"SELECT m.id FROM r_menu_field m WHERE EXISTS(SELECT 1 FROM r_role_power rp WHERE rp.power_id = m.id AND rp.power_type = '3' AND role_id = #{roleId}) "
			)
	@ResultType(String.class)
	List<String> selectMeunAndInfosPowerKeyByRole(@Param("roleId")String roleId);
	
	/**
	 * 查询权限下 可展示的菜单列表
	 * @param roleId
	 * @return
	 */
	@Select("SELECT m.* FROM r_menu m WHERE menu_state = '1' AND EXISTS(SELECT 1 FROM r_role_power rp WHERE rp.power_id = m.id AND rp.power_type = '1' AND role_id = #{roleId}) ORDER BY orders ")
	@Results(value={
		@Result(column="parent_menu_id",property="parentMenuId",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="menu_name",property="menuName",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="menu_herf",property="menuHerf",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR)
	})
	List<ZmenuModel> selectMenuListByRoleId(@Param("roleId")String roleId);
	
	/**
	 * 角色下属全部资源
	 * @param roleId
	 * @return
	 */
	@Select("SELECT m.*,#{roleId} as role_id FROM r_menu m WHERE menu_state = '1' AND EXISTS(SELECT 1 FROM r_role_power rp WHERE rp.power_id = m.id AND rp.power_type = '1' AND role_id = #{roleId}) ORDER BY orders ")
	@Results(value={
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="list_type",property="listType",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="list_inner",property="listInner",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="{roleId=role_id,menuId=id}",property="buttons",many=@Many(select="org.zhao.common.pojo.dao.ZmenuButtonModelMapper.selectButtonByRoleAndMenuId")),
		@Result(column="{roleId=role_id,menuId=id}",property="fields",many=@Many(select="org.zhao.common.pojo.dao.ZmenuFieldModelMapper.selectFieldByRoleAndMenuId")),
		@Result(column="id",property="searchs",many=@Many(select="org.zhao.common.pojo.dao.ZmenuSearchModelMapper.selectListByMenuId"))
	})
	List<ZmenuModel> selectResourcesByRoleId(@Param("roleId")String roleId);
	/**
	 * 查询全部菜单资源 以树状图存在
	 * @return
	 */
	@Select("SELECT * FROM r_menu WHERE parent_menu_id = #{pId} ")
	@Results(value={
		@Result(column="parent_menu_id",property="parentMenuId",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="menu_state",property="menuState",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="power_key",property="powerKey",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="menu_name",property="menuName",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="orders",property="orders",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="menu_herf",property="menuHerf",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR) ,
		@Result(column="list_type",property="listType",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="list_inner",property="listInner",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="id" , property = "children" , many=@Many(select="org.zhao.common.pojo.dao.ZmenuModelMapper.selectMenuListOfTree"))
	})
	List<ZmenuModel> selectMenuListOfTree(@Param("pId")String pId);
	
	/**
	 * 权限设置使用
	 * @param pId
	 * @return
	 */
	@Select("SELECT * FROM r_menu WHERE parent_menu_id = #{pId} ")
	@Results(value={
		@Result(column="menu_name",property="menuName",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="id" , property = "children" , many=@Many(select="org.zhao.common.pojo.dao.ZmenuModelMapper.selectAllResourcesList")),
		@Result(column="id",property="buttons",many=@Many(select="org.zhao.common.pojo.dao.ZmenuButtonModelMapper.selectButtonByMenuId")),
		@Result(column="id",property="fields",many=@Many(select="org.zhao.common.pojo.dao.ZmenuFieldModelMapper.selectFieldByMenuId")),
	})
	List<ZmenuModel> selectAllResourcesList(@Param("pId")String pId);
	
	@Select("SELECT * FROM r_menu WHERE parent_menu_id = #{pId} ")
	@Results(value={
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="menu_name",property="menuName",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="id" , property = "children" , many=@Many(select="org.zhao.common.pojo.dao.ZmenuModelMapper.selectcomboListMenuOfTree"))
	})
	List<ZmenuModel> selectcomboListMenuOfTree(@Param("pId")String pId);
    /**
    @Results(value={
		@Result(column="parent_menu_id",property="parentMenuId",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="menu_state",property="menuState",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="power_key",property="powerKey",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="list_inner",property="listInner",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="menu_name",property="menuName",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="orders",property="orders",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="id",property="id",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="menu_herf",property="menuHerf",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR),
		@Result(column="list_type",property="listType",javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR)
	 })
     */

}