package org.zhao.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.zhao.common.client.ClientContext;
import org.zhao.common.interceptor.PublicServerKV;
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.mybatis.query.QueryParames;
import org.zhao.common.pojo.model.ZregiestClientModel;
import org.zhao.common.server.ServerConfig;
import org.zhao.common.server.thread.RegiestOtherServerThread;
import org.zhao.common.service.ZLogService;
import org.zhao.common.util.CacheUtil;
import org.zhao.common.util.RandomUtils;
import org.zhao.common.util.SessionUtil;
import org.zhao.common.util.ThreadPoolUtils;
import org.zhao.common.util.view.ResultContent;
import org.zhao.service.ServerService;
import org.zhao.service.ZregiestClientService;

@Service
public class ServerServiceImpl implements ServerService {

	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private ZLogService zLogService;
	@Autowired
	private ZregiestClientService zRegiestClientService;
	
	@Override
	public ResultContent<String> regiestClient(ClientContext client , HttpServletRequest request) {
		if(StringUtils.isEmpty(client.getIp())) return new ResultContent<String>(ResultContent.ERROR, "请求参数缺失【01】");
		if(StringUtils.isEmpty(client.getPort())) return new ResultContent<String>(ResultContent.ERROR, "请求参数缺失【02】");
		if(StringUtils.isEmpty(client.getServiceName())) return new ResultContent<String>(ResultContent.ERROR, "请求参数缺失【03】");
		/*if(client.isFullServer()) { //非其他注册中心转发时需要效验
			//验证ip
			String ip = SessionUtil.getIpAddress(request);
			if(!ip.equals(client.getIp())) {
				logger.info("ip注册失败，源【"+ip+"】请求【"+client.getIp()+"】");
				return new ResultContent<String>(ResultContent.ERROR, "请求参数不匹配【01】");
			}
		}*/
		//验证账号密码
		
		/**
		 * 帐号密码可用更新为分发式 ， 可用建立帐号,为各个服务单独分发
		 * 可用在建立帐号时同时指派白名单地址
		 * 可用防止在不同ip上相同的账号混用
		 */
		QueryParames query = QueryParames.init();
		query.addEquality("name", client.getLoginName());
		query.addEquality("pass", client.getPassword());
		ResultContent<List<ZregiestClientModel>> models = this.zRegiestClientService.selectPageListByParameterRequire(null, query.getParames());
		if(CollectionUtils.isEmpty(models.getData())) {
			logger.info("帐号密码注册失败，请求【"+client.getLoginName()+"|"+client.getPassword()+"】");
			return new ResultContent<String>(ResultContent.ERROR, "无注册权限");
		}
		else {
			ZregiestClientModel m = models.getData().get(0);
			if(!StringUtils.isEmpty(m.getUseIps())) {
				client.setIps(Arrays.asList(m.getUseIps().split(",")));
			}
			if(!StringUtils.isEmpty(m.getPowers())) {
				client.setPowers(Arrays.asList(m.getPowers().split(",")));
			}
		}
		
		client.setRegiestTime(new Date());
		//加入本地
		ServerConfig.addClient(client);
		String regiesToken = RandomUtils.getRandomStrByAll(100);
		if(!StringUtils.isEmpty(client.getToken()))
			regiesToken = client.getToken();
		CacheUtil.saveMapCache(ServerConfig.REGIEST_CLIENT_TOKEN, regiesToken, client);
		
		//推送到其他注册中心
		if(client.isFullServer()) {
			client.setFullServer(false);
			client.setToken(regiesToken);
			ThreadPoolUtils.putThread("regiestFull【"+client.getServiceName()+"】", new RegiestOtherServerThread(client , this.zLogService));
		}
		logger.info("注册完成，当前注册服务内容【"+JSONObject.fromObject(ServerConfig.getClients()).toString()+"】身份编码【"+regiesToken+"】");
		
		return new ResultContent<String>(ResultContent.SUCCESS, "注册成功" , regiesToken);
	}

	@Override
	public ResultContent<String> regiestClientList(String key  , PageContext page) {
		Map<String, Set<String>> clients = ServerConfig.getClients();
		JSONArray rows = new JSONArray();
		int count = 0;
		int index = 0;
		if(clients != null && clients.size() > 0) {
			for (Map.Entry<String, Set<String>> entry : clients.entrySet()) { 
			  if(index >= page.getStart().intValue() && index < (page.getStart().intValue() + page.getCount().intValue())) {
				  if(!StringUtils.isEmpty(key)) {
					  if(entry.getKey().toLowerCase().indexOf(key.toLowerCase()) != -1) {
						  JSONObject row = new JSONObject();
						  row.put("key", entry.getKey());
						  row.put("clients", org.apache.commons.lang.StringUtils.join(entry.getValue(), ","));
						  rows.add(row);
						  index++;
					  }
					  else {
						  continue;
					  }
				  } else {
					  JSONObject row = new JSONObject();
					  row.put("key", entry.getKey());
					  row.put("clients", org.apache.commons.lang.StringUtils.join(entry.getValue(), ","));
					  rows.add(row);
				  }
			  }
			  if(StringUtils.isEmpty(key))
				  index++;
			}
			count = clients.size();
		}
		JSONObject datas = new JSONObject();
		datas.put("rows", rows);
		datas.put("total", count);
		return new ResultContent<String>(datas.toString());
	}

	
}
