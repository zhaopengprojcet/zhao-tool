package org.zhao.common.service;

import java.util.List;
import java.util.Map;

import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.pojo.model.ZwhiteBlackIpList;
import org.zhao.common.util.view.ResultContent;

public interface ZwhiteBlackIpService {

	ResultContent<List<ZwhiteBlackIpList>> selectPageListByParameterRequire(PageContext page ,  Map<String, Map<String, String>> require);
	
	ResultContent<ZwhiteBlackIpList> selectIpById(String id);
	
	ResultContent<String> save(ZwhiteBlackIpList ip);
	
	ResultContent<String> delete(String id);
}
