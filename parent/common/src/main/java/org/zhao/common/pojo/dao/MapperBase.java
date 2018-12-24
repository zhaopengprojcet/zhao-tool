package org.zhao.common.pojo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.zhao.common.mybatis.query.PageContext;

public abstract interface MapperBase<T> {

	  public int deleteByPrimaryKey(String paramString);

	  public int deleteByParames(@Param("record") T paramT);
	
	  public int insert(@Param("record")T paramT);
	
	  public int insertSelective(@Param("record")T paramT);
	
	  public int batchCreate(@Param("list") List<T> paramList);
	
	  public T selectByPrimaryKey(@Param("id") String paramString);
	
	  public int updateByPrimaryKeySelective(@Param("record")T paramT);
	
	  public int updateByPrimaryKey(@Param("record")T paramT);
	
	  public List<T> selectPageListByParameters(@Param("record") T paramT, @Param("page") PageContext pageContext);
	
	  public int selectPageListByParametersCount(@Param("record") T paramT);
	
	  public List<T> selectPageListByParameterRequire(@Param("record") T paramT, @Param("page") PageContext pageContext, @Param("require") Map<String, Map<String, String>> paramMap);
	
	  public int selectPageListByParameterRequireCount(@Param("record") T paramT, @Param("require") Map<String, Map<String, String>> paramMap);
}
