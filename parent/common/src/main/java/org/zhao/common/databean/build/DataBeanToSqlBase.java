package org.zhao.common.databean.build;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zhao.common.databean.annotation.DataBean;
import org.zhao.common.databean.annotation.DataColum;
import org.zhao.common.databean.em.FieldTypeEnum;
import org.zhao.common.databean.model.DataColumObject;
import org.zhao.common.databean.service.SysService;
import org.zhao.common.databean.util.DataParameUtil;

/**
 * 实体对象转换为sql语句
 * @author zhao
 *
 */
public class DataBeanToSqlBase {

	private String createType = "";//表创建方式
	
	private Class<DataBean> bean;//实体对象
	
	private List<Map<String, DataColum>> colums = new ArrayList<Map<String,DataColum>>();
	
	private String tableName;
	
	private List<String> sql = new ArrayList<String>();
	
	private SysService sysService;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private DataBeanToSqlBase(String createType , Class bean , SysService sysService) {
		this.bean = bean;
		this.createType = createType;
		this.sysService = sysService;
	}
	
	public static DataBeanToSqlBase init(String createType , Class bean , SysService sysService) {
		return new DataBeanToSqlBase(createType , bean ,sysService);
	}
	
	public List<String> createSql() {
		//初始化数据
		this.parameCreate();
		if(!this.sysService.checkTable(this.tableName)) create();
		else {
			//更新处理
			//获取索引和字段信息
			Map<String,DataColumObject> colums = this.sysService.tableColums(this.tableName);
			if(colums != null && colums.size() > 0) {
				List<String> indexs = new ArrayList<String>();
				for (Map.Entry<String, DataColumObject> entry : colums.entrySet()) {
					if(entry.getValue().isSetIndex()) indexs.add(entry.getKey());
				}
				update(colums, indexs);
			}
			
		}
		//构建附加sql索引等等
		return sql;
	}
	
	/**
	 * 更新表
	 * @param oldColums 已有字段
	 * @param oldIndexs 已有索引
	 */
	private void update(Map<String,DataColumObject> oldColums , List<String> oldIndexs) { //不删除原有主键  不删除原有索引
		Map<String, DataColum> newCol = new HashMap<String, DataColum>();
		for (Map<String, DataColum> map : colums) { //判断新增字段和 需要更新的字段
			for (Map.Entry<String, DataColum> entry : map.entrySet()) {
				newCol.put(entry.getKey(), entry.getValue());
				if(oldColums.containsKey(entry.getKey())) { //更新
					logger.info("UPDATE【"+entry.getKey()+"】");
					sql.add("alter table "+this.tableName+"  modify column "+DataParameUtil.convertName(entry.getKey())+" "+
							DataParameUtil.convertType(entry.getValue().type()) + 
							(entry.getValue().type() == FieldTypeEnum.STRING?DataParameUtil.convertLength((entry.getValue().isKey()?40:entry.getValue().length())):"")+" " +
							DataParameUtil.convertNotNull(entry.getValue().isKey()?true:entry.getValue().notNull()) + 
							"  COMMENT '"+entry.getValue().comment()+"';\n");
				}
				else {
					logger.info("ADD【"+entry.getKey()+"】");
					sql.add("alter table "+this.tableName+"  add "+DataParameUtil.convertName(entry.getKey())+" "+
							DataParameUtil.convertType(entry.getValue().type()) + 
							(entry.getValue().type() == FieldTypeEnum.STRING?DataParameUtil.convertLength((entry.getValue().isKey()?40:entry.getValue().length())):"")+" " +
							DataParameUtil.convertNotNull(entry.getValue().isKey()?true:entry.getValue().notNull()) + 
							"  COMMENT '"+entry.getValue().comment()+"';\n");
				}
				
				if(entry.getValue().setIndex()) { //新键需要设置索引
					if(!oldIndexs.contains(entry.getKey())) { //添加索引
						logger.info("ADD INDEX【"+entry.getKey()+"】");
						sql.add("ALTER TABLE `"+this.tableName+"` ADD INDEX ( `"+DataParameUtil.convertName(entry.getKey())+"` ) ; \n");
					}
				}
 		    }
		}
		
		
		for (Map.Entry<String, DataColumObject> entry : oldColums.entrySet()) { //判断删除字段
			if(!newCol.containsKey(entry.getKey())) { //字段不存在， 删除
				sql.add("alter table "+this.tableName+" drop "+DataParameUtil.convertName(entry.getKey())+"; \n");
			}
		}
	}
	
	/**
	 * 创建表
	 */
	private void create() {
		StringBuffer insert = new StringBuffer();
		List<String> insertIndex = new ArrayList<String>();
		sql.add("DROP TABLE IF EXISTS `"+this.tableName+"`;\n");
		insert.append(
			"CREATE TABLE " + this.tableName + " \n" +
			"( \n"
		);
		boolean begin = true;
		
		for (Map<String, DataColum> map : colums) {
			for (Map.Entry<String, DataColum> entry : map.entrySet()) {
				if(!begin) insert.append(" , \n");
				insert.append(
						DataParameUtil.convertName(entry.getKey()) + " " + 
								DataParameUtil.convertType(entry.getValue().type()) + 
						(entry.getValue().type() == FieldTypeEnum.STRING?DataParameUtil.convertLength((entry.getValue().isKey()?40:entry.getValue().length())):"") + " " + 
						(entry.getValue().isKey()?"PRIMARY KEY":"") + " " + 
						DataParameUtil.convertNotNull(entry.getValue().isKey()?true:entry.getValue().notNull()) + " " +
						(StringUtils.isEmpty(entry.getValue().comment())?"":"COMMENT '"+entry.getValue().comment()+"' ")
				);
				begin = false;
				if(entry.getValue().setIndex()) {
					insertIndex.add("ALTER TABLE `"+this.tableName+"` ADD INDEX ( `"+DataParameUtil.convertName(entry.getKey())+"` ) ; \n");
				}
		    }
		}
		insert.append(
				")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;\n "
		);
		sql.add(insert.toString());
		sql.addAll(insertIndex);
		
	}
	
	
	
	
	
	private void parameCreate() {
		//获取类实例的表名
		DataBean data = bean.getAnnotation(DataBean.class);
		this.tableName = data.tableName();
		if(StringUtils.isEmpty(this.tableName)) {
			throw new RuntimeException("数据实体表名不能为空值【"+bean.getName()+"】");
		}
		//字段实例
		Field[] fields = bean.getDeclaredFields();
		for (Field field : fields) {
			if(field.isAnnotationPresent(DataColum.class)) {
				DataColum colum = field.getAnnotation(DataColum.class);
				Map<String, DataColum> co = new HashMap<String, DataColum>();
				co.put(field.getName(), colum);
				this.colums.add(co);
			}
		}
	}
	
	
}
