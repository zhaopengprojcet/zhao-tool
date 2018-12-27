package org.zhao.common.aspect.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zhao.common.client.ClientContext;
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.server.ServerConfig;
import org.zhao.common.util.CacheUtil;

/**
 * 计算机异常 处理线程
 * @author zhao
 *
 */
public class QuerySchedules extends Thread{
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	private List<String> datas;
	private String token;
	
	public QuerySchedules(){}
	public QuerySchedules(List<String> data ,  String token) {
		this.datas = data;
		this.token = token;
	}
	
	@Override
	public void run() {
		if(StringUtils.isEmpty(token)) callSchedules();
		else regiestSchedules();
	}
	
	
	private static final String SCHEDULE_CLIENTS = "schedule_clients";
	
	/**
	 * 获取当前已注册定时任务列表 非同步，需要主动刷新
	 * @param scheduleName
	 * @param serverName
	 * @param page
	 * @return
	 */
	public static String getPageList(String scheduleName ,String serverName , PageContext page) {
		Map<String, ClientContext> clients = (Map<String, ClientContext>) CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, "*");
		int index = 0;
		JSONArray array = new JSONArray();
		for(String key:localServices.keySet()) {
			if(!StringUtils.isEmpty(scheduleName) && key.indexOf(scheduleName) == -1) continue;
			JSONObject obj = new JSONObject();
			obj.put("scheduleName", key);
			List<String> names = new ArrayList<String>();
			List<String> tokens = localServices.get(key);
			boolean sefd = false;
			for (String string : tokens) {
				if(!StringUtils.isEmpty(serverName) && clients.get(string).getServiceName().indexOf(serverName) != -1) sefd = true;
				names.add(clients.get(string).getServiceName());
			}
			if(!StringUtils.isEmpty(serverName) && !sefd) continue;
			obj.put("serverNames", StringUtils.join(names, ","));
			if(index >= page.getStart().intValue() && index <= (page.getStart().intValue() + page.getCount().intValue())) {
				array.add(obj);
			}
			index ++;
		}
		JSONObject result = new JSONObject();
		result.put("total", index);
		result.put("rows", array);
		return result.toString();
	}
	
	private static LinkedHashMap<String, List<String>> localServices = new LinkedHashMap<String, List<String>>();
	
	/**
	 * 重新将缓存中最新的注册服务加载到查询列表中
	 */
	public static void reloadSchedules() {
		synchronized(localServices) {
			Map<String, List<String>> cache = (Map<String, List<String>>) CacheUtil.getMapListCache(SCHEDULE_CLIENTS, "*");
			localServices = new LinkedHashMap<String, List<String>>();
			for (String key : cache.keySet()) {
				localServices.put(key, cache.get(key));
			}
		}
	}
	
	//注册
	private void regiestSchedules() {
		if(datas == null) return;
		logger.info("开始注册定时任务【"+JSONArray.fromObject(datas).toString()+"】【"+token+"】");
		Map<String, List<String>> services = (Map<String, List<String>>) CacheUtil.getMapListCache(SCHEDULE_CLIENTS, "*");
		if(services != null) {
			//新增修改   限原有key
			for (String key : services.keySet()) { //遍历原有缓存 删除新注册中没有的,添加在新注册中有的
				List<String> tks = (List<String>) CacheUtil.getMapListCache(SCHEDULE_CLIENTS, key);
				if(datas.contains(key)) { //原有注册
					if(!tks.contains(token)) tks.add(token); //新
						CacheUtil.saveMapListCache(SCHEDULE_CLIENTS, key, tks);
				}
				else {
					if(tks.contains(token)) {
						tks.remove(token);
						CacheUtil.saveMapListCache(SCHEDULE_CLIENTS, key, tks);
					}
				}
			}
			//新增
			for (String key : datas) { //遍历新注册，将原缓存中未有的加入
				if(!services.keySet().contains(key)) {
					List<String> tks = new ArrayList<String>();
					tks.add(token);
					CacheUtil.saveMapListCache(SCHEDULE_CLIENTS, key, tks);
				}
			}
		}
		else {
			for (String key : datas) { //遍历新注册，将原缓存中未有的加入
				List<String> tks = new ArrayList<String>();
				tks.add(token);
				CacheUtil.saveMapListCache(SCHEDULE_CLIENTS, key, token);
			}
		}
		services = (Map<String, List<String>>) CacheUtil.getMapListCache(SCHEDULE_CLIENTS, "*");
		logger.info("注册完成，定时任务注册列表【"+JSONObject.fromObject(services).toString()+"】");
	}
	
	//调用
	private void callSchedules() {
		
	}
	
}
