package org.zhao.common.databean.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zhao.common.databean.em.FieldTypeEnum;
import org.zhao.common.databean.model.DataColumObject;
import org.zhao.common.databean.service.SysService;
import org.zhao.common.databean.util.DataParameUtil;
import org.zhao.common.pojo.dao.SysBaseMapper;

@Service
public class SysServiceImpl implements SysService {

	@Autowired
	private SysBaseMapper sysBaseMapper;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public boolean checkTable(String name) {
		Map<String, String> result = this.sysBaseMapper.checkTable(name);
		if(result != null && result.keySet().size() > 0) return true;
		return false;
	}

	@Override
	public Map<String,DataColumObject> tableColums(String name) {
		List<Map<String, Object>> colums = this.sysBaseMapper.selectTableColumns(name);
		if(colums == null || colums.size() < 1) {
			return new HashMap<String, DataColumObject>();
		}
		Map<String,DataColumObject> datas = new HashMap<String, DataColumObject>();
		for (Map<String, Object> map : colums) {
			DataColumObject dco = new DataColumObject();
			if(map.get("COLUMN_KEY") != null && map.get("COLUMN_KEY").toString().equalsIgnoreCase("PRI"))
				dco.setKey(true);
			if(map.get("COLUMN_KEY") != null && map.get("COLUMN_KEY").toString().equalsIgnoreCase("MUL"))
				dco.setSetIndex(true);
			if(map.get("IS_NULLABLE") != null && map.get("IS_NULLABLE").toString().equalsIgnoreCase("NO"))
				dco.setNotNull(true);
			if(map.get("COLUMN_COMMENT") != null && StringUtils.isEmpty(map.get("COLUMN_COMMENT").toString()))
				dco.setComment(map.get("COLUMN_COMMENT").toString());
			switch (map.get("DATA_TYPE").toString()) {
			case "varchar":
				dco.setType(FieldTypeEnum.STRING);
				dco.setLength(Integer.parseInt(map.get("CHARACTER_MAXIMUM_LENGTH").toString()));
				break;
			case "datetime":
				dco.setType(FieldTypeEnum.DATETIME);			
				break;
			case "double":
				dco.setType(FieldTypeEnum.DOUBLE);
				break;
			default:
				dco.setType(FieldTypeEnum.STRING);
				break;
			}
			datas.put(DataParameUtil.convertDataName(map.get("COLUMN_NAME").toString()), dco);
		}
		return datas;
	}

	@Transactional
	@Override
	public void ddlSql(List<String> sql) {
		if(sql != null && sql.size() > 0) {
			for (String string : sql) {
				this.sysBaseMapper.updateDDL(string.toLowerCase());
			}
		}
		
	}

	
}
