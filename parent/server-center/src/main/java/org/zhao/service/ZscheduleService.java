package org.zhao.service;

import java.util.List;
import java.util.Map;

import org.zhao.common.mybatis.query.PageContext;
import org.zhao.common.pojo.model.ZscheduleLogModel;
import org.zhao.common.pojo.model.ZscheduleSetModel;
import org.zhao.common.util.view.ResultContent;

public interface ZscheduleService {

	ResultContent<List<ZscheduleSetModel>> selectPageListByParameterRequire(PageContext page ,  Map<String, Map<String, String>> parames);
	
	ResultContent<ZscheduleSetModel> selectScheduleSetById(String id);
	
	ResultContent<String> save(ZscheduleSetModel model);
	
	ResultContent<String> delete(String id);
	
	ResultContent<String> saveLog(ZscheduleLogModel log);
	
	ResultContent<String> updateResultLog(String id , String msg);
	
	ResultContent<String> updatePutLog(ZscheduleLogModel log);
	
	ResultContent<List<ZscheduleLogModel>> selectLogPageListByParameterRequire(PageContext page ,  Map<String, Map<String, String>> parames);
	
}
