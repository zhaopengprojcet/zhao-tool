package org.zhao.common.mybatis.query;

import java.util.HashMap;
import java.util.Map;

/**
 * 新版 mybatis 查询条件处理
 * @author zhao
 *
 */
public class QueryParames {

	public static QueryParames init() {
		return new QueryParames();
	}
	
	private Map<String, Map<String, String>> parames = new HashMap<String, Map<String,String>>();
	
	public Map<String, Map<String, String>> getParames() {
		return parames;
	}
	
	/**
	 * 大于
	 * create by j_zhao
	 *
	 */
	public void addGreaterThan(String colom , String data) {
		
		Map<String, String> datas;
		if(!parames.containsKey(colom)) {
			datas = new HashMap<String, String>();
		}
		else {
			datas = parames.get(colom);
		}
		datas.put("greateThan", data);
		parames.put(colom, datas);
	}
	/**
	 * 小于
	 * create by j_zhao
	 *
	 */
	public void addLessThan(String colom , String data) {
		
		Map<String, String> datas;
		if(!parames.containsKey(colom)) {
			datas = new HashMap<String, String>();
		}
		else {
			datas = parames.get(colom);
		}
		datas.put("lessThan", data);
		parames.put(colom, datas);
	}
	/**
	 * 大于等于
	 * create by j_zhao
	 *
	 */
	public void addGreaterThanAndEquals(String colom , String data) {
		
		Map<String, String> datas;
		if(!parames.containsKey(colom)) {
			datas = new HashMap<String, String>();
		}
		else {
			datas = parames.get(colom);
		}
		datas.put("greaterThanAndEquals", data);
		parames.put(colom, datas);
	}
	/**
	 * 小于等于
	 * create by j_zhao
	 *
	 */
	public void addLessThanAndEquals(String colom , String data) {
		
		Map<String, String> datas;
		if(!parames.containsKey(colom)) {
			datas = new HashMap<String, String>();
		}
		else {
			datas = parames.get(colom);
		}
		datas.put("lessThanAndEquals", data);
		parames.put(colom, datas);
	}
	/**
	 * 不等于
	 * create by j_zhao
	 *
	 */
	public void addNotEquality(String colom , String data) {
		
		Map<String, String> datas;
		if(!parames.containsKey(colom)) {
			datas = new HashMap<String, String>();
		}
		else {
			datas = parames.get(colom);
		}
		datas.put("notEquality", data);
		parames.put(colom, datas);
	}
	
	/**
	 * 等于
	 * create by j_zhao
	 *
	 */
	public void addEquality(String colom , String data) {
		
		Map<String, String> datas;
		if(!parames.containsKey(colom)) {
			datas = new HashMap<String, String>();
		}
		else {
			datas = parames.get(colom);
		}
		datas.put("equality", data);
		parames.put(colom, datas);
	}
	
	/**
	 * 模糊等于
	 * create by j_zhao
	 *
	 */
	public void addSimilar(String colom , String data) {
		
		Map<String, String> datas;
		if(!parames.containsKey(colom)) {
			datas = new HashMap<String, String>();
		}
		else {
			datas = parames.get(colom);
		}
		datas.put("similar", data);
		parames.put(colom, datas);
	}
}
