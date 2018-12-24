package org.zhao.common.databean.service;

import java.util.List;
import java.util.Map;

import org.zhao.common.databean.model.DataColumObject;

public interface SysService {

	boolean checkTable(String name);//查询表是否存在
	
	Map<String,DataColumObject> tableColums(String name);//查询表结构字段信息
	
	void ddlSql(List<String> sql); //直接执行sql
}
