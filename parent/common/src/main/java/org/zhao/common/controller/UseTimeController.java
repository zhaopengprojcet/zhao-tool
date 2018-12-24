package org.zhao.common.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.zhao.common.aspect.query.QueryTimeUse;
import org.zhao.common.interceptor.ServletContextLoadListener;
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.mybatis.query.ParamterRequirement;
import org.zhao.common.pojo.model.ZkeyValueModel;
import org.zhao.common.pojo.model.ZrequestUseModel;
import org.zhao.common.pojo.model.ZuserModel;
import org.zhao.common.role.RoleAop;
import org.zhao.common.role.RoleAopEnum;
import org.zhao.common.service.ZkvService;
import org.zhao.common.service.ZuseTimeService;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.DateUtil;
import org.zhao.common.util.SpringContextUtil;
import org.zhao.common.util.view.QuerySign;
import org.zhao.common.util.view.ResultContent;
import org.zhao.common.util.view.TablelListUtils;

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
		if(query.getData().containsKey("requestTime") && !StringUtils.isEmpty(query.getJsonString("requestTime"))) {
			String date = query.getJsonString("requestTime");
			if(DateUtil.getTimeStr(new Date(), DateUtil.yyyy_MM_dd).equals(date)) {
				return QueryTimeUse.getTimeEasyUiData(queryKey ,page);
			}
			else {
				ZrequestUseModel req = new ZrequestUseModel();
				req.setRequestTime(date);
				ParamterRequirement require = new ParamterRequirement();
				if(!StringUtils.isEmpty(queryKey)) {
					req.setName(queryKey);
					require.addSimilar("name");
				}
				return BaseResultUtil.resultEasyUi(this.zUseTimeService.selectPageList(req, page , require.getParamter()));
			}
		}
		else {
			return QueryTimeUse.getTimeEasyUiData(queryKey ,page);
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
