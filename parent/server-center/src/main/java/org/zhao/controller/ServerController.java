package org.zhao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zhao.common.aspect.query.QueryMcs;
import org.zhao.common.aspect.query.QueryMq;
import org.zhao.common.aspect.query.QuerySchedules;
import org.zhao.common.aspect.query.QueryTimeUse;
import org.zhao.common.client.ClientCheckUtil;
import org.zhao.common.client.ClientContext;
import org.zhao.common.interceptor.PublicServerKV;
import org.zhao.common.role.RoleAop;
import org.zhao.common.role.RoleAopEnum;
import org.zhao.common.server.ServerConfig;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.CacheUtil;
import org.zhao.common.util.SignUtil;
import org.zhao.common.util.ThreadPoolUtils;
import org.zhao.common.util.view.ResultContent;
import org.zhao.common.zmq.model.MqContext;
import org.zhao.service.ServerService;
import org.zhao.service.ZmessageQueueService;
import org.zhao.service.ZscheduleService;
import org.zhao.service.ZserverExpService;
import org.zhao.usetime.annotation.UseTime;

@RestController
@RequestMapping("/server/")
public class ServerController {

	@Autowired
	private ServerService serverService;
	@Autowired
	private ZserverExpService zServerExpService;
	@Autowired
	private ZscheduleService zScheduleService;
	@Autowired
	private ZmessageQueueService zMessageQueueService;
	
