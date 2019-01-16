package org.zhao.common.aspect.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zhao.common.client.ClientContext;
import org.zhao.common.pojo.model.ZmessageQueueModel;
import org.zhao.common.properties.ConfigProperties;
import org.zhao.common.server.ServerConfig;
import org.zhao.common.util.CacheUtil;
import org.zhao.common.util.RandomUtils;
import org.zhao.common.zmq.model.MqContext;
import org.zhao.common.zmq.queue.ZQueueConfig;
import org.zhao.common.zmq.queue.Zqueue;
import org.zhao.service.ZmessageQueueService;

/**
 * 消息队列接收 处理线程
 * @author zhao
 *
 */
public class QueryMq extends Thread{

	private Log logger = LogFactory.getLog(this.getClass());
	
	//false 注册服务   true 接收消息
	private boolean flush = true;
	
	
	private MqContext mqContext;
	private Zqueue<ZmessageQueueModel> mqQueue;
	private ZmessageQueueService zMessageQueueService;
	
	public QueryMq(MqContext context ,ZmessageQueueService zMessageQueueService){
		this.mqContext = context;
		this.zMessageQueueService = zMessageQueueService;
		if(ConfigProperties.instance().getPropertiesBoolean("server.redis.open")) {
			this.mqQueue = ZQueueConfig.createRedisQueue(MQ_QUEUE_KEY);
		}
		else {
			this.mqQueue = ZQueueConfig.createQueue(MQ_QUEUE_KEY);
		}
	}

	/**
	 * 重新导入失败消息
	 * @param model
	 */
	public static void flush(ZmessageQueueModel model) {
		Zqueue<ZmessageQueueModel> mqQueue = null;
		if(ConfigProperties.instance().getPropertiesBoolean("server.redis.open")) {
			mqQueue = ZQueueConfig.createRedisQueue(MQ_QUEUE_KEY);
		}
		else {
			mqQueue = ZQueueConfig.createQueue(MQ_QUEUE_KEY);
		}
		mqQueue.flush(model);
	}
	
	private void flush() {
		ClientContext client = (ClientContext) CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, this.mqContext.getToken());
		//将消息结构转换并放入缓存队列中
		ZmessageQueueModel model = new ZmessageQueueModel();
		model.setContext(this.mqContext.getContext());
		model.setPutServiceName(this.mqContext.getService());
		model.setSendIp(client.getIp());
		model.setSendPort(client.getPort());
		model.setSendServerName(client.getServiceName());
		model.setSendTime(this.mqContext.getSendTime());
		model.setMsgState("0");
		model.setPushType(this.mqContext.getPushType());
		this.zMessageQueueService.save(model);
		this.mqQueue.flush(model);
	}
	
	
	private List<String> services;
	private String token;
	
	public QueryMq(List<String> services , String token){
		this.services = services;
		this.token = token;
		this.flush = false;
	}
	
	private void regiest() {
		if(services == null) return;
		logger.info("开始注册消息队列订阅服务【"+JSONArray.fromObject(services).toString()+"】【"+token+"】");
		Map<String, List<String>> oldService = (Map<String, List<String>>) CacheUtil.getMapListCache(MQ_SERVICES_LIST, "*");
		if(oldService != null) {
			//新增修改   限原有key
			for (String key : oldService.keySet()) { //遍历原有缓存 删除新注册中没有的,添加在新注册中有的
				List<String> tks = (List<String>) CacheUtil.getMapListCache(MQ_SERVICES_LIST, key);
				if(services.contains(key)) { //原有注册
					if(!tks.contains(token)) { 
						tks.add(token); //新
						CacheUtil.saveMapListCache(MQ_SERVICES_LIST, key, tks);
					}
				}
				else {
					if(tks.contains(token)) {
						tks.remove(token);
						CacheUtil.saveMapListCache(MQ_SERVICES_LIST, key, tks);
					}
				}
			}
			//新增
			for (String key : services) { //遍历新注册，将原缓存中未有的加入
				if(!oldService.keySet().contains(key)) {
					List<String> tks = new ArrayList<String>();
					tks.add(token);
					CacheUtil.saveMapListCache(MQ_SERVICES_LIST, key, tks);
				}
			}
		}
		else {
			for (String key : services) { //遍历新注册，将原缓存中未有的加入
				List<String> tks = new ArrayList<String>();
				tks.add(token);
				CacheUtil.saveMapListCache(MQ_SERVICES_LIST, key, token);
			}
		}
		oldService = (Map<String, List<String>>) CacheUtil.getMapListCache(MQ_SERVICES_LIST, "*");
		logger.info("注册完成，消息队列订阅服务列表【"+JSONObject.fromObject(oldService).toString()+"】");
	}
	
	/**
	 * 获得指定服务下的客户端token，若未注册，返回null
	 * @param serviceName
	 * @return
	 */
	public static List<String> getServiceToken(String serviceName , String type) {
		List<String> tokens = (List<String>) CacheUtil.getMapListCache(MQ_SERVICES_LIST, serviceName);
		if(!CollectionUtils.isEmpty(tokens)) {
			if(type.equals("ALL")) return tokens;
			else {
				int i = (int) (Math.random() * tokens.size());
				List<String> token = new ArrayList<String>();
				token.add(tokens.get(i));
				return token;
			}
			
		}
		return null;
	}
	
	private static final String MQ_SERVICES_LIST = "MQ_SERVICES_LIST";
	public static final String MQ_QUEUE_KEY = "MQ_QUEUE_LIST";
	
	@Override
	public void run() {
		if(flush) flush();
		else regiest();
	}
	
	
}
