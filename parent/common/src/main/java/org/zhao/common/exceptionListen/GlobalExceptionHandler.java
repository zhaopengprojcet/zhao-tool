package org.zhao.common.exceptionListen;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.zhao.common.interceptor.PublicServerKV;
import org.zhao.common.util.BaseResultUtil;
import org.zhao.common.util.view.ResultContent;


/**
 * 异常处理
 * @author zhao
 *
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler implements HandlerExceptionResolver ,Ordered{
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		ex.printStackTrace();
		MappingJackson2JsonView view = new MappingJackson2JsonView();
		view.addStaticAttribute("code", ResultContent.ERROR);
		if(PublicServerKV.getBooleanVal("springmvc.exception.debug"))
			view.addStaticAttribute("message", "操作失败【"+ex.getMessage()+"】");
		else
			view.addStaticAttribute("message", "操作失败【"+specialExceptionResolve(ex, request)+"】");
		return new ModelAndView(view);
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
	
	@ExceptionHandler(value=RuntimeException.class)
	public String otherExceptionHandler(RuntimeException runtimeException) {
		runtimeException.printStackTrace();
		return BaseResultUtil.result(new ResultContent<String>(ResultContent.ERROR, "操作失败【"+runtimeException.getLocalizedMessage()+"】"));
	}
	
	@SuppressWarnings("deprecation")
	private String specialExceptionResolve(Exception ex, HttpServletRequest request) {
        try {
            if (ex instanceof NoSuchRequestHandlingMethodException 
                || ex instanceof NoHandlerFoundException) {
                return "404";
            }
            else if (ex instanceof HttpRequestMethodNotSupportedException) {
                return "405";
            }
            else if (ex instanceof HttpMediaTypeNotSupportedException) {
                return "not post";
            }
            else if (ex instanceof HttpMediaTypeNotAcceptableException) {
                return "406";
            }
            else if (ex instanceof MissingPathVariableException) {
                return "missing URI templ";
            }
            else if (ex instanceof MissingServletRequestParameterException) {
                return "400";
            }
            else if (ex instanceof ServletRequestBindingException) {
                return "bind error";
            }
            else if (ex instanceof ConversionNotSupportedException) {
                return "500";
            }
            else if (ex instanceof TypeMismatchException) {
                return "400";
            }
            else if (ex instanceof HttpMessageNotReadableException) {
                return "400";
            }
            else if (ex instanceof HttpMessageNotWritableException) {
                return "400";
            }
            else if (ex instanceof MethodArgumentNotValidException) {
                return "validation error";
            }
            else if (ex instanceof MissingServletRequestPartException) {
                return "400";
            }
            else if (ex instanceof BindException) {
            	return "bind error";
            }
        } catch (Exception handlerException) {
        	return "未知的错误";
        }
        return "请求错误";
    }
}
