package org.zhao.controller;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zhao.common.aspect.query.QuerySchedules;
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.role.RoleAop;
import org.zhao.common.role.RoleAopEnum;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.view.QuerySign;
import org.zhao.common.util.view.ResultContent;
import org.zhao.common.util.view.TablelListUtils;

/**
 * 缓存中 已注册定时任务数据
 * @author zhao
 *
 */
@Controller
@RequestMapping("/scdr/")
public class ZscheduleRegiestController {

	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("index.html")
	public String index(@RequestParam(value="_jr",required=false,defaultValue="")String menuId ,HttpServletRequest request ,Model model) {
		return TablelListUtils.addSessionButtons(menuId, request, model);
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("list.html")
	@ResponseBody
	public String list( @RequestParam(value="_jr",required=false,defaultValue="")String jr , HttpServletRequest request) {
		ResultContent<JSONObject> query = QuerySign.deQuery(jr, request , "page" , "rows");
		PageContext page = new PageContext(query.getJsonInt("page"), query.getJsonInt("rows"));
		String scheduleName = query.getJsonString("scheduleName");
		String serverNames = query.getJsonString("serverNames");
		return QuerySchedules.getPageList(scheduleName, serverNames, page);
		
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("reload.html")
	@ResponseBody
	public String reload(@RequestParam(value="_jr",required=false,defaultValue="")String jr , HttpServletRequest request) {
		QuerySchedules.reloadSchedules();
		return BaseResultUtil.result(new ResultContent<String>(ResultContent.SUCCESS , "重置完成"));
	}
}
