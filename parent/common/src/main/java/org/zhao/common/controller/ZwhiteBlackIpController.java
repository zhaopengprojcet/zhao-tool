package org.zhao.common.controller;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zhao.common.interceptor.RequestWBipInterceptor;
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.mybatis.query.QueryParames;
import org.zhao.common.pojo.model.ZwhiteBlackIpList;
import org.zhao.common.role.RoleAop;
import org.zhao.common.role.RoleAopEnum;
import org.zhao.common.service.ZwhiteBlackIpService;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.SessionUtil;
import org.zhao.common.util.view.QuerySign;
import org.zhao.common.util.view.ResultContent;
import org.zhao.common.util.view.TablelListUtils;

@Controller
@RequestMapping("/wbip/")
public class ZwhiteBlackIpController {
	
	@Autowired
	private ZwhiteBlackIpService zWhiteBlackIpService;
	
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
		QueryParames parames = QueryParames.init();
		if(!StringUtils.isEmpty(query.getJsonString("addDesc"))) {
			parames.addSimilar("addDesc", query.getJsonString("addDesc"));
		}
		if(!StringUtils.isEmpty(query.getJsonString("ip"))) {
			parames.addSimilar("ip", query.getJsonString("ip"));
		}
		if(!StringUtils.isEmpty(query.getJsonString("bwType"))) {
			parames.addEquality("bwType", query.getJsonString("bwType"));
		}
		PageContext page = new PageContext(query.getJsonInt("page"), query.getJsonInt("rows"));
		return BaseResultUtil.resultEasyUi(this.zWhiteBlackIpService.selectPageListByParameterRequire(page, parames.getParames()));
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("add.html")
	public String add(Model model) {
		return TablelListUtils.buildUpdate(ZwhiteBlackIpList.class,"/wbip/save.html", model);
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("update.html")
	public String update(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request , Model model) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "id");
		return TablelListUtils.buildUpdate(ZwhiteBlackIpList.class,"/wbip/save.html", model , this.zWhiteBlackIpService.selectIpById(query.getJsonString("id")).getData());
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("save.html")
	@ResponseBody
	public String save(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		ResultContent<ZwhiteBlackIpList> query = QuerySign.deQueryToBean(context, request, ZwhiteBlackIpList.class ,true);
		return BaseResultUtil.result(this.zWhiteBlackIpService.save(query.getData())); 
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("delete.html")
	@ResponseBody
	public String delete(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "id");
		return BaseResultUtil.result(this.zWhiteBlackIpService.delete(query.getJsonString("id")));
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("rold.html")
	@ResponseBody
	public String rold(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		RequestWBipInterceptor.loadIps(request, "1", SessionUtil.WHITE_IP_LIST, this.zWhiteBlackIpService);
		RequestWBipInterceptor.loadIps(request, "2", SessionUtil.BLACK_IP_LIST, this.zWhiteBlackIpService);
		return BaseResultUtil.result(new ResultContent<String>(ResultContent.SUCCESS, "重置完成"));
	}
}
