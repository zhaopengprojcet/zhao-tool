package org.zhao.common.service;

import java.util.List;
import java.util.Map;

import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.pojo.model.ZuserModel;
import org.zhao.common.util.view.ResultContent;

public interface ZuserService {

	ResultContent<List<ZuserModel>> selectPageListByParameterRequire(PageContext page , Map<String,Map<String,String>> param);
	
	ResultContent<ZuserModel> selectUserById(String id);
	
	ResultContent<String> save(ZuserModel user);
	
	ResultContent<String> delete(String id);
	
	ResultContent<ZuserModel> checkUserLogin(String name , String pass) ;
}
