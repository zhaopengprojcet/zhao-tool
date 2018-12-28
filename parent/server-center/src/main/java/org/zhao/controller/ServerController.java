package org.zhao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zhao.common.aspect.query.QueryMcs;
import org.zhao.common.aspect.query.QuerySchedules;
import org.zhao.common.aspect.query.QueryTimeUse;
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
import org.zhao.service.ServerService;
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
	
	/**
	 * 注册服务
	 * @param context
	 * @param request
	 * @return
	 */
	@UseTime
	@RoleAop(key=RoleAopEnum.ALL)
	@RequestMapping("regiest.html")
	public String regiest(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		if(!PublicServerKV.getBooleanVal("server.service.regiest")) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "中心服务器未开启注册服务！"));
		ResultContent<ClientContext> client = SignUtil.getResultHttpContext(context, ClientContext.class, "ip","port","serviceName","loginName","password");
		if(client.getCode().equals(ResultContent.ERROR)) return BaseResultUtil.result(client);
		return BaseResultUtil.result(this.serverService.regiestClient(client.getData(), request));
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
		if(!PublicServerKV.getBooleanVal("server.service.puttime")) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "中心服务器未开启耗时服务！"));
		ResultContent<String> time = SignUtil.getResultHttpContext(context,"token" ,"from","times");
		if(time.getCode().equals(ResultContent.ERROR)) return BaseResultUtil.result(time);
		ClientContext client = (ClientContext) CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, time.getJsonString("token"));
		if(client == null) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "token错误"));
		QueryTimeUse.putTime(client.getIp() + "|" + client.getServiceName() + "|" +  time.getJsonString("from"), Long.parseLong(time.getJsonString("times")));
		return BaseResultUtil.result(new ResultContent<String>(ResultContent.SUCCESS , "记录完成"));
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
		if(!PublicServerKV.getBooleanVal("server.service.mcs")) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "中心服务器未开启计算机运行异常记录服务！"));
		ResultContent<String> time = SignUtil.getResultHttpContext(context,"token" ,"mcs");
		if(time.getCode().equals(ResultContent.ERROR)) return BaseResultUtil.result(time);
		List<Double> datas = JSONArray.toList(JSONObject.fromObject(time.getData()).getJSONArray("mcs"));
		ClientContext client = (ClientContext) CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, time.getJsonString("token"));
		ThreadPoolUtils.putThread("异常状态处理", new QueryMcs(datas ,this.zServerExpService , client));
		return BaseResultUtil.result(new ResultContent<String>(ResultContent.SUCCESS , "记录完成"));
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
		if(!PublicServerKV.getBooleanVal("server.service.schedule")) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "中心服务器未开启定时任务调度服务！"));
		ResultContent<String> time = SignUtil.getResultHttpContext(context,"token" ,"schedules");
		if(time.getCode().equals(ResultContent.ERROR)) return BaseResultUtil.result(time);
		List<String> schedules = JSONArray.toList(JSONObject.fromObject(time.getData()).getJSONArray("schedules"));
		ClientContext client = (ClientContext) CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, time.getJsonString("token"));
		if(client == null) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "token错误"));
		ThreadPoolUtils.putThread("定时任务注册", new QuerySchedules(schedules, time.getJsonString("token")));
		return BaseResultUtil.result(new ResultContent<String>(ResultContent.SUCCESS , "记录完成"));
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
		if(!PublicServerKV.getBooleanVal("server.service.schedule")) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "中心服务器未开启定时任务调度服务！"));
		ResultContent<String> time = SignUtil.getResultHttpContext(context,"token" ,"scheduleId","result");
		if(time.getCode().equals(ResultContent.ERROR)) return BaseResultUtil.result(time);
		ClientContext client = (ClientContext) CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, time.getJsonString("token"));
		if(client == null) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "token错误"));
		
		try {
			JSONObject obj = JSONObject.fromObject(time.getData()).getJSONObject("result");
			String scheduleId = time.getJsonString("scheduleId");
			return BaseResultUtil.result(this.zScheduleService.updateResultLog(scheduleId, obj.toString()));
		} catch (Exception e) {
			e.printStackTrace();
			return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "执行结果解析错误"));
		}
	}
}
