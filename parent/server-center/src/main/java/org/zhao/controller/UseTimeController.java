package org.zhao.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zhao.common.aspect.query.QueryTimeUse;
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.mybatis.query.ParamterRequirement;
import org.zhao.common.mybatis.query.QueryParames;
import org.zhao.common.pojo.model.ZrequestUseModel;
import org.zhao.common.role.RoleAop;
import org.zhao.common.role.RoleAopEnum;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.DateUtil;
import org.zhao.common.util.view.QuerySign;
import org.zhao.common.util.view.ResultContent;
import org.zhao.common.util.view.TablelListUtils;
import org.zhao.service.ZuseTimeService;

@Controller
@RequestMapping("/ut/")
public class UseTimeController {
	@Autowired
	private ZuseTimeService zUseTimeService;
	
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
		PageContext page = new PageContext(query.getJsonInt("page"), query.getJsonInt("rows") , "times" , "desc");
		String queryKey = query.getJsonString("queryKey");
		String ipKey = query.getJsonString("ipKey");
		String nameKey = query.getJsonString("nameKey");
		if(query.getData().containsKey("requestTime") && !StringUtils.isEmpty(query.getJsonString("requestTime"))) {
			String date = query.getJsonString("requestTime");
			if(DateUtil.getTimeStr(new Date(), DateUtil.yyyy_MM_dd).equals(date)) {
				return QueryTimeUse.getTimeEasyUiData(queryKey , ipKey , nameKey ,page);
			}
			else {
				QueryParames parames = QueryParames.init();
				if(!StringUtils.isEmpty(queryKey)) {
					parames.addSimilar("serviceName", queryKey);
				}
				if(!StringUtils.isEmpty(ipKey)) {
					parames.addSimilar("queryIp", ipKey);
				}
				if(!StringUtils.isEmpty(nameKey)) {
					parames.addSimilar("name", nameKey);
				}
				return BaseResultUtil.resultEasyUi(this.zUseTimeService.selectPageListByParameterRequire(page , parames.getParames()));
			}
		}
		else {
			return QueryTimeUse.getTimeEasyUiData(queryKey , ipKey , nameKey ,page);
		}
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("reload.html")
	@ResponseBody
	public String reload(@RequestParam(value="_jr",required=false,defaultValue="")String jr , HttpServletRequest request) {
		QueryTimeUse.reloadTable();
		return BaseResultUtil.result(new ResultContent<String>(ResultContent.SUCCESS , "重置完成"));
	}
}
