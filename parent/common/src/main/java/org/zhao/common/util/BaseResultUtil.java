package org.zhao.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.apache.log4j.Logger;
import org.assertj.core.util.DateUtil;
import org.zhao.common.properties.ConfigProperties;
import org.zhao.common.util.view.ResultContent;


public class BaseResultUtil {

	private static Logger log = Logger.getLogger(BaseResultUtil.class);
	
	public static <T> String result(ResultContent<T> result) {
		log.debug(JSONObject.fromObject(result).toString());
		return JSONObject.fromObject(result).toString();
	}
	
	public static <T> String resultEasyUi(ResultContent<List<T>> result) {
		JSONObject obj = new JSONObject();
		if(result.getCode().equals(ResultContent.ERROR)) {
			obj.put("total", 0);
			obj.put("rows", "[]");
			obj.put("message", result.getMessage());
		}
		else {
			obj.put("total", result.getCount());
			obj.put("rows", result.getData());
		}
		log.debug(obj.toString());
		return obj.toString();
	}
	
	public static <T> String resultList(ResultContent<List<T>> result) {
		if(result.getCode().equals(ResultContent.SUCCESS)) {
			log.debug(JSONArray.fromObject(result.getData()).toString());
			return JSONArray.fromObject(result.getData()).toString();
		}
		return new JSONArray().toString();
	}
	
	public static <T> String resultEasyUiData(JSONObject obj) {
		if(obj.getString("code").equals(ResultContent.SUCCESS)) {
			return obj.getJSONObject("data").toString();
		}
		return new JSONObject().toString();
	}
	
	public static String resultData(JSONObject obj) {
		return obj.toString();
	}
	
	/**
	 * 用于补充查询功能下的结果标记和提示
	 * @param <T>
	 * @return
	 */
	public static <T> ResultContent<T> setCodeMsg(ResultContent<T> result) {
		if(result.getData() == null) {
			result.setCode(ResultContent.ERROR);
			result.setMessage("没有满足条件的数据");
			return result;
		}
		result.setCode(ResultContent.SUCCESS);
		result.setMessage("查询成功");
		return result;
	}
	
	public static String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}
