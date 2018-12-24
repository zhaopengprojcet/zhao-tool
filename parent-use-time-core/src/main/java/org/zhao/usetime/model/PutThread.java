package org.zhao.usetime.model;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zhao.usetime.util.HttpUtils;
import org.zhao.usetime.util.RegiestServer;
import org.zhao.usetime.util.SignUtil;

public class PutThread extends Thread{

	private Log logger = LogFactory.getLog(PutThread.class);
	
	private String name;
	private long time;
	
	public PutThread(){}
	public PutThread(String name , long time) {
		this.name = name;
		this.time = time;
	}
		
	@Override
	public void run() {
		String url = RegiestServer.queryPutUrl("/server/putUseTime.html");
 		JSONObject obj = new JSONObject();
 		obj.put("token", RegiestServer.getToken(false));
 		obj.put("from", name);
 		obj.put("times", time);
 		Map<String, String> obj2 = new HashMap<String, String>();
		obj2.put("_jr", SignUtil.getHttpContext(JSONObject.fromObject(obj).toString()));
		JSONObject result = HttpUtils.post(url, obj2);
		logger.info(result);
	}
}
