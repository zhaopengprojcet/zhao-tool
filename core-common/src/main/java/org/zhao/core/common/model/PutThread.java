package org.zhao.core.common.model;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zhao.core.common.util.HttpUtils;
import org.zhao.core.common.util.RegiestServer;
import org.zhao.core.common.util.SignUtil;

public class PutThread extends Thread{

	private Log logger = LogFactory.getLog(this.getClass());
	
	private Map<String, Object> parame;
	
	private String queryPath;
	
	public PutThread(){}
	public PutThread(Map<String, Object> parame , String queryPath) {
		this.parame = parame;
		this.queryPath = queryPath;
	}
	
	@Override
	public void run() {
		String url = RegiestServer.queryPutUrl(queryPath);
 		parame.put("token", RegiestServer.getToken(false));
 		Map<String, String> obj2 = new HashMap<String, String>();
		obj2.put("_jr", SignUtil.getHttpContext(JSONObject.fromObject(parame).toString()));
		JSONObject result = HttpUtils.post(url, obj2);
		logger.info(result);
	}

	
}
