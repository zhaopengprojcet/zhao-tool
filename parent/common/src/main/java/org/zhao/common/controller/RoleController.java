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
import org.zhao.common.pojo.model.ZroleModel;
import org.zhao.common.role.RoleAop;
import org.zhao.common.role.RoleAopEnum;
import org.zhao.common.service.ZmenuService;
import org.zhao.common.service.ZroleService;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.view.QuerySign;
import org.zhao.common.util.view.ResultContent;
import org.zhao.common.util.view.RolePowerHtml;
import org.zhao.common.util.view.TablelListUtils;

@Controller
@RequestMapping("/role/")
public class RoleController {

	@Autowired
	private ZroleService zRoleService;
	@Autowired
	private ZmenuService zMenuService;
	
	@RoleAop(key=RoleAopEnum.LOGIN)
	@RequestMapping("comboList.html")
	@ResponseBody
	public String comboList() {
		return BaseResultUtil.resultList(this.zRoleService.selectComboList());
	}
	
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
		PageContext page = new PageContext(query.getJsonInt("page"), query.getJsonInt("rows"));
		if(!StringUtils.isEmpty(query.getJsonString("roleName"))) {
			parames.addSimilar("roleName", query.getJsonString("roleName"));
		}
		return BaseResultUtil.resultEasyUi(this.zRoleService.selectPageListByParameterRequire(page, parames.getParames()));
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("add.html")
	public String add(Model model) {
		return TablelListUtils.buildUpdate(ZroleModel.class,"/role/save.html", model);
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("update.html")
	public String update(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request , Model model) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "id");
		return TablelListUtils.buildUpdate(ZroleModel.class,"/role/save.html", model , this.zRoleService.selectRoleById(query.getJsonString("id")).getData());
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("save.html")
	@ResponseBody
	public String save(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		ResultContent<ZroleModel> query = QuerySign.deQueryToBean(context, request, ZroleModel.class ,true);
		return BaseResultUtil.result(this.zRoleService.save(query.getData() , request));
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("delete.html")
	@ResponseBody
	public String delete(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "id");
		return BaseResultUtil.result(this.zRoleService.delete(query.getJsonString("id") ,request));
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("rolePowerUpdate.html")
	public String rolePowerUpdate(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request,Model model) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "id");
		model.addAttribute("role_key", query.getJsonString("id"));
		model.addAttribute("powers", this.zRoleService.selectRolePowers(query.getJsonString("id")));
		
		model.addAttribute("data", RolePowerHtml.rolePowerHtmls(this.zMenuService.selectAllResourcesList("0").getData()));
		return "role/rolePowerUpdate";
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("powerSave.html")
	@ResponseBody
	public String powerSave(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "role_key");
		return BaseResultUtil.result(this.zRoleService.updatePowers(query.getData() , request));
	}
}
