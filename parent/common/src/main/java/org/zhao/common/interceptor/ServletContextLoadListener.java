package org.zhao.common.interceptor;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.zhao.common.mybatis.query.ParamterRequirement;
import org.zhao.common.pojo.model.ZkeyValueModel;
import org.zhao.common.pojo.model.ZroleModel;
import org.zhao.common.service.ZkvService;
import org.zhao.common.service.ZroleService;
import org.zhao.common.util.CacheUtil;
import org.zhao.common.util.SessionUtil;
import org.zhao.common.util.SpringContextUtil;
import org.zhao.common.util.view.ResultContent;

@Component
@Order(value = 1)
public class ServletContextLoadListener implements ApplicationRunner{

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private ServletContext servletContext;
	@Autowired
	private ZroleService zRoleService;
	@Autowired
	private ZkvService kVService;
	
	@Value("${server.redis.open}")
	private boolean redisOpen;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		if(redisOpen) {
			RedisTemplate redisTemplate = (RedisTemplate) SpringContextUtil.getBean("redisTemplate", RedisTemplate.class);
			CacheUtil.context = redisTemplate;
		}
		else {
			CacheUtil.context = servletContext;
		}
		logger.info("全局数据预加载...");
		ResultContent<List<ZroleModel>> roles = this.zRoleService.selectPageListByParameterRequire(new ZroleModel(), null, new ParamterRequirement().getParamter());
		if(CollectionUtils.isEmpty(roles.getData())) {
			for (ZroleModel role : roles.getData()) {
				CacheUtil.saveSingleCache(role.getId()+"_"+RequestLoginInterceptor.SERVLET_POWER_UPDATE_TIME, new Date().getTime());
			}
		}
		//加载全局参数
		loadKV(this.kVService);
		//加载本机ip
		CacheUtil.saveSingleCache(SessionUtil.WEB_IP, SessionUtil.getInternetIp());
	}

	public static void loadKV(ZkvService kVService) {
		ResultContent<List<ZkeyValueModel>> list = kVService.selectPageListByParameterRequire(new ZkeyValueModel(), null, new ParamterRequirement().getParamter());
		new PublicServerKV(list.getData());
	}
}
