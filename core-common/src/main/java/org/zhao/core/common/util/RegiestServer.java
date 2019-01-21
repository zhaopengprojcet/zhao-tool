package org.zhao.core.common.util;

import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.zhao.core.common.model.ClientContext;

public class RegiestServer {

	public static Map<String, String> QUERY_TOKEN = new HashMap<String, String>();
	
	public static ClientContext context;
	
	public static String WEB_IP = "";
	
	private static Log logger = LogFactory.getLog(RegiestServer.class);
	
	/**
	 * 向服务中心注册并取得token
	 * @param rolad
	 * @return
	 */
	public static synchronized String getToken(boolean rolad , String key) {
		getContext();
 		if(rolad) {
 			String url = queryPutUrl("/server/regiest.html");
 			Map<String, String> obj = new HashMap<String, String>();
 			obj.put("_jr", SignUtil.getHttpContext(JSONObject.fromObject(context).toString()));
 			JSONObject result = HttpUtils.post(url, obj);
 			if(result.containsKey("code") && result.getString("code").equals("SUCCESS")) {
 				QUERY_TOKEN.put(key, result.getString("data"));
 			}
 			else {
 				QUERY_TOKEN.put(key, "-1");
 				logger.error("注册服务【"+key+"】失败【"+result.getString("message")+"】");
 			}
 			return QUERY_TOKEN.get(key);
 		}
 		else {
 			if(!StringUtils.isEmpty(QUERY_TOKEN.get(key)) && !QUERY_TOKEN.get(key).equals("-1")) {
 				String url = queryPutUrl("/server/checkToken.html");
 	 			Map<String, String> obj = new HashMap<String, String>();
 	 			obj.put("_jr", SignUtil.getHttpContext(QUERY_TOKEN.get(key)));
 	 			JSONObject result = HttpUtils.post(url, obj);
 	 			if(result.containsKey("code") && result.getString("code").equals("SUCCESS")) {
 	 				return QUERY_TOKEN.get(key);
 	 			}
 			}
 			return getToken(true , key);
 		}
 	}
	
	/**
	 * 构建服务中心请求地址
	 * @param path
	 * @return
	 */
	public static String queryPutUrl(String path) {
 		String url = ConfigProperties.instance().getPropertiesVal("zhao.server.center.url");
 		if(StringUtils.isEmpty(url)) throw new RuntimeException("未配置耗时服务器地址");
 		return url + path;
 	}
	
	private static ClientContext getContext() {
 		if(RegiestServer.context == null) {
 			ClientContext ct = new ClientContext();
 	 		String ip = getWebIp();
 	 		if(StringUtils.isEmpty(ip)) throw new RuntimeException("获得服务ip失败");
 	 		ct.setIp(ip);
 	 		ct.setLoginName(ConfigProperties.instance().getPropertiesVal("zhao.loginname"));
 	 		ct.setPassword(ConfigProperties.instance().getPropertiesVal("zhao.password"));
 	 		String port = getLocalPort();
 	 		if(StringUtils.isEmpty(port)) throw new RuntimeException("获得服务端口失败");
 	 		ct.setPort(port);
 	 		String serverName = ConfigProperties.instance().getPropertiesVal("run.server.name");
 	 		if(StringUtils.isEmpty(serverName)) throw new RuntimeException("获得服务名失败");
 	 		ct.setServiceName(serverName);
 	 		RegiestServer.context = ct;
 		}
 		return RegiestServer.context;
 	}
	
	private static String getLocalPort() {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = null;
		try {
			objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
			        Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		}
		if(objectNames == null) throw new RuntimeException("获得服务端口号失败");
        String port = objectNames.iterator().next().getKeyProperty("port");
        return port;
    }
	
	private static String getWebIp() {
 		if(StringUtils.isEmpty(WEB_IP)) 
 			WEB_IP = getInternetIp();
 		return WEB_IP;
 	}
 	
 	private  static String getInternetIp(){
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
 	
 	private static String getIntranetIp(){
        try{
            return InetAddress.getLocalHost().getHostAddress();
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
