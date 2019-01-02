package org.zhao.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.data.redis.core.RedisTemplate;
import org.zhao.common.interceptor.PublicServerKV;

public class CacheUtil {

	public static Object context;
	
	public static ServletContext servletContext;
	
	public static RedisTemplate redisTemplate;
	
	/**
	 * 删除键
	 * @param key
	 * @param context
	 */
	public static void removeCache(String key , Object... cont) {
		Object ct = null;
		if(cont != null && cont.length > 0) {
			ct = cont[0];
		}
		else {
			ct = context;
		}
		if(ct instanceof HttpSession) {
			HttpSession session = (HttpSession) ct;
			session.removeAttribute(key);
		}
		else if(ct instanceof ServletContext) {
			ServletContext servlet = (ServletContext) ct;
			servlet.removeAttribute(key);
		}
		else if(ct instanceof RedisTemplate) {
			RedisTemplate redis = (RedisTemplate) ct;
			redis.delete(key);
		}
	}
	
	/**
	 * 删除键
	 * @param key
	 * @param context
	 */
	public static void removeMapCache(String key ,String hashKey , Object... cont) {
		Object ct = null;
		if(cont != null && cont.length > 0) {
			ct = cont[0];
		}
		else {
			ct = context;
		}
		if(ct instanceof HttpSession) {
			HttpSession session = (HttpSession) ct;
			Map<String, Object> map = (Map<String, Object>) session.getAttribute(key);
			map.remove(hashKey);
			session.setAttribute(key, map);
		}
		else if(ct instanceof ServletContext) {
			ServletContext servlet = (ServletContext) ct;
			Map<String, Object> map = (Map<String, Object>) servlet.getAttribute(key);
			map.remove(hashKey);
			servlet.setAttribute(key, map);
		}
		else if(ct instanceof RedisTemplate) {
			RedisTemplate redis = (RedisTemplate) ct;
			redis.delete(hashKey);
		}
	}
	
	/**
	 * 存入单个键值 若存在则覆盖
	 * @param key
	 * @param value
	 * @param context
	 */
	@SuppressWarnings("unchecked")
	public static void saveSingleCache(String key , Object value, Object... cont) {
		Object ct = null;
		if(cont != null && cont.length > 0) {
			ct = cont[0];
		}
		else {
			ct = context;
		}
		if(ct instanceof HttpSession) {
			HttpSession session = (HttpSession) ct;
			session.setAttribute(key, value);
		}
		else if(ct instanceof ServletContext) {
			ServletContext servlet = (ServletContext) ct;
			servlet.setAttribute(key, value);
		}
		else if(ct instanceof RedisTemplate) {
			RedisTemplate redis = (RedisTemplate) ct;
			redis.opsForValue().set(key, value);
			redis.expire(key, PublicServerKV.getIntVal("server-center.redis.outTime"), TimeUnit.HOURS);
		}
	}
	/**
	 * 取单个键值
	 * @param key
	 * @param value
	 * @param context
	 */
	public static Object getSingleCache(String key , Object... cont) {
		Object ct = null;
		if(cont != null && cont.length > 0) {
			ct = cont[0];
		}
		else {
			ct = context;
		}
		if(ct instanceof HttpSession) {
			HttpSession session = (HttpSession) ct;
			return session.getAttribute(key);
		}
		else if(ct instanceof ServletContext) {
			ServletContext servlet = (ServletContext) ct;
			return servlet.getAttribute(key);
		}
		else if(ct instanceof RedisTemplate) {
			RedisTemplate redis = (RedisTemplate) ct;
			return redis.opsForValue().get(key);
		}
		return null;
	}
	
	
	/**
	 *  存入集合值
	 * @param key
	 * @param value
	 * @param context
	 */
	@SuppressWarnings("unchecked")
	public static void saveListCache(String key , Object value, Object... cont) {
		Object ct = null;
		if(cont != null && cont.length > 0) {
			ct = cont[0];
		}
		else {
			ct = context;
		}
		if(ct instanceof HttpSession) {
			HttpSession session = (HttpSession) ct;
			List<Object> l = null;
			if(session.getAttribute(key) != null) {
				l = (List<Object>) session.getAttribute(key);
			}
			else {
				l = new ArrayList<Object>();
			}
			l.add(value);
			session.setAttribute(key, l);
		}
		else if(ct instanceof ServletContext) {
			ServletContext servlet = (ServletContext) ct;
			List<Object> l = null;
			if(servlet.getAttribute(key) != null) {
				l = (List<Object>) servlet.getAttribute(key);
			}
			else {
				l = new ArrayList<Object>();
			}
			l.add(value);
			servlet.setAttribute(key, l);
		}
		else if(ct instanceof RedisTemplate) {
			RedisTemplate redis = (RedisTemplate) ct;
			redis.opsForList().rightPush(key, value);
			redis.expire(key, PublicServerKV.getIntVal("server-center.redis.outTime"), TimeUnit.HOURS);
		}
	}
	
