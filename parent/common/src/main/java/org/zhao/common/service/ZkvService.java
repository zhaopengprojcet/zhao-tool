package org.zhao.common.service;

import java.util.List;
import java.util.Map;

import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.pojo.model.ZkeyValueModel;
import org.zhao.common.pojo.model.ZuserModel;
import org.zhao.common.util.view.ResultContent;

public interface ZkvService {

	ResultContent<List<ZkeyValueModel>> selectPageListByParameterRequire(PageContext page , Map<String,Map<String,String>> param);
	
	ResultContent<ZkeyValueModel> selectKvById(String id);
	
	ResultContent<String> save(ZkeyValueModel kv);
	
	ResultContent<String> delete(String id);
	
	ResultContent<String> getStringValue(String key);
	
	ResultContent<Integer> getIntValue(String key);
	
	ResultContent<Boolean> getBooleanValue(String key);
}
