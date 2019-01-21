package org.zhao.core.common.model;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zhao.core.common.util.HttpUtils;
import org.zhao.core.common.util.RegiestServer;
import org.zhao.core.common.util.SignUtil;

public class PutThread extends Thread{

	private Log logger = LogFactory.getLog(this.getClass());
	
	private Map<String, Object> parame;
	
	private String queryPath;
	
	private String key;
	
	public PutThread(){}
	public PutThread(Map<String, Object> parame , String queryPath , String key) {
		this.parame = parame;
		this.queryPath = queryPath;
		this.key = key;
	}
	
	@Override
	public void run() {
		String url = RegiestServer.queryPutUrl(queryPath);
		String token = RegiestServer.getToken(false , key);
		if(StringUtils.isEmpty(token) || token.equals("-1")) return;
 		parame.put("token", token);
 		Map<String, String> obj2 = new HashMap<String, String>();
		obj2.put("_jr", SignUtil.getHttpContext(JSONObject.fromObject(parame).toString()));
		JSONObject result = HttpUtils.post(url, obj2);
		logger.info(result);
	}

	
}