	/**
	 *  取集合
	 * @param key
	 * @param value
	 * @param context
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> getListCache(String key, Object... cont) {
		Object ct = null;
		if(cont != null && cont.length > 0) {
			ct = cont[0];
		}
		else {
			ct = context;
		}
		if(ct instanceof HttpSession) {
			HttpSession session = (HttpSession) ct;
			return (List<Object>) session.getAttribute(key);
		}
		else if(ct instanceof ServletContext) {
			ServletContext servlet = (ServletContext) ct;
			return (List<Object>) servlet.getAttribute(key);
		}
		else if(ct instanceof RedisTemplate) {
			RedisTemplate redis = (RedisTemplate) ct;
			return redis.opsForList().range(key, 0, -1);
		}
		return null;
	}
	
	/**
	 * 存普通 map
	 * @param key
	 * @param hashKey
	 * @param value
	 * @param context
	 */
	@SuppressWarnings("unchecked")
	public static void saveMapCache(String key ,String hashKey ,Object value, Object... cont) {
		Object ct = null;
		if(cont != null && cont.length > 0) {
			ct = cont[0];
		}
		else {
			ct = context;
		}
		if(ct instanceof HttpSession) {
			HttpSession session = (HttpSession) ct;
			Map<String ,Object> l = null;
			if(session.getAttribute(key) != null) {
				l = (HashMap<String ,Object>) session.getAttribute(key);
			}
			else {
				l = new HashMap<String ,Object>();
			}
			l.put(hashKey ,value);
			session.setAttribute(key, l);
		}
		else if(ct instanceof ServletContext) {
			ServletContext servlet = (ServletContext) ct;
			Map<String ,Object> l = null;
			if(servlet.getAttribute(key) != null) {
				l = (HashMap<String ,Object>) servlet.getAttribute(key);
			}
			else {
				l = new HashMap<String ,Object>();
			}
			l.put(hashKey ,value);
			servlet.setAttribute(key, l);
		}
		else if(ct instanceof RedisTemplate) {
			RedisTemplate redis = (RedisTemplate) context;
			redis.opsForHash().put(key, hashKey, value);
			redis.expire(key, PublicServerKV.getIntVal("server-center.redis.outTime"), TimeUnit.HOURS);
		}
	}
	
	/**
	 * 取普通 map值
	 * @param key
	 * @param hashKey
	 * @param value
	 * @param context
	 */
	@SuppressWarnings("unchecked")
	public static Object getMapCache(String key ,String hashKey, Object... cont) {
		Object ct = null;
		if(cont != null && cont.length > 0) {
			ct = cont[0];
		}
		else {
			ct = context;
		}
		if(ct instanceof HttpSession) {
			HttpSession session = (HttpSession) ct;
			Map<String , Object> l = (Map<String, Object>) session.getAttribute(key);
			if(l != null) {
				if(hashKey.equals("*"))
					return l;
				return l.get(hashKey);
			}
		}
		else if(ct instanceof ServletContext) {
			ServletContext servlet = (ServletContext) ct;
			Map<String , Object> l = (Map<String, Object>) servlet.getAttribute(key);
			if(l != null) {
				if(hashKey.equals("*"))
					return l;
				return l.get(hashKey);
			}
		}
		else if(ct instanceof RedisTemplate) {
			RedisTemplate redis = (RedisTemplate) ct;
			if(hashKey.equals("*"))
				return redis.opsForHash().entries(key);
			return redis.opsForHash().get(key, hashKey);
		}
		return null;
	}
	
