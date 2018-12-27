package org.zhao.mcs.model;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zhao.core.common.util.HttpUtils;
import org.zhao.core.common.util.RegiestServer;
import org.zhao.core.common.util.SignUtil;
import org.zhao.mcs.util.MemoryCpuUtil;

public class PutThread extends Thread{

	private Log logger = LogFactory.getLog(PutThread.class);
	
	@Override
	public void run() {
		String url = RegiestServer.queryPutUrl("/server/putMcs.html");
 		JSONObject obj = new JSONObject();
 		obj.put("token", RegiestServer.getToken(false));
 		obj.put("mcs", MemoryCpuUtil.getMemoryCupData());
 		Map<String, String> obj2 = new HashMap<String, String>();
		obj2.put("_jr", SignUtil.getHttpContext(JSONObject.fromObject(obj).toString()));
		JSONObject result = HttpUtils.post(url, obj2);
		logger.info("推送计算机信息结果--->" + result);
	}
}
