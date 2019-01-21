package org.zhao.service;

import java.util.List;
import java.util.Map;

import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.pojo.model.ZregiestClientModel;
import org.zhao.common.util.view.ResultContent;

public interface ZregiestClientService {

	ResultContent<List<ZregiestClientModel>> selectPageListByParameterRequire(PageContext page ,  Map<String, Map<String, String>> parames);
	
	ResultContent<ZregiestClientModel> selectClientById(String id);
	
	ResultContent<String> save(ZregiestClientModel model);
	
	ResultContent<String> delete(String id);
}