	/**
	 * 存入Map<String , List<Object>>
	 * @param key
	 * @param hashKey
	 * @param value
	 * @param context
	 */
	@SuppressWarnings("unchecked")
	public static void saveMapListCache(String key ,String hashKey, Object value, Object... cont) {
		Object ct = null;
		if(cont != null && cont.length > 0) {
			ct = cont[0];
		}
		else {
			ct = context;
		}
		if(ct instanceof HttpSession) {
			HttpSession session = (HttpSession) ct;
			Map<String , List<Object>> l = null;
			List<Object> ll = null;
			if(session.getAttribute(key) != null) {
				l = (HashMap<String , List<Object>>) session.getAttribute(key);
				ll = l.get(hashKey);
				if(ll == null) ll = new ArrayList<Object>();
			}
			else {
				l = new HashMap<String ,List<Object>>();
				ll = new ArrayList<Object>();
			}
			if(value instanceof List) ll.addAll((List)value);
			else ll.add(value);
			l.put(hashKey, ll);
			session.setAttribute(key, l);
		}
		else if(ct instanceof ServletContext) {
			ServletContext servlet = (ServletContext) ct;
			Map<String , List<Object>> l = null;
			List<Object> ll = null;
			if(servlet.getAttribute(key) != null) {
				l = (HashMap<String , List<Object>>) servlet.getAttribute(key);
				ll = l.get(hashKey);
				if(ll == null) ll = new ArrayList<Object>();
			}
			else {
				l = new HashMap<String ,List<Object>>();
				ll = new ArrayList<Object>();
			}
			if(value instanceof List) ll.addAll((List)value);
			else ll.add(value);
			l.put(hashKey, ll);
			servlet.setAttribute(key, l);
		}
		else if(ct instanceof RedisTemplate) {
			RedisTemplate redis = (RedisTemplate) ct;
			ArrayList<Object> jd = null;
			if(redis.opsForHash().hasKey(key, hashKey)) {
				jd = (ArrayList<Object>) redis.opsForHash().get(key, hashKey);
			}
			else {
				jd = new ArrayList<Object>();
			}
			if(value instanceof List) jd.addAll((List)value);
			else jd.add(value);
			redis.opsForHash().put(key, hashKey, jd);
			redis.expire(key, PublicServerKV.getIntVal("server-center.redis.outTime"), TimeUnit.HOURS);
		}
	}
	
	/**
	 * 取Map<String , List<Object>> list 值
	 * @param key
	 * @param hashKey
	 * @param value
	 * @param context
	 */
	@SuppressWarnings("unchecked")
	public static Object getMapListCache(String key ,String hashKey, Object... cont) {
		Object ct = null;
		if(cont != null && cont.length > 0) {
			ct = cont[0];
		}
		else {
			ct = context;
		}
		if(ct instanceof HttpSession) {
			HttpSession session = (HttpSession) ct;
			Map<String , List<Object>> l = (Map<String, List<Object>>) session.getAttribute(key);
			if(l != null) {
				if(hashKey.equals("*"))
					return l;
				return l.get(hashKey);
			}
		}
		else if(ct instanceof ServletContext) {
			ServletContext servlet = (ServletContext) ct;
			Map<String , List<Object>> l = (Map<String, List<Object>>) servlet.getAttribute(key);
			if(l != null) {
				if(hashKey.equals("*"))
					return l;
				return l.get(hashKey);
			}
		}
		else if(ct instanceof RedisTemplate) {
			RedisTemplate redis = (RedisTemplate) ct;
			if(hashKey.equals("*"))
				return redis.opsForHash().entries(key);
			return redis.opsForHash().get(key, hashKey);
		}
		return null;
	}
	
