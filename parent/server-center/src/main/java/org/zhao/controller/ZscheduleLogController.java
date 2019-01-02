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
import org.zhao.common.mybatis.query.QueryParames;
import org.zhao.common.role.RoleAop;
import org.zhao.common.role.RoleAopEnum;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.view.QuerySign;
import org.zhao.common.util.view.ResultContent;
import org.zhao.common.util.view.TablelListUtils;
import org.zhao.service.ZscheduleService;

@Controller
@RequestMapping("/scheduleLog/")
public class ZscheduleLogController {

	@Autowired
	private ZscheduleService zScheduleService;
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("index.html")
	public String index(@RequestParam(value="_jr",required=false,defaultValue="")String menuId ,HttpServletRequest request ,Model model) {
		return TablelListUtils.addSessionButtons(menuId, request, model);
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("list.html")
	@ResponseBody
	public String list(@RequestParam(value="_jr",required=false,defaultValue="")String jr , HttpServletRequest request) {
		ResultContent<JSONObject> query = QuerySign.deQuery(jr, request , "page" , "rows");
		QueryParames parames = QueryParames.init();
		if(!StringUtils.isEmpty(query.getJsonString("scheduleName"))) {
			parames.addSimilar("scheduleName", query.getJsonString("scheduleName"));
		}
		if(!StringUtils.isEmpty(query.getJsonString("putServer"))) {
			parames.addSimilar("putServer", query.getJsonString("putServer"));
		}
		if(!StringUtils.isEmpty(query.getJsonString("putTimeBegin"))) {
			parames.addGreaterThanAndEquals("putTime", query.getJsonString("putTimeBegin"));
		}
		if(!StringUtils.isEmpty(query.getJsonString("putTimeEnd"))) {
			parames.addLessThanAndEquals("putTime", query.getJsonString("putTimeEnd"));
		}
		PageContext page = new PageContext(query.getJsonInt("page"), query.getJsonInt("rows") ,"put_time" , PageContext.DESC);
		return BaseResultUtil.resultEasyUi(this.zScheduleService.selectLogPageListByParameterRequire(page, parames.getParames()));
	}
}
