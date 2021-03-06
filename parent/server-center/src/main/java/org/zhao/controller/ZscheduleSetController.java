package org.zhao.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zhao.common.aspect.query.QuerySchedules;
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.mybatis.query.QueryParames;
import org.zhao.common.pojo.model.ZscheduleSetModel;
import org.zhao.common.role.RoleAop;
import org.zhao.common.role.RoleAopEnum;
import org.zhao.common.schedule.ServerSchedule;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.ThreadPoolUtils;
import org.zhao.common.util.view.QuerySign;
import org.zhao.common.util.view.ResultContent;
import org.zhao.common.util.view.TablelListUtils;
import org.zhao.service.ZscheduleService;

@Controller
@RequestMapping("/scheduleSet/")
public class ZscheduleSetController {

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
	public String list( @RequestParam(value="_jr",required=false,defaultValue="")String jr , HttpServletRequest request) {
		ResultContent<JSONObject> query = QuerySign.deQuery(jr, request , "page" , "rows");
		QueryParames parames = QueryParames.init();
		if(!StringUtils.isEmpty(query.getJsonString("scheduleType"))) {
			parames.addEquality("scheduleType", query.getJsonString("scheduleType"));
		}
		if(!StringUtils.isEmpty(query.getJsonString("scheduleState"))) {
			parames.addEquality("scheduleState", query.getJsonString("scheduleState"));
		}
		if(!StringUtils.isEmpty(query.getJsonString("scheduleName"))) {
			parames.addSimilar("scheduleName", query.getJsonString("scheduleName"));
		}
		if(!StringUtils.isEmpty(query.getJsonString("scheduleKey"))) {
			parames.addSimilar("scheduleKey", query.getJsonString("scheduleKey"));
		}
		PageContext page = new PageContext(query.getJsonInt("page"), query.getJsonInt("rows"));
		return BaseResultUtil.resultEasyUi(this.zScheduleService.selectPageListByParameterRequire(page, parames.getParames()));
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("add.html")
	public String add(Model model) {
		return TablelListUtils.buildUpdate(ZscheduleSetModel.class,"/scheduleSet/save.html", model);
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("update.html")
	public String update(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request , Model model) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "id");
		return TablelListUtils.buildUpdate(ZscheduleSetModel.class,"/scheduleSet/save.html", model , this.zScheduleService.selectScheduleSetById(query.getJsonString("id")).getData());
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("save.html")
	@ResponseBody
	public String save(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		ResultContent<ZscheduleSetModel> query = QuerySign.deQueryToBean(context, request, ZscheduleSetModel.class ,true);
		return BaseResultUtil.result(this.zScheduleService.save(query.getData()));
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("delete.html")
	@ResponseBody
	public String delete(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "id");
		return BaseResultUtil.result(this.zScheduleService.delete(query.getJsonString("id")));
	}
	
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("reload.html")
	@ResponseBody
	public String reload(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		QuerySign.deQuery(context, request);
		ServerSchedule.reload();
		return BaseResultUtil.result(new ResultContent<String>(ResultContent.SUCCESS, "重置完成"));
	}
	
	/**
	 * 主动调用
	 * @param context
	 * @param request
	 * @return
	 */
	@RoleAop(key=RoleAopEnum.POWER)
	@RequestMapping("userPut.html")
	@ResponseBody
	public String userPut(@RequestParam(value="_jr",required=false,defaultValue="")String context , HttpServletRequest request) {
		ResultContent<JSONObject> query = QuerySign.deQuery(context, request , "id");
		ResultContent<ZscheduleSetModel> model = this.zScheduleService.selectModelById(query.getJsonString("id"));
		if(model.getCode().equals(ResultContent.ERROR)) return BaseResultUtil.result(model);
		List<String> services = new ArrayList<String>();
		services.add(model.getData().getScheduleKey());
		ThreadPoolUtils.putThread("定时任务调用扫描", new QuerySchedules(services , null ,model.getData().getPushType() ,zScheduleService , true));
		return  BaseResultUtil.result(new ResultContent<String>(ResultContent.SUCCESS, "调用成功"));
	}
}
