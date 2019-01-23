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
import org.zhao.common.pojo.model.ZregiestClientModel;
import org.zhao.common.role.RoleAop;
import org.zhao.common.role.RoleAopEnum;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.view.QuerySign;
import org.zhao.common.util.view.ResultContent;
import org.zhao.common.util.view.TablelListUtils;
import org.zhao.service.ZregiestClientService;

@Controller
@RequestMapping("/regiestclient/")
public class ZregiestClientController {

	@Autowired
	private ZregiestClientService zRegiestClientService;
	
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
		if(!StringUtils.isEmpty(query.getJsonString("name"))) {
			parames.addSimilar("name", query.getJsonString("name"));
		}
		if(!StringUtils.isEmpty(query.getJsonString("clientDesc"))) {
			parames.addSimilar("clientDesc", query.getJsonString("clientDesc"));
		}
		PageContext page = new PageContext(query.getJsonInt("page"), query.getJsonInt("rows"));
		return BaseResultUtil.resultEasyUi(this.zRegiestClientService.selectPageListByParameterRequire(page, parames.getParames()));
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("add.html")
	public String add(Model model) {
		return TablelListUtils.buildUpdate(ZregiestClientModel.class,"/regiestclient/save.html", model);
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("update.html")
	public String update(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request , Model model) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "id");
		return TablelListUtils.buildUpdate(ZregiestClientModel.class,"/regiestclient/save.html", model , this.zRegiestClientService.selectClientById(query.getJsonString("id")).getData());
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("save.html")
	@ResponseBody
	public String save(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		ResultContent<ZregiestClientModel> query = QuerySign.deQueryToBean(context, request, ZregiestClientModel.class ,true);
		return BaseResultUtil.result(this.zRegiestClientService.save(query.getData()));
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("delete.html")
	@ResponseBody
	public String delete(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "id");
		return BaseResultUtil.result(this.zRegiestClientService.delete(query.getJsonString("id")));
	}
}
