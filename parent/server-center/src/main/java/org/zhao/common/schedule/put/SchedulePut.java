package org.zhao.common.schedule.put;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zhao.common.aspect.query.QueryMq;
import org.zhao.common.aspect.query.QueryTimeUse;
import org.zhao.common.client.ClientContext;
import org.zhao.common.interceptor.PublicServerKV;
import org.zhao.common.pojo.model.ZmessageQueueLogModel;
import org.zhao.common.pojo.model.ZmessageQueueModel;
import org.zhao.common.properties.ConfigProperties;
import org.zhao.common.server.ServerConfig;
import org.zhao.common.util.CacheUtil;
import org.zhao.common.util.HttpUtils;
import org.zhao.common.util.view.ResultContent;
import org.zhao.common.zmq.queue.ZQueueConfig;
import org.zhao.common.zmq.queue.Zqueue;
import org.zhao.schedule.annotation.ScheduleMethod;
import org.zhao.schedule.annotation.zhaoScheduleBean;
import org.zhao.schedule.model.ReturnCode;
import org.zhao.service.ZmessageQueueService;
import org.zhao.service.ZuseTimeService;

/**
 * 中心服务器自带的定时任务
 * 
 * 启用需要在菜单中配置
 * @author zhao
 *
 */
@zhaoScheduleBean
public class SchedulePut {

	private Log logger = LogFactory.getLog(this.getClass());
	@Autowired
	private ZuseTimeService zUseTimeService;
	
	/**
	 * 耗时持久化
	 * @return
	 */
	@ScheduleMethod(scheduleName = "put_time_save")
	public String savePutTime() {
		try {
			if(!CollectionUtils.isEmpty(QueryTimeUse.getSaveList())) {
				zUseTimeService.save(QueryTimeUse.getSaveList());
				QueryTimeUse.clearAll();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ReturnCode.error(e.getLocalizedMessage());
		}
		return ReturnCode.SUCCESS;
	}
	
	/**
	 * token过期删除
	 * @return
	 */
	@ScheduleMethod(scheduleName = "delete_outtime_token")
	public String deleteOutTimeToken() {
		try {
			logger.info("删除过期token开始");
			Map<String , ClientContext> clients = (Map<String, ClientContext>) CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, "*");
			logger.info("原注册内容【"+JSONObject.fromObject(clients).toString()+"】");
			if(clients != null && clients.size() > 0) {
				logger.info("----------------------删除列表----------------");
				for(String key : clients.keySet()) {
					ClientContext cl = clients.get(key);
					if(System.currentTimeMillis() - cl.getRegiestTime().getTime() >= PublicServerKV.getIntVal("server-center.token.outTime")) { //token2小时过期
						CacheUtil.removeMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, key);
						Set<String> ips = (Set<String>) CacheUtil.getMapSetCache(ServerConfig.REGIEST_CLIENT_LIST, cl.getServiceName());
						ips.remove(cl.getIp());
						CacheUtil.saveMapSetCache(ServerConfig.REGIEST_CLIENT_LIST, cl.getServiceName(), ips);
						logger.info("【"+key+"】"+JSONObject.fromObject(cl).toString());
					}
				}
			}
			logger.info("剩余注册内容【"+JSONObject.fromObject(CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, "*")).toString()+"】");
			logger.info("删除过期token结束");
		} catch (Exception e) {
			e.printStackTrace();
			return ReturnCode.error(e.getLocalizedMessage());
		}
		return ReturnCode.SUCCESS;
	}
	
	@Autowired
	private ZmessageQueueService zMessageQueueService;
	
	/**
	 * 消息队列中消息转发任务
	 * @return
	 */
	@ScheduleMethod(scheduleName="server_mq_put")
	public String serverMqPut() {
		try {
			if(PublicServerKV.getBooleanVal("server-center.service.mq")) {
				Zqueue<ZmessageQueueModel> mqQueue = null;
				if(ConfigProperties.instance().getPropertiesBoolean("server.redis.open")) {
					mqQueue = ZQueueConfig.createRedisQueue(QueryMq.MQ_QUEUE_KEY);
				}
				else {
					mqQueue = ZQueueConfig.createQueue(QueryMq.MQ_QUEUE_KEY);
				}
				ZmessageQueueModel model = mqQueue.pop();
				if(model == null) return ReturnCode.success("无任务");
				//当前状态不是第一次发送时进行失效判断
				if(!StringUtils.isEmpty(model.getMsgState()) && !model.getMsgState().equals("0")) {
					//如果已有状态且转入时间截止目前超过设置时间，则认为是失败消息，进行持久化并不再放入队列
					if((System.currentTimeMillis() - model.getSendTime().getTime())/1000 >= PublicServerKV.getIntVal("server-center.service.mq.outTime")) {
						this.zMessageQueueService.save(model);
						return ReturnCode.success("服务设置为过期【"+model.getPutServiceName()+"】");
					}
				}
				if(model != null) {
					//查看注册列表中是否有订阅该服务的客户端
					List<String> token = QueryMq.getServiceToken(model.getPutServiceName() , model.getPushType());
					if(CollectionUtils.isEmpty(token)) {
						model.setMsgState("2");
						mqQueue.flush(model);
						return ReturnCode.success("没有订阅该服务的客户端【"+model.getPutServiceName()+"】");
					}
					for (String string : token) {
						ClientContext client = (ClientContext) CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, string);
						ZmessageQueueLogModel log = new ZmessageQueueLogModel();
						
						log.setLastPuTime(new Date());
						log.setPutClientIp(client.getIp());
						log.setPutClientName(client.getServiceName());
						log.setPutClientPort(client.getPort());
						log.setMessageId(model.getId());
						this.zMessageQueueService.saveLog(log);
						put(string, model, log);
					}
					
				}
			}
			else {
				return ReturnCode.error("未开启消息队列服务");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ReturnCode.error(e.getLocalizedMessage());
		}
		
		
		return ReturnCode.SUCCESS;
	}
	
	private void put(String token , ZmessageQueueModel model , ZmessageQueueLogModel log) {
		ResultContent<String> result = new ResultContent<String>();
		ClientContext client = (ClientContext) CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, token);
		Map<String, String> obj = new HashMap<String, String>();
		JSONObject json = new JSONObject();
		json.put("_tk", token);
		json.put("_sev", model.getPutServiceName());
		json.put("_sci", log.getId());
		json.put("_ct", model.getContext());
		obj.put("_jr", json.toString());
		try {
			result = HttpUtils.post("http://"+client.getIp()+":"+client.getPort()+"/mq/response.html", obj);
			log.setLastReturnContext(result.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.setLastReturnContext(result.getMessage());
		}
		try {
			this.zMessageQueueService.saveLog(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if(JSONObject.fromObject(result).getString("code").equals("0")) {
				model.setMsgState("1");
				this.zMessageQueueService.save(model);
			}
			else {
				Zqueue<ZmessageQueueModel> mqQueue = null;
				if(ConfigProperties.instance().getPropertiesBoolean("server.redis.open")) {
					mqQueue = ZQueueConfig.createRedisQueue(QueryMq.MQ_QUEUE_KEY);
				}
				else {
					mqQueue = ZQueueConfig.createQueue(QueryMq.MQ_QUEUE_KEY);
				}
				model.setMsgState("-1");
				mqQueue.flush(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
