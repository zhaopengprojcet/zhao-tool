package org.zhao.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * @author zhao
 *
 */
public class DateUtil {

	private static SimpleDateFormat sdf = null;
	public static String yyyy_MM_dd_hh_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static String yyyy_MM_dd = "yyyy-MM-dd";
	
	
	public static long getMinOfTime2Now(long time) {
		time = new Date().getTime() - time;
		if(time < 1) time *= -1;
		return time / 1000 / 60;
	}
	
	public static String getTimeStr(Date date , String formatter) {
		sdf = new SimpleDateFormat(formatter);
		return sdf.format(date);
	}
	
	public static Date getTime(String date , String formatter) {
		date = date.replaceAll("\\+", " ");
		sdf = new SimpleDateFormat(formatter);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
