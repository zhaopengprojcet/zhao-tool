package org.zhao.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zhao.common.aspect.query.QueryTimeUse;
import org.zhao.common.client.ClientContext;
import org.zhao.common.role.RoleAop;
import org.zhao.common.role.RoleAopEnum;
import org.zhao.common.server.ServerConfig;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.CacheUtil;
import org.zhao.common.util.SignUtil;
import org.zhao.common.util.view.ResultContent;
import org.zhao.service.ServerService;

@RestController
@RequestMapping("/server/")
public class ServerController {

	@Autowired
	private ServerService serverService;
	
	/**
	 * 注册服务
	 * @param context
	 * @param request
	 * @return
	 */
	@RoleAop(key=RoleAopEnum.ALL)
	@RequestMapping("regiest.html")
	public String regiest(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
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
	public String putUseTime(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		ResultContent<String> time = SignUtil.getResultHttpContext(context,"token" ,"from","times");
		if(time.getCode().equals(ResultContent.ERROR)) return BaseResultUtil.result(time);
		ClientContext client = (ClientContext) CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, time.getJsonString("token"));
		if(client == null) return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR , "token错误"));
		QueryTimeUse.putTime(client.getIp() + "|" + client.getServiceName() + "|" +  time.getJsonString("from"), Long.parseLong(time.getJsonString("times")));
		return BaseResultUtil.result(new ResultContent<String>(ResultContent.SUCCESS , "记录完成"));
	}
}
