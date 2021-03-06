package org.zhao.common.properties;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class ConfigProperties {

	private static Map<String, Object> cache;
	private static Logger log = Logger.getLogger(ConfigProperties.class);
	
	private static ConfigProperties protoConfig;
	
	public static ConfigProperties instance(){
		if (protoConfig == null) {
			protoConfig = new ConfigProperties();
		}
		return protoConfig;
	}
	
	private ConfigProperties() {
		if (cache == null) {
			cache = new HashMap<String, Object>();
			readPropertiesBySpring();
		}
		
	}
	
	public  String getPropertiesVal(String key) {
		return cache.get(key) == null ? null :(String)cache.get(key);
	}
	
	public int getPropertiesInt(String key) {
		return Integer.parseInt(cache.get(key) == null ? "0" : cache.get(key).toString());
	}
	
	public  boolean getPropertiesBoolean(String key) {
		return Boolean.parseBoolean(cache.get(key) == null ?"false":cache.get(key).toString());
	}
	
	private void readPropertiesBySpring() {
		PathMatchingResourcePatternResolver load = new PathMatchingResourcePatternResolver();
		Resource[] resources = null;
		try {
			resources = load
					.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
							//+ "config-final-static-*.properties");
							+ "*.properties");
		} catch (IOException e) {
			log.error("配置文件获取错误:", e);
		}
		Properties allProperties = new Properties();
		if (ArrayUtils.isNotEmpty(resources)) {
			for (Resource resource : resources) {
				try {
					log.info("config-final-static.*.properties------>"
							+ resource.getFilename());
					Properties eachProperties = new Properties();
					eachProperties.load(resource.getInputStream());
					allProperties.putAll(eachProperties);
				} catch (IOException e) {
					log.error("配置文件获取错误:", e);
				}
			}
		}

		Enumeration en = allProperties.propertyNames();
		while (en.hasMoreElements()) {
			try {
				String key = (String) en.nextElement();
				String property;
				property = new String(allProperties.getProperty(key).getBytes(
						"ISO-8859-1"), "UTF-8");
				if (StringUtils.isNotEmpty(property)) {
					property = property.trim();
				}
				log.info(key + "------------" + property);
				cache.put(key, property);
			} catch (UnsupportedEncodingException e) {
				log.error("配置文件获取错误:", e);
			}
		}

	}
}
