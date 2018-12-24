package org.zhao.common.service;

import java.util.List;
import java.util.Map;

import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.pojo.model.ZrequestUseModel;
import org.zhao.common.util.view.ResultContent;

public interface ZuseTimeService {

	ResultContent<List<ZrequestUseModel>> selectPageList(ZrequestUseModel model , PageContext page, Map<String, Map<String, String>> paramMap);
	
	ResultContent<String> save(List<ZrequestUseModel> model);
}