	/**
	 * 注册服务
	 * @param context
	 * @param request
	 * @return
	 */
	//@UseTime
	@RoleAop(key=RoleAopEnum.ALL)
	@RequestMapping("regiest.html")
	public String regiest(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		if(!PublicServerKV.getBooleanVal("server-center.service.regiest")) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "中心服务器未开启注册服务！"));
		ResultContent<ClientContext> client = SignUtil.getResultHttpContext(context, ClientContext.class, "ip","port","serviceName","loginName","password");
		if(client.getCode().equals(ResultContent.ERROR)) return BaseResultUtil.result(client);
		return BaseResultUtil.result(this.serverService.regiestClient(client.getData(), request));
	}
	
	/**
	 * token有效验证
	 * @param context
	 * @return
	 */
	@RoleAop(key=RoleAopEnum.ALL)
	@RequestMapping("checkToken.html")
	public String checkToken(@RequestParam(value="_jr",required=false,defaultValue="")String context) {
		if(!PublicServerKV.getBooleanVal("server-center.service.regiest")) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "中心服务器未开启注册服务！"));
		ResultContent<String> client = SignUtil.getResultHttpContext(context);
		if(client.getCode().equals(ResultContent.ERROR)) return BaseResultUtil.result(client);
		if(StringUtils.isEmpty(client.getData())) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR, "参数缺失【01】"));
		if(CacheUtil.getMapSetCache(ServerConfig.REGIEST_CLIENT_TOKEN, client.getData()) == null) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR, "无效TOKEN"));
		return BaseResultUtil.result(new ResultContent<String>(ResultContent.SUCCESS, "success"));
	}
	
	/**
	 * 耗时服务
	 * @param context
	 * @param request
	 * @return
	 */
	@RoleAop(key=RoleAopEnum.ALL)
	@RequestMapping("putUseTime.html")
	public String putUseTime(@RequestParam(value="_jr",required=false,defaultValue="")String context) {
		if(!PublicServerKV.getBooleanVal("server-center.service.puttime")) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "中心服务器未开启耗时服务！"));
		ResultContent<String> time = SignUtil.getResultHttpContext(context,"token" ,"from","times");
		if(time.getCode().equals(ResultContent.ERROR)) return BaseResultUtil.result(time);
		ClientContext client = (ClientContext) CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, time.getJsonString("token"));
		if(client == null) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "token错误"));
		
		if(!ClientCheckUtil.checkPowers(client, null, "UTC")) {
			return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "授权验证未通过【UTC】"));
		}
		
		QueryTimeUse.putTime(client.getIp() + "|" + client.getServiceName() + "|" +  time.getJsonString("from"), Long.parseLong(time.getJsonString("times")));
		return BaseResultUtil.result(new ResultContent<String>(ResultContent.SUCCESS , "记录完成【UTC】"));
	}
	
	/**
	 * 计算机异常运行状态监控
	 * @param context
	 * @param request
	 * @return
	 */
	@UseTime
	@RoleAop(key=RoleAopEnum.ALL)
	@RequestMapping("putMcs.html")
	public String putMcs(@RequestParam(value="_jr",required=false,defaultValue="")String context) {
		if(!PublicServerKV.getBooleanVal("server-center.service.mcs")) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "中心服务器未开启计算机运行异常记录服务！"));
		ResultContent<String> time = SignUtil.getResultHttpContext(context,"token" ,"mcs");
		if(time.getCode().equals(ResultContent.ERROR)) return BaseResultUtil.result(time);
		List<Double> datas = JSONArray.toList(JSONObject.fromObject(time.getData()).getJSONArray("mcs"));
		ClientContext client = (ClientContext) CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, time.getJsonString("token"));
		if(!ClientCheckUtil.checkPowers(client, null, "MCC")) {
			return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "授权验证未通过【MCC】"));
		}
		ThreadPoolUtils.putThread("异常状态处理", new QueryMcs(datas ,this.zServerExpService , client));
		return BaseResultUtil.result(new ResultContent<String>(ResultContent.SUCCESS , "记录完成【MCC】"));
	}
	
	/**
	 * 定时任务注册服务
	 * @param context
	 * @return
	 */
	@UseTime
	@RoleAop(key=RoleAopEnum.ALL)
	@RequestMapping("putSchedule.html")
	public String putSchedule(@RequestParam(value="_jr",required=false,defaultValue="")String context) {
		if(!PublicServerKV.getBooleanVal("server-center.service.schedule")) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "中心服务器未开启定时任务调度服务！"));
		ResultContent<String> time = SignUtil.getResultHttpContext(context,"token" ,"schedules");
		if(time.getCode().equals(ResultContent.ERROR)) return BaseResultUtil.result(time);
		List<String> schedules = JSONArray.toList(JSONObject.fromObject(time.getData()).getJSONArray("schedules"));
		ClientContext client = (ClientContext) CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, time.getJsonString("token"));
		if(client == null) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "token错误"));
		if(!ClientCheckUtil.checkPowers(client, null, "SCC")) {
			return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "授权验证未通过【SCC】"));
		}
		ThreadPoolUtils.putThread("定时任务注册", new QuerySchedules(schedules, time.getJsonString("token")));
		return BaseResultUtil.result(new ResultContent<String>(ResultContent.SUCCESS , "注册完成【SCC】"));
	}
	
	/**
	 * 调度定时任务执行情况反馈接口
	 * @param context
	 * @return
	 */
	@UseTime
	@RoleAop(key=RoleAopEnum.ALL)
	@RequestMapping("scheduleState.html")
	public String scheduleState(@RequestParam(value="_jr",required=false,defaultValue="")String context) {
		if(!PublicServerKV.getBooleanVal("server-center.service.schedule")) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "中心服务器未开启定时任务调度服务！"));
		ResultContent<String> time = SignUtil.getResultHttpContext(context,"token" ,"scheduleId","result");
		if(time.getCode().equals(ResultContent.ERROR)) return BaseResultUtil.result(time);
		ClientContext client = (ClientContext) CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, time.getJsonString("token"));
		if(client == null) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "token错误"));
		if(!ClientCheckUtil.checkPowers(client, null, "SCC")) {
			return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "授权验证未通过【SCC】"));
		}
		try {
			JSONObject obj = JSONObject.fromObject(time.getData()).getJSONObject("result");
			String scheduleId = time.getJsonString("scheduleId");
			return BaseResultUtil.result(this.zScheduleService.updateResultLog(scheduleId, obj.toString()));
		} catch (Exception e) {
			e.printStackTrace();
			return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "执行结果解析错误【SCC】"));
		}
	}
	
	
	
	/**
	 *mq客户端服务注册
	 * @param context
	 * @return
	 */
	@UseTime
	@RoleAop(key=RoleAopEnum.ALL)
	@RequestMapping("putMqClient.html")
	public String putMqClient(@RequestParam(value="_jr",required=false,defaultValue="")String context) {
		if(!PublicServerKV.getBooleanVal("server-center.service.mq")) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "中心服务器未开启mq消息服务！"));
		ResultContent<String> time = SignUtil.getResultHttpContext(context,"token" ,"mqServices");
		if(time.getCode().equals(ResultContent.ERROR)) return BaseResultUtil.result(time);
		List<String> mqServices = JSONArray.toList(JSONObject.fromObject(time.getData()).getJSONArray("mqServices"));
		ClientContext client = (ClientContext) CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, time.getJsonString("token"));
		if(client == null) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "token错误"));
		if(!ClientCheckUtil.checkPowers(client, null, "MMC")) {
			return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "授权验证未通过【MMC】"));
		}
		ThreadPoolUtils.putThread("mq客户端服务注册", new QueryMq(mqServices, time.getJsonString("token")));
		return BaseResultUtil.result(new ResultContent<String>(ResultContent.SUCCESS , "注册完成【MMC】"));
	}
	/**
	 * mq客户端请求服务
	 * @param context
	 * @return
	 */
	@UseTime
	@RoleAop(key=RoleAopEnum.ALL)
	@RequestMapping("putMsg.html")
	public String putMsg(@RequestParam(value="_jr",required=false,defaultValue="")String context) {
		if(!PublicServerKV.getBooleanVal("server-center.service.mq")) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "中心服务器未开启mq消息服务！"));
		ResultContent<MqContext> mq = SignUtil.getResultHttpContext(context,MqContext.class ,"token" ,"context" ,"service" ,"sendTime" , "pushType");
		if(mq.getCode().equals(ResultContent.ERROR)) return BaseResultUtil.result(mq);
		ClientContext client = (ClientContext) CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, mq.getData().getToken());
		if(client == null) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "token错误"));
		if(!ClientCheckUtil.checkPowers(client, null, "MMC")) {
			return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "授权验证未通过【MMC】"));
		}
		ThreadPoolUtils.putThread("接收mq消息", new QueryMq(mq.getData() , this.zMessageQueueService));
		return BaseResultUtil.result(new ResultContent<String>(ResultContent.SUCCESS , "记录完成【MMC】"));
	}
	
	/**
	 * 调度mq情况反馈接口
	 * @param context
	 * @return
	 */
	@UseTime
	@RoleAop(key=RoleAopEnum.ALL)
	@RequestMapping("mqState.html")
	public String mqState(@RequestParam(value="_jr",required=false,defaultValue="")String context) {
		if(!PublicServerKV.getBooleanVal("server-center.service.mq")) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "中心服务器未开启mq消息服务！"));
		ResultContent<String> time = SignUtil.getResultHttpContext(context,"token" ,"scheduleId","result");
		if(time.getCode().equals(ResultContent.ERROR)) return BaseResultUtil.result(time);
		ClientContext client = (ClientContext) CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, time.getJsonString("token"));
		if(client == null) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "token错误"));
		if(!ClientCheckUtil.checkPowers(client, null, "MMC")) {
			return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "授权验证未通过【MMC】"));
		}
		try {
			JSONObject obj = JSONObject.fromObject(time.getData()).getJSONObject("result");
			String logId = time.getJsonString("scheduleId");
			return BaseResultUtil.result(this.zMessageQueueService.updateLog(logId, obj.toString()));
		} catch (Exception e) {
			e.printStackTrace();
			return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "执行结果解析错误【MMC】"));
		}
	}
}
