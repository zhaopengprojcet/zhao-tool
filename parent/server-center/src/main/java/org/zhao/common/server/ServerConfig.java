package org.zhao.common.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;
import org.zhao.common.client.ClientContext;
import org.zhao.common.interceptor.PublicServerKV;
import org.zhao.common.util.CacheUtil;

public class ServerConfig {

	public static final String REGIEST_CLIENT_TOKEN = "REGIEST_CLIENT_TOKEN";//已注册通过客户端token
	public static final String REGIEST_CLIENT_LIST = "REGIEST_CLIENT_LIST"; //通过注册的客户端集合
	
	private static String[] serverPaths; //注册服务地址
	protected static void setServerPaths() {
		String path = PublicServerKV.getStringVal("server-center.server.paths");
		if(!StringUtils.isEmpty(path)) {
			Set<String> set = new HashSet<String>();
			String[] list = path.split(",");
			for (String string : list) {
				set.add(string);
			}
			serverPaths = new String[set.size()];
			set.toArray(serverPaths);
		}
	}
	public static String[] getServerPaths() {
		return serverPaths;
	}
	
	private static String loginUser;//注册 账号和密码
	private static String password;
	protected static void setLoginUser(String user) {
		loginUser = user;
	}
	public static String getLoginUser() {
		return loginUser;
	}
	protected static void setPassword(String pass) {
		password = pass;
	}
	public static String getPassword() {
		return password;
	}
	
	
	public synchronized static void addClient(ClientContext client) {
		CacheUtil.saveMapSetCache(REGIEST_CLIENT_LIST, client.getServiceName(), client.getIp());
	}
	public static Set<String> getClient(String key) {
		return (Set<String>) CacheUtil.getMapSetCache(REGIEST_CLIENT_LIST, key);
	}
	public static Map<String, Set<String>> getClients() {
		return (Map<String, Set<String>>) CacheUtil.getMapSetCache(REGIEST_CLIENT_LIST, "*");
	}
}
