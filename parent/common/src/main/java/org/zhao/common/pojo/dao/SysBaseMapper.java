package org.zhao.common.pojo.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface SysBaseMapper {

	/**
	 * 查询表是否存在
	 * @param tableName
	 * @return
	 */
	@Select("show tables like #{tableName}")
	@ResultType(HashMap.class)
	Map<String, String> checkTable(@Param("tableName")String tableName);
	
	/**
	 * 查询表字段信息
	 * @param tableName
	 * @return
	 */
	@Select("select COLUMN_NAME,IS_NULLABLE,DATA_TYPE,COLUMN_KEY,COLUMN_COMMENT,CHARACTER_MAXIMUM_LENGTH from information_schema.columns where table_name=#{tableName} ;")
	@ResultType(HashMap.class)
	List<Map<String, Object>> selectTableColumns(@Param("tableName")String tableName);
	
	/**
	 * 查询表索引信息
	 * @param tableName
	 * @return
	 */
	@Select("show index from ${tableName} ")
	@ResultType(HashMap.class)
	List<Map<String, String>> selectTableIndex(@Param("tableName")String tableName);
	
	/**
	 * 直接执行的sql
	 * @param sql
	 * @return
	 */
	@Update("${sql}")
	void updateDDL(@Param("sql")String sql);
}
