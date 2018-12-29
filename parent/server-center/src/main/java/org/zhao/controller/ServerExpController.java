package org.zhao.controller;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.mybatis.query.ParamterRequirement;
import org.zhao.common.mybatis.query.QueryParames;
import org.zhao.common.pojo.model.ZserverExpModel;
import org.zhao.common.role.RoleAop;
import org.zhao.common.role.RoleAopEnum;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.DateUtil;
import org.zhao.common.util.view.QuerySign;
import org.zhao.common.util.view.ResultContent;
import org.zhao.common.util.view.TablelListUtils;
import org.zhao.service.ZserverExpService;
import org.zhao.usetime.annotation.UseTime;

@Controller
@RequestMapping("/exp/")
public class ServerExpController {

	@Autowired
	private ZserverExpService zServerExpService;
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("index.html")
	public String index(@RequestParam(value="_jr",required=false,defaultValue="")String menuId ,HttpServletRequest request ,Model model) {
		return TablelListUtils.addSessionButtons(menuId, request, model);
	}
	
	@UseTime
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("list.html")
	@ResponseBody
	public String list( @RequestParam(value="_jr",required=false,defaultValue="")String jr , HttpServletRequest request) {
		ResultContent<JSONObject> query = QuerySign.deQuery(jr, request , "page" , "rows");
		PageContext page = new PageContext(query.getJsonInt("page"), query.getJsonInt("rows") , "create_time" , "desc");
		String serverKey = query.getJsonString("serverKey");
		String ipKey = query.getJsonString("ipKey");
		String descKey = query.getJsonString("descKey");
		String beginTime = query.getJsonString("btKey");
		String endTime = query.getJsonString("etKey");
		QueryParames parames = QueryParames.init();
		if(!StringUtils.isEmpty(ipKey))
			parames.addSimilar("queryIp", ipKey);
		if(!StringUtils.isEmpty(serverKey))
			parames.addSimilar("queryServerName", serverKey);
		if(!StringUtils.isEmpty(descKey))
			parames.addSimilar("expDesc", descKey);
		if(!StringUtils.isEmpty(beginTime))
			parames.addGreaterThanAndEquals("createTime", beginTime);
		if(!StringUtils.isEmpty(endTime))
			parames.addLessThanAndEquals("createTime", endTime);
		return BaseResultUtil.resultEasyUi(this.zServerExpService.selectPageListByParameterRequire(page , parames.getParames()));
	}
}
