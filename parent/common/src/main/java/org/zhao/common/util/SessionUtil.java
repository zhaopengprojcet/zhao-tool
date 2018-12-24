package org.zhao.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;


public class SessionUtil {

	/**
	 * session缓存全局权限时间
	 */
	public static final String LESSION_TIME = "JESSION_TIME";
	
	public static final String LOGIN_MEMBER = "LOGIN_MEMBER"; //登录身份信息
	
	public static final String POWER_LIST = "POWER_LIST";//登录身份权限标记集合
	
	public static final String ROLE_POWERS = "ROLE_POWERS";//权限资源
	
	public static final String WHITE_IP_LIST = "WHITE_IP_LIST";//白名单
	
	public static final String BLACK_IP_LIST = "BLACK_IP_LIST";//黑名单
	
	public static final String WEB_IP = "WEB_IP";//外网地址
	
	/**
	 * 获得来访者真实ip
	 * @param request
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

    /**
     * 获得内网IP
     * @return 内网IP
     */
    public static String getIntranetIp(){
        try{
            return InetAddress.getLocalHost().getHostAddress();
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 获得外网IP
     * @return 外网IP
     */
    public static String getInternetIp(){
        try{
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            Enumeration<InetAddress> addrs;
            while (networks.hasMoreElements())
            {
                addrs = networks.nextElement().getInetAddresses();
                while (addrs.hasMoreElements())
                {
                    ip = addrs.nextElement();
                    if (ip != null
                            && ip instanceof Inet4Address
                            && ip.isSiteLocalAddress()
                            && !ip.getHostAddress().equals(getIntranetIp()))
                    {
                        return ip.getHostAddress();
                    }
                }
            }

            // 如果没有外网IP，就返回内网IP
            return getIntranetIp();
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
    public static String getWebIp() {
    	return (String) CacheUtil.getSingleCache(WEB_IP);
    }
}
