package org.zhao.common.server.thread;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.zhao.common.client.ClientContext;
import org.zhao.common.server.ServerConfig;
import org.zhao.common.service.ZLogService;
import org.zhao.common.util.HttpUtils;
import org.zhao.common.util.LogUtil;
import org.zhao.common.util.SignUtil;
import org.zhao.common.util.view.ResultContent;

public class RegiestOtherServerThread extends Thread{

	private ClientContext client;
	public RegiestOtherServerThread(){}
	public RegiestOtherServerThread(ClientContext client , ZLogService service) {
		this.client = client;
		this.zLogService = service;
	}
	private ZLogService zLogService;
	
	@Override
	public void run() {
		String[] servers = ServerConfig.getServerPaths();
		client.setFullServer(false);
		String par = SignUtil.getHttpContext(JSONObject.fromObject(client).toString());
		for (String string : servers) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("_jr", par);
			ResultContent<String> result = HttpUtils.post(string+"/server/regiest.html", map);
			if(result.getCode().equals(ResultContent.ERROR)) {
				//加入错误日志
				this.zLogService.insertLog(LogUtil.ERROR, LogUtil.REGIEST_FULL_SERVER, "【"+string+"】" + result.getMessage());
			}
		}
	}

	
}
