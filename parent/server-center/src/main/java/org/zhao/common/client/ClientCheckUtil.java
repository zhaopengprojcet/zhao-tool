package org.zhao.common.client;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 客户端验证
 * @author zhao
 *
 */
public class ClientCheckUtil {
	
	public static boolean checkPowers(ClientContext client , String ip , String key) {
		if(!StringUtils.isEmpty(ip)) {
			if(!CollectionUtils.isEmpty(client.getIps())) {
				return client.getIps().contains(ip);// 如果有需要验证的ip 并且白名单列表不为空时，验证 ，否则不验证
			}
		}
		if(!StringUtils.isEmpty(key)) {
			if(CollectionUtils.isEmpty(client.getPowers())) return false;//有需要验证的key 若无授权，false
			return client.getPowers().contains(key);
		}
		return true;
	}
}
