package org.zhao.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 时间工具类
 * @author zhao
 *
 */
public class DateUtil {

	private static SimpleDateFormat sdf = null;
	public static String yyyy_MM_dd_hh_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static String yyyy_MM_dd = "yyyy-MM-dd";
	public static String hh_mm = "HH:mm";
	
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
	
	public static int getWeekDay() {
		Calendar c=Calendar.getInstance(); 
		c.setTime(new Date()); 
		int week=c.get(Calendar.DAY_OF_WEEK);
		if(week < 1) return 7;
		return week;
	}
	
	public static int getMonthDay() {
		Calendar c=Calendar.getInstance(); 
		c.setTime(new Date()); 
		int day=c.get(Calendar.DAY_OF_MONTH);
		return day;
	}
	
	/**
	 * 开始到今天结束的所有命中时间(分钟)
	 * @param date
	 * @param se
	 * @return
	 */
	public static List<String> getSplitTime(Date date ,Date endDate, int se) {
		long thisTime = date.getTime();
		long endTime = endDate.getTime();
		List<String> times = new ArrayList<String>();
		for(;thisTime <= endTime ; thisTime+=(se*1000*60)) {
			times.add(DateUtil.getTimeStr(new Date(thisTime), hh_mm));
		}
		return times;
	}
	
	public static Date getDateEnd(Date time) {
		String end = getTimeStr(time, yyyy_MM_dd) + " 23:59:59";
		return getTime(end, yyyy_MM_dd_hh_mm_ss);
	}
}
