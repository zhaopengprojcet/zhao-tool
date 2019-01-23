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
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.mybatis.query.QueryParames;
import org.zhao.common.pojo.model.ZuserModel;
import org.zhao.common.role.RoleAop;
import org.zhao.common.role.RoleAopEnum;
import org.zhao.common.service.ZuserService;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.view.QuerySign;
import org.zhao.common.util.view.ResultContent;
import org.zhao.common.util.view.TablelListUtils;

@Controller
@RequestMapping("/user/")
public class UserController {
	@Autowired
	private ZuserService zUserService;
	
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
		if(!StringUtils.isEmpty(query.getJsonString("userState"))) {
			parames.addEquality("userState", query.getJsonString("userState"));
		}
		if(!StringUtils.isEmpty(query.getJsonString("loginName"))) {
			parames.addSimilar("loginName", query.getJsonString("loginName"));
		}
		PageContext page = new PageContext(query.getJsonInt("page"), query.getJsonInt("rows"));
		return BaseResultUtil.resultEasyUi(this.zUserService.selectPageListByParameterRequire(page, parames.getParames()));
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("add.html")
	public String add(Model model) {
		return TablelListUtils.buildUpdate(ZuserModel.class,"/user/save.html", model);
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("update.html")
	public String update(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request , Model model) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "id");
		return TablelListUtils.buildUpdate(ZuserModel.class,"/user/save.html", model , this.zUserService.selectUserById(query.getJsonString("id")).getData());
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("save.html")
	@ResponseBody
	public String save(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		ResultContent<ZuserModel> query = QuerySign.deQueryToBean(context, request, ZuserModel.class ,true);
		return BaseResultUtil.result(this.zUserService.save(query.getData()));
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("delete.html")
	@ResponseBody
	public String delete(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "id");
		return BaseResultUtil.result(this.zUserService.delete(query.getJsonString("id")));
	}
	
	@RoleAop(key=RoleAopEnum.LOGIN)
	@RequestMapping("passUpdate.html")
	@ResponseBody
	public String passUpdate(@RequestParam(value="_jr",required=false,defaultValue="")String context ,HttpServletRequest request) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "upuo" , "upun" , "upudn");
		return BaseResultUtil.result(this.zUserService.updatePass(query.getJsonString("upuo"), query.getJsonString("upun"), query.getJsonString("upudn"), request));
		
	}
}
