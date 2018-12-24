package org.zhao.common.databean.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhao.common.databean.annotation.DataBean;
import org.zhao.common.databean.build.DataBeanToMapper;
import org.zhao.common.databean.build.DataBeanToSqlBase;
import org.zhao.common.databean.build.DataBeanToXml;
import org.zhao.common.databean.service.SysService;
import org.zhao.common.properties.ConfigProperties;
import org.zhao.common.util.ClassGetUtil;

/**
 * 项目启动时更新数据库结构和生成对应mapper与xml
 * @author zhao
 *
 */
@Component
public class LoadDataBeanListener implements InitializingBean{

	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private SysService sysService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("-------------------------预处理实体对象 开始----------------------------");
		Set<Class<?>> set = ClassGetUtil.getClassSet(ConfigProperties.instance().getPropertiesVal("data.bean.path"));
		List<String> sql = new ArrayList<String>();
		for (Class<?> c : set) {
			if(c.isAnnotationPresent(DataBean.class)) {
				DataBean db = c.getAnnotation(DataBean.class);
				if(db.database() || ConfigProperties.instance().getPropertiesBoolean("data.database.create")) {
					sql.addAll(DataBeanToSqlBase.init(ConfigProperties.instance().getPropertiesVal("data.bean.init"), c, sysService).createSql());
				}
				if(db.mapper() || ConfigProperties.instance().getPropertiesBoolean("data.mapper.create")) {
					DataBeanToMapper.init(ConfigProperties.instance().getPropertiesVal("data.mapper.filePath"), ConfigProperties.instance().getPropertiesVal("data.mapper.package"), c).createFile();
				}
				if(db.xml() || ConfigProperties.instance().getPropertiesBoolean("data.xml.create")) {
					DataBeanToXml.init(ConfigProperties.instance().getPropertiesVal("data.xml.filePath"), ConfigProperties.instance().getPropertiesVal("data.xml.package"), ConfigProperties.instance().getPropertiesVal("data.bean.path"),ConfigProperties.instance().getPropertiesVal("data.mapper.package"), c).createFile();
				}
			}
		}
		if(sql.size() > 0) sysService.ddlSql(sql);
		logger.info("-------------------------预处理实体对象 结束----------------------------"); 
	}

	
	
}