	/**
	 * 存入Map<String , Set<Object>>
	 * @param key
	 * @param hashKey
	 * @param value
	 * @param context
	 */
	@SuppressWarnings("unchecked")
	public static void saveMapSetCache(String key ,String hashKey, Object value, Object... cont) {
		Object ct = null;
		if(cont != null && cont.length > 0) {
			ct = cont[0];
		}
		else {
			ct = context;
		}
		if(ct instanceof HttpSession) {
			HttpSession session = (HttpSession) ct;
			Map<String , Set<Object>> l = null;
			Set<Object> ll = null;
			if(session.getAttribute(key) != null) {
				l = (HashMap<String , Set<Object>>) session.getAttribute(key);
				ll = l.get(hashKey);
				if(ll == null) ll = new HashSet<Object>();
			}
			else {
				l = new HashMap<String ,Set<Object>>();
				ll = new HashSet<Object>();
			}
			if(value instanceof Set) ll.addAll((List)value);
			else ll.add(value);
			l.put(hashKey, ll);
			session.setAttribute(key, l);
		}
		else if(ct instanceof ServletContext) {
			ServletContext servlet = (ServletContext) ct;
			Map<String , Set<Object>> l = null;
			Set<Object> ll = null;
			if(servlet.getAttribute(key) != null) {
				l = (HashMap<String , Set<Object>>) servlet.getAttribute(key);
				ll = l.get(hashKey);
				if(ll == null) ll = new HashSet<Object>();
			}
			else {
				l = new HashMap<String ,Set<Object>>();
				ll = new HashSet<Object>();
			}
			if(value instanceof Set) ll.addAll((Set)value);
			else ll.add(value);
			l.put(hashKey, ll);
			servlet.setAttribute(key, l);
		}
		else if(ct instanceof RedisTemplate) {
			RedisTemplate redis = (RedisTemplate) ct;
			Set<Object> jd = null;
			if(redis.opsForHash().hasKey(key, hashKey)) {
				jd = (Set<Object>) redis.opsForHash().get(key, hashKey);
			}
			else {
				jd = new HashSet<Object>();
			}
			if(value instanceof Set) jd.addAll((List)value);
			else jd.add(value);
			redis.opsForHash().put(key, hashKey, jd);
			redis.expire(key, PublicServerKV.getIntVal("server-center.redis.outTime"), TimeUnit.HOURS);
		}
	}
	
	/**
	 * 取Map<String , Set<Object>> set集合
	 * @param key
	 * @param hashKey
	 * @param value
	 * @param context
	 */
	@SuppressWarnings("unchecked")
	public static Object getMapSetCache(String key ,String hashKey, Object... cont) {
		Object ct = null;
		if(cont != null && cont.length > 0) {
			ct = cont[0];
		}
		else {
			ct = context;
		}
		if(ct instanceof HttpSession) {
			HttpSession session = (HttpSession) ct;
			Map<String , Set<Object>> l = (Map<String, Set<Object>>) session.getAttribute(key);
			if(l != null) {
				if(hashKey.equals("*"))
					return l;
				return l.get(hashKey);
			}
		}
		else if(ct instanceof ServletContext) {
			ServletContext servlet = (ServletContext) ct;
			Map<String , Set<Object>> l = (Map<String, Set<Object>>) servlet.getAttribute(key);
			if(l != null) {
				if(hashKey.equals("*"))
					return l;
				return l.get(hashKey);
			}
		}
		else if(ct instanceof RedisTemplate) {
			RedisTemplate redis = (RedisTemplate) ct;
			if(hashKey.equals("*"))
				return redis.opsForHash().entries(key);
			return redis.opsForHash().get(key, hashKey);
		}
		return null;
	}
}
