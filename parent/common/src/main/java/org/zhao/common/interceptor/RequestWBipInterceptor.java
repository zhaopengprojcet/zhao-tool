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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.zhao.common.mybatis.query.ParamterRequirement;
import org.zhao.common.mybatis.query.QueryParames;
import org.zhao.common.pojo.model.ZmenuModel;
import org.zhao.common.pojo.model.ZuserModel;
import org.zhao.common.pojo.model.ZwhiteBlackIpList;
import org.zhao.common.role.RoleAop;
import org.zhao.common.role.RoleAopEnum;
import org.zhao.common.service.ZmenuService;
import org.zhao.common.service.ZwhiteBlackIpService;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.CookieUtil;
import org.zhao.common.util.RandomUtils;
import org.zhao.common.util.SessionUtil;
import org.zhao.common.util.view.ResultContent;

/**
 * 黑白名单
 * @author zhao
 *
 */
@Component
public class RequestWBipInterceptor implements HandlerInterceptor{

	private Logger logger = Logger.getLogger(RequestWBipInterceptor.class);
	@Autowired
	private ZwhiteBlackIpService zWhiteBlackIpService;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if(PublicServerKV.getBooleanVal("server.wip.open")) {//白
			if(request.getServletContext().getAttribute(SessionUtil.WHITE_IP_LIST) == null) {
				loadIps(request , "1" , SessionUtil.WHITE_IP_LIST , this.zWhiteBlackIpService);
			}
			List<ZwhiteBlackIpList> wh = (List<ZwhiteBlackIpList>) request.getServletContext().getAttribute(SessionUtil.WHITE_IP_LIST);
			String requestIp = SessionUtil.getIpAddress(request);
			for (ZwhiteBlackIpList zwhiteBlackIpList : wh) {
				if(zwhiteBlackIpList.getIp().indexOf(requestIp) != -1) return true;
			}
			noPowersRest(response);
			logger.info("白名单拦截【"+requestIp+"】");
			return false;
		}
		else if(PublicServerKV.getBooleanVal("server.bip.open")) { //黑
			if(request.getServletContext().getAttribute(SessionUtil.BLACK_IP_LIST) == null) {
				loadIps(request , "2" , SessionUtil.BLACK_IP_LIST , this.zWhiteBlackIpService);
			}
			List<ZwhiteBlackIpList> bl = (List<ZwhiteBlackIpList>) request.getServletContext().getAttribute(SessionUtil.BLACK_IP_LIST);
			String requestIp = SessionUtil.getIpAddress(request);
			for (ZwhiteBlackIpList zwhiteBlackIpList : bl) {
				if(zwhiteBlackIpList.getIp().indexOf(requestIp) != -1) {
					noPowersRest(response);
					logger.info("黑名单拦截【"+requestIp+"】");
					return false;
				}
			}
			return true;
		}
		return true;
	}
	
	public static void loadIps(HttpServletRequest request , String type , String sessionSaveName ,ZwhiteBlackIpService zWhiteBlackIpService) {
		QueryParames parames = QueryParames.init();
		parames.addEquality("bwType", type);
		ResultContent<List<ZwhiteBlackIpList>> list = zWhiteBlackIpService.selectPageListByParameterRequire(null, parames.getParames());
		if(CollectionUtils.isNotEmpty(list.getData())) {
			request.getServletContext().setAttribute(sessionSaveName, list.getData());
		}
		else {
			request.getServletContext().setAttribute(sessionSaveName, new ArrayList<ZwhiteBlackIpList>());
		}
	}
	
	private void noPowersRest(HttpServletResponse response) {
		PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR, "拒绝访问,您的请求被拒绝")));
        } catch (IOException e) {
        } finally {
            if (writer != null)
                writer.close();
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
