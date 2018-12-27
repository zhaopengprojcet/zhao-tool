package org.zhao.common.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 外接服务 开通登录验证
 * @author zhao
 *
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

	/**
	 * 添加登录效验拦截器
	 * @return
	 */
	@Bean
	public HandlerInterceptor getInterceptor() {
		return new RequestLoginInterceptor();
	}
	
	/**
	 * 黑白名单拦截器
	 * @return
	 */
	@Bean
	public HandlerInterceptor getInterceptor2() {
		return new RequestWBipInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getInterceptor2())
		.addPathPatterns("/")
		.addPathPatterns("/**")
		.excludePathPatterns("/schedule/response.html")
		;
		
		registry.addInterceptor(getInterceptor())
		.addPathPatterns("/")
		.addPathPatterns("/**")
		.excludePathPatterns("/schedule/response.html")
		;
        super.addInterceptors(registry);
	}
}
