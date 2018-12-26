package org.zhao.service;

import java.util.List;
import java.util.Map;

import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.pojo.model.ZserverExpModel;
import org.zhao.common.util.view.ResultContent;

public interface ZserverExpService {

	ResultContent<String> save(ZserverExpModel model);
	
	ResultContent<List<ZserverExpModel>> selectPageListByParameterRequire( PageContext page ,  Map<String, Map<String, String>> require);
}
