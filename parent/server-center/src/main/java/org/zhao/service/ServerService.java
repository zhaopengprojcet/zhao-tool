package org.zhao.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.zhao.common.client.ClientContext;
import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.util.view.ResultContent;

public interface ServerService {

	ResultContent<String> regiestClient(ClientContext client , HttpServletRequest request);
	
	ResultContent<String> regiestClientList(String key , PageContext page);
}
