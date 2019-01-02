package org.zhao.common.schedule.put;

import java.security.PublicKey;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zhao.common.aspect.query.QueryTimeUse;
import org.zhao.common.client.ClientContext;
import org.zhao.common.interceptor.PublicServerKV;
import org.zhao.common.mybatis.query.QueryParames;
import org.zhao.common.server.ServerConfig;
import org.zhao.common.util.CacheUtil;
import org.zhao.schedule.annotation.ScheduleMethod;
import org.zhao.schedule.annotation.zhaoScheduleBean;
import org.zhao.schedule.model.ReturnCode;
import org.zhao.service.ZuseTimeService;

/**
 * 耗时数据持久化
 * @author zhao
 *
 */
@zhaoScheduleBean
public class SchedulePut {

	private Log logger = LogFactory.getLog(this.getClass());
	@Autowired
	private ZuseTimeService zUseTimeService;
	
	/**
	 * 耗时持久化
	 * @return
	 */
	@ScheduleMethod(scheduleName = "put_time_save")
	public String savePutTime() {
		try {
			zUseTimeService.save(QueryTimeUse.getSaveList());
			QueryTimeUse.clearAll();
		} catch (Exception e) {
			e.printStackTrace();
			return ReturnCode.error(e.getLocalizedMessage());
		}
		return ReturnCode.SUCCESS;
	}
	
	/**
	 * token过期删除
	 * @return
	 */
	@ScheduleMethod(scheduleName = "delete_outtime_token")
	public String deleteOutTimeToken() {
		try {
			logger.info("删除过期token开始");
			Map<String , ClientContext> clients = (Map<String, ClientContext>) CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, "*");
			logger.info("原注册内容【"+JSONObject.fromObject(clients).toString()+"】");
			if(clients != null && clients.size() > 0) {
				logger.info("----------------------删除列表----------------");
				for(String key : clients.keySet()) {
					ClientContext cl = clients.get(key);
					if(System.currentTimeMillis() - cl.getRegiestTime().getTime() >= PublicServerKV.getIntVal("server-center.token.outTime")) { //token2小时过期
						CacheUtil.removeMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, key);
						Set<String> ips = (Set<String>) CacheUtil.getMapSetCache(ServerConfig.REGIEST_CLIENT_LIST, cl.getServiceName());
						ips.remove(cl.getIp());
						CacheUtil.saveMapSetCache(ServerConfig.REGIEST_CLIENT_LIST, cl.getServiceName(), ips);
						logger.info("【"+key+"】"+JSONObject.fromObject(cl).toString());
					}
				}
			}
			logger.info("剩余注册内容【"+JSONObject.fromObject(CacheUtil.getMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, "*")).toString()+"】");
			logger.info("删除过期token结束");
		} catch (Exception e) {
			e.printStackTrace();
			return ReturnCode.error(e.getLocalizedMessage());
		}
		return ReturnCode.SUCCESS;
	}
}
