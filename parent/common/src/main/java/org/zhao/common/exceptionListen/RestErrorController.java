package org.zhao.common.exceptionListen;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.zhao.common.util.view.ResultContent;

@RestController
public class RestErrorController implements ErrorController{
	
	@RequestMapping(value = "/error")
    public ModelAndView error(HttpServletRequest request, HttpServletResponse response) {
		MappingJackson2JsonView view = new MappingJackson2JsonView();
		view.addStaticAttribute("code", ResultContent.ERROR);
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		view.addStaticAttribute("message", "操作失败【"+statusCode.intValue()+"】");
        return new ModelAndView(view);
    }

	@Override
	public String getErrorPath() {
		return "/error";
	}
}
