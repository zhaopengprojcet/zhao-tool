package org.zhao.common.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.zhao.common.pojo.model.ZmenuModel;
import org.zhao.common.pojo.model.ZuserModel;
import org.zhao.common.role.RoleAop;
import org.zhao.common.role.RoleAopEnum;
import org.zhao.common.service.ZmenuService;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.CookieUtil;
import org.zhao.common.util.RandomUtils;
import org.zhao.common.util.SessionUtil;
import org.zhao.common.util.view.ResultContent;

@Component("requestLogin")
public class RequestLoginInterceptor implements HandlerInterceptor{

	private Logger logger = Logger.getLogger(RequestLoginInterceptor.class);
	@Autowired
	private ZmenuService zMenuService;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if(request.getSession().getAttribute(CookieUtil.QUERY_COOKIE_NAME) == null) {
			Cookie query = CookieUtil.getCookieByName(request, CookieUtil.QUERY_COOKIE_NAME);
			if(query != null) {
				request.getSession().setAttribute(CookieUtil.QUERY_COOKIE_NAME, query.getValue());
    			CookieUtil.setCookie(response, CookieUtil.QUERY_COOKIE_NAME, query.getValue(), 60 * 60 * 3 , false);
			}
			else {
				String keys = RandomUtils.getRandomStrByAll(16);
    			request.getSession().setAttribute(CookieUtil.QUERY_COOKIE_NAME, keys);
    			CookieUtil.setCookie(response, CookieUtil.QUERY_COOKIE_NAME, keys, 60 * 60 * 3 , false);
			}
		}
		
		if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            if(method.hasMethodAnnotation(RoleAop.class)) {
            	RoleAop role = method.getMethodAnnotation(RoleAop.class);
            	if(role.key() == RoleAopEnum.ALL) return true;
            }
		}
		
		if(request.getSession().getAttribute(SessionUtil.LOGIN_MEMBER) == null) {
			//未登录
			this.returnToLoginPage(request, response);
			return false;
		}
		
		ZuserModel user = (ZuserModel) request.getSession().getAttribute(SessionUtil.LOGIN_MEMBER);
		
		this.checkLocalTime(request , user.getRoleId()); //效验权限时间
		
		//获取在构建列表页面的权限数据
		if(request.getSession().getAttribute(SessionUtil.ROLE_POWERS) == null) { 
			ResultContent<List<ZmenuModel>> powers = this.zMenuService.findResourcesByRoleId(user.getRoleId());
			if(powers != null && powers.getCode().equals(ResultContent.SUCCESS)) {
				request.getSession().setAttribute(SessionUtil.ROLE_POWERS, powers.getData());
			}
			else {
				request.getSession().setAttribute(SessionUtil.ROLE_POWERS, new ArrayList<ZmenuModel>());
			}
		}
		
		//获取请求的权限数据
		if(request.getSession().getAttribute(SessionUtil.POWER_LIST) == null) { 
			ResultContent<List<String>> powers = this.zMenuService.selectAllPowerKeyByRoleId(user.getRoleId());
			if(powers.getCode().equals(ResultContent.SUCCESS)) {
				Set<String> all = new HashSet<String>();
				for (String p : powers.getData()) {
					if(!StringUtils.isEmpty(p)) {
						String[] ps = p.split(",");
						for (String sp : ps) {
							if(!StringUtils.isEmpty(sp)) all.add(sp);
						}
					}
				}
				
				request.getSession().setAttribute(SessionUtil.POWER_LIST,new ArrayList<String>(all));
			}
			else {
				request.getSession().setAttribute(SessionUtil.POWER_LIST, new ArrayList<String>());
			}
		}
		
		if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            if(method.hasMethodAnnotation(RoleAop.class)) {
            	RoleAop role = method.getMethodAnnotation(RoleAop.class);
            	if(role.key() == RoleAopEnum.LOGIN) return true;
            	else if(role.key() == RoleAopEnum.POWER) {
            		List<String> powers = (List<String>) request.getSession().getAttribute(SessionUtil.POWER_LIST);
            		if(powers == null || powers.size() < 1) {
        				this.noPowersRest(response);
        		        return false;
        			}
            		if(powers.contains(request.getRequestURI())) {
        				return true;
        			}
        			else {
        				logger.info("无权限【"+request.getRequestURI()+"】");
        				this.noPowersRest(response);
        		        return false;
        			}
            	}
            }
		}
		return true;
	}
	
	private void noPowersRest(HttpServletResponse response) {
		PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR, "拒绝访问,该资源包含未被授权的信息")));
        } catch (IOException e) {
        } finally {
            if (writer != null)
                writer.close();
        }
	}
	
	private void returnToLoginPage(HttpServletRequest request , HttpServletResponse response) {
		try {
			response.sendRedirect("/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 更新servlet中定义的一个时间变量，当时间变量改变时，所有的新访问请求都会因为session中缓存的时间与全局不一致而强制刷新权限
	 * @param request
	 */
	public static final String SERVLET_POWER_UPDATE_TIME = "SERVLET_POWER_UPDATE_TIME";
	
	public static void updateServletPowerStatic(HttpServletRequest request , String roleKey) {
		request.getSession().getServletContext().setAttribute(roleKey + "_" + SERVLET_POWER_UPDATE_TIME, new Date().getTime());
	}
	//删除角色时删除时间段
	public static void deleteServletPowerStatic(HttpServletRequest request , String roleKey) {
		request.getSession().getServletContext().removeAttribute(roleKey + "_" + SERVLET_POWER_UPDATE_TIME);
	}
	
	//权限时间效验
	private void checkLocalTime(HttpServletRequest request , String roleKey) {
		if(request.getSession().getAttribute(SessionUtil.LESSION_TIME) == null || request.getServletContext().getAttribute(SERVLET_POWER_UPDATE_TIME) == null) {
			//未获得全局权限同步时间
			//删除原有资源权限缓存
			request.getSession().removeAttribute(SessionUtil.ROLE_POWERS);
			//删除原有访问请求权限
			request.getSession().removeAttribute(SessionUtil.POWER_LIST);
			//同步时间
			request.getSession().setAttribute(SessionUtil.LESSION_TIME, request.getServletContext().getAttribute(roleKey + "_" + SERVLET_POWER_UPDATE_TIME));
			
		}
		else {
			//已有同步时间
			long localTime = (long) request.getSession().getAttribute(SessionUtil.LESSION_TIME);
			long serverTime = (long) (request.getServletContext().getAttribute(SERVLET_POWER_UPDATE_TIME) == null ? -1 : request.getServletContext().getAttribute(SERVLET_POWER_UPDATE_TIME));
			if(localTime != serverTime) {
				request.getSession().removeAttribute(SessionUtil.ROLE_POWERS);
				request.getSession().removeAttribute(SessionUtil.POWER_LIST);
				request.getSession().setAttribute(SessionUtil.LESSION_TIME, request.getServletContext().getAttribute(roleKey + "_" + SERVLET_POWER_UPDATE_TIME));
			}
		}
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
