package org.zhao.common.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zhao.common.pojo.model.ZuserModel;
import org.zhao.common.role.RoleAop;
import org.zhao.common.role.RoleAopEnum;
import org.zhao.common.service.ZuserService;
import org.zhao.common.util.RandomUtils;
import org.zhao.common.util.SessionUtil;
import org.zhao.common.util.view.ResultContent;
import org.zhao.usetime.annotation.UseTime;

@Controller
public class BaseController {

	@Autowired
	private ZuserService zUserService;
	
	@RoleAop(key=RoleAopEnum.ALL)
	@RequestMapping("/")
	public String in() {
		return "redirect:/login.html";
	}
	
	@UseTime
	@RoleAop(key=RoleAopEnum.ALL)
	@RequestMapping("/login.html")
	public String login(@RequestParam(value="loginName",defaultValue="",required=false)String loginName ,
						@RequestParam(value="password",defaultValue="",required=false)String password ,
						@RequestParam(value="code",defaultValue="",required=false)String code ,
						HttpServletRequest request ,
						Model model) {
		if(StringUtils.isEmpty(code) && StringUtils.isEmpty(password) && StringUtils.isEmpty(loginName))
			return "login";
		if(StringUtils.isEmpty(loginName)) {
			model.addAttribute("error_msg", "登录名不能为空");
			return "login";
		}
		if(StringUtils.isEmpty(password)) {
			model.addAttribute("error_msg", "登录密码不能为空");
			return "login";
		}
		if(StringUtils.isEmpty(code)) {
			model.addAttribute("error_msg", "验证码不能为空");
			return "login";
		}
		if(!RandomUtils.validateCode(code, request)) {
			model.addAttribute("error_msg", "验证码错误");
			return "login";
		}
		ResultContent<ZuserModel> user = this.zUserService.checkUserLogin(loginName, password);
		if(user.getCode().equals(ResultContent.ERROR)) {
			model.addAttribute("error_msg", user.getMessage());
			return "login";
		}
		ZuserModel loginMember = user.getData();
		loginMember.setPassword("");
		request.getSession().setAttribute(SessionUtil.LOGIN_MEMBER, loginMember);
		
		return "redirect:/index.html";
	}
	
	@UseTime
	@RoleAop(key=RoleAopEnum.LOGIN)
	@RequestMapping("/index.html")
	public String index() {
		return "index";
	}
	
	@UseTime
	@RoleAop(key=RoleAopEnum.ALL)
	@RequestMapping("/drawCode.sv")
	public void drawCode(HttpServletRequest request , HttpServletResponse reponse) {
		RandomUtils.drawPic(request, reponse);
	}
	
	
	
	
	/**  工具  ***/
	@RoleAop(key=RoleAopEnum.ALL)
	@RequestMapping("icoIndex.html")
	public String icoIndex(Model model) {
		try {
			StringBuffer icos = new StringBuffer();
			RandomAccessFile raf = new RandomAccessFile(ResourceUtils.getFile("classpath:static/jquery-easyui/themes/icon.css"), "r");
			raf.seek(0);
			for(int i = 0 ; i < raf.length() ; i++) {
				String css = raf.readLine();
				if(!StringUtils.isEmpty(css))
					icos.append("<div><span style=\"width:20px;height:20px;\" class=\"l-btn-left l-btn-icon-left\"><span class=\"l-btn-icon "+css.substring(1, css.indexOf("{"))+"\">&nbsp;</span></span></div>");
			}
			model.addAttribute("icos", icos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "util/icoIndex";
	}
}
