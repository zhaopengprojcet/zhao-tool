package org.zhao.common.aspect.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minidev.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.pojo.model.ZrequestUseModel;
import org.zhao.common.properties.ConfigProperties;
import org.zhao.common.util.CacheUtil;
import org.zhao.common.util.DateUtil;

/**
 * 记录全局访问请求的平均耗时
 * @author zhao
 *
 */
public class QueryTimeUse {

	private final static String USE_TIME_CACHE = "use_time_cache";
	private final static String REQUEST_COUNT_CACHE = "request_count_cache";
	
	/**
	 * 请求耗时
	 */
	private static  Map<String, Long> useTimeTable = new LinkedHashMap<String, Long>();
	/**
	 * 请求次数
	 */
	private static  Map<String, Long> requestCountTable = new HashMap<String, Long>();
	
	public static List<ZrequestUseModel> getSaveList() {
		synchronized (useTimeTable) {
			Map<String, Long> useTime = (Map<String, Long>) CacheUtil.getMapCache(USE_TIME_CACHE, "*");
			useTimeTable.putAll(useTime);	
			Map<String, Long> requestCount = (Map<String, Long>) CacheUtil.getMapCache(REQUEST_COUNT_CACHE, "*");
			requestCountTable.putAll(requestCount);
			List<ZrequestUseModel> list = new ArrayList<ZrequestUseModel>();
			for (Map.Entry<String, Long> entry : useTimeTable.entrySet()) { 
				ZrequestUseModel model = new ZrequestUseModel();
				model.setCount(requestCountTable.get(entry.getKey()) == null ? 0 : requestCountTable.get(entry.getKey()).intValue());
				model.setName(entry.getKey());
				model.setRequestTime(DateUtil.getTimeStr(new Date(), DateUtil.yyyy_MM_dd));
				model.setTimes(entry.getValue() == null ? 0 : entry.getValue().longValue());
				list.add(model);
			}
			return list;
		}
	}
	
	public static void clearAll() {
		synchronized (useTimeTable) {
			CacheUtil.removeCache(USE_TIME_CACHE);
			CacheUtil.removeCache(REQUEST_COUNT_CACHE);
			useTimeTable = new LinkedHashMap<String, Long>();
			requestCountTable = new HashMap<String, Long>();
		}
	}
	
	public synchronized static void putTime(String name , long time) {
		if(CacheUtil.getMapCache(USE_TIME_CACHE, name) != null) {
			Long tm = (Long) CacheUtil.getMapCache(USE_TIME_CACHE, name);
			Long count =  (Long) CacheUtil.getMapCache(REQUEST_COUNT_CACHE, name);
			CacheUtil.saveMapCache(USE_TIME_CACHE, name, (tm + time) / 2L);
			CacheUtil.saveMapCache(REQUEST_COUNT_CACHE, name, count+1L);
		}
		else {
			CacheUtil.saveMapCache(USE_TIME_CACHE, name, time);
			CacheUtil.saveMapCache(REQUEST_COUNT_CACHE, name, 1L);
		}
	}
	
	public static void reloadTable() {
		Map<String, Long> useTime = (Map<String, Long>) CacheUtil.getMapCache(USE_TIME_CACHE, "*");
		if(useTime != null)useTimeTable.putAll(useTime);	
		Map<String, Long> requestCount = (Map<String, Long>) CacheUtil.getMapCache(REQUEST_COUNT_CACHE, "*");
		if(requestCount != null)requestCountTable.putAll(requestCount);
	}
	
	public static String getTimeEasyUiData(String queryKey ,PageContext page) {
		synchronized (useTimeTable) {
			List<Map.Entry<String,Long>> list = new ArrayList<Map.Entry<String,Long>>(useTimeTable.entrySet());
	        Collections.sort(list,new Comparator<Map.Entry<String,Long>>() {
	            //升序排序
	            public int compare(Entry<String, Long> o1,
	                    Entry<String, Long> o2) {
	                return o2.getValue().compareTo(o1.getValue());
	            }
	            
	        });
			
			int count = useTimeTable.keySet().size();
			
			JSONArray array = new JSONArray();
			int index = 0;
			if(count > 0) {
				int begin = page.getStart().intValue();
				int end = begin + page.getCount().intValue() - 1;
				for(Map.Entry<String,Long> mapping:list){ 
					String key = mapping.getKey(); 
					if(!StringUtils.isEmpty(queryKey)) {
						if(key.indexOf(queryKey) == -1) continue;
					}
					long val = mapping.getValue();
					if(index >= begin && index <= end) {
						JSONObject ob = new JSONObject();
						ob.put("name", key);
						ob.put("times", val);
						ob.put("count", requestCountTable.get(key).longValue());
						array.add(ob);
					}
					index++;
		        } 
			}
			JSONObject result = new JSONObject();
			result.put("total", index);
			result.put("rows", array);
			return result.toString();
		}
	}
}
