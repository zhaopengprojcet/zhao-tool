package org.zhao.common.interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.zhao.common.pojo.model.ZkeyValueModel;

public class PublicServerKV {

	private Logger logger = Logger.getLogger(this.getClass());
	
	private static Map<String, String> map = new HashMap<String, String>();
	
	public PublicServerKV(List<ZkeyValueModel> list) {
		logger.info("加载全局kv...");
		if(CollectionUtils.isNotEmpty(list)) {
			for (ZkeyValueModel zkeyValueModel : list) {
				logger.info("【"+zkeyValueModel.getServerKey()+"----->"+zkeyValueModel.getServerValue()+"】");
				map.put(zkeyValueModel.getServerKey(), zkeyValueModel.getServerValue());
			}
		}
	}
	
	public static int getIntVal(String key) {
		if(map.containsKey(key)) return Integer.parseInt(map.get(key));
		else throw new RuntimeException("【"+key+"】"+"value值应该为整数");
	}
	
	public static boolean getBooleanVal(String key) {
		if(map.containsKey(key)) return Boolean.parseBoolean(map.get(key));
		else throw new RuntimeException("【"+key+"】"+"value值应该为布尔数");
	}
	
	public static String getStringVal(String key) {
		if(map.containsKey(key)) return map.get(key);
		else throw new RuntimeException("【"+key+"】"+"不存在");
	}
	
	public static double getDoubleVal(String key) {
		if(map.containsKey(key)) return Double.parseDouble(map.get(key));
		else throw new RuntimeException("【"+key+"】"+"value值应该为浮点数");
	}
}
