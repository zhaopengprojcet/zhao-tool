package org.zhao.common.controller;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zhao.common.pojo.model.ZmenuButtonModel;
import org.zhao.common.pojo.model.ZmenuFieldModel;
import org.zhao.common.pojo.model.ZmenuModel;
import org.zhao.common.pojo.model.ZmenuSearchModel;
import org.zhao.common.role.RoleAop;
import org.zhao.common.role.RoleAopEnum;
import org.zhao.common.service.ZmenuService;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.view.QuerySign;
import org.zhao.common.util.view.ResultContent;
import org.zhao.common.util.view.TablelListUtils;
import org.zhao.usetime.annotation.UseTime;

@Controller
@RequestMapping("/menu/")
public class MenuController {

	@Autowired
	private ZmenuService zMenuService;
	
	/**
	 * 界面菜单加载
	 * @return
	 */
	@UseTime
	@RoleAop(key=RoleAopEnum.LOGIN)
	@RequestMapping("menuPowerList.html")
	@ResponseBody
	public String menuPowerList() {
		return BaseResultUtil.result(this.zMenuService.selectMenuListByRoleId("1540187710651qyPHa-2FH4Y-s6TNX-9mbFu"));
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
		QuerySign.deQuery(jr, request);
		return BaseResultUtil.resultList(this.zMenuService.selectMenuListOfTree("0"));
	}
	
	@RoleAop(key=RoleAopEnum.LOGIN)
	@RequestMapping("comboList.html")
	@ResponseBody
	public String comboList( @RequestParam(value="_jr",required=false,defaultValue="")String jr , HttpServletRequest request) {
		QuerySign.deQuery(jr, request);
		return BaseResultUtil.resultList(this.zMenuService.selectcomboListMenuOfTree("0"));
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("add.html")
	public String add(Model model) {
		return TablelListUtils.buildUpdate(ZmenuModel.class,"/menu/save.html", model);
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("update.html")
	public String update(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request , Model model) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "id");
		return TablelListUtils.buildUpdate(ZmenuModel.class,"/menu/save.html", model , this.zMenuService.selectMenuById(query.getJsonString("id")).getData());
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("save.html")
	@ResponseBody
	public String save(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		ResultContent<ZmenuModel> query = QuerySign.deQueryToBean(context, request, ZmenuModel.class ,true);
		return BaseResultUtil.result(this.zMenuService.save(query.getData()));
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("delete.html")
	@ResponseBody
	public String delete(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "id");
		return BaseResultUtil.result(this.zMenuService.delete(query.getJsonString("id")));
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("buttonUpdate.html")
	public String buttonUpdate(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request , Model model) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "id");
		model.addAttribute("menuId", query.getJsonString("id"));
		model.addAttribute("data", JSONArray.fromObject(this.zMenuService.selectButtonsForMenuId(query.getJsonString("id")).getData()));
		return "menu/buttonUpdate";
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("buttonSave.html")
	@ResponseBody
	public String buttonSave(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "menuId","buttons");
		return BaseResultUtil.result(this.zMenuService.buttonSave(query.getJsonString("menuId"), query.getJsoinArray("buttons", ZmenuButtonModel.class)));
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("fieldUpdate.html")
	public String fieldUpdate(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request , Model model) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "id");
		model.addAttribute("menuId", query.getJsonString("id"));
		model.addAttribute("data", JSONArray.fromObject(this.zMenuService.selectFieldsForMenuId(query.getJsonString("id")).getData()));
		return "menu/fieldsUpdate";
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("fieldSave.html")
	@ResponseBody
	public String fieldSave(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "menuId","fields");
		return BaseResultUtil.result(this.zMenuService.fieldSave(query.getJsonString("menuId"), query.getJsoinArray("fields", ZmenuFieldModel.class)));
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("searchUpdate.html")
	public String searchUpdate(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request , Model model) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "id");
		model.addAttribute("menuId", query.getJsonString("id"));
		model.addAttribute("data", JSONArray.fromObject(this.zMenuService.selectSearchsForMenuId(query.getJsonString("id")).getData()));
		return "menu/searchUpdate";
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("searchSave.html")
	@ResponseBody
	public String searchSave(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "menuId","searchs");
		return BaseResultUtil.result(this.zMenuService.searchSave(query.getJsonString("menuId"), query.getJsoinArray("searchs", ZmenuSearchModel.class)));
	}
}
