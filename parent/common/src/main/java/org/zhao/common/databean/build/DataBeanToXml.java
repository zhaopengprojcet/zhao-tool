package org.zhao.common.databean.build;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import org.zhao.common.databean.util.DataParameUtil;

/**
 * 实体bean 转换为xml
 * @author zhao
 *
 *
 *
 *mybatis对时间类型判断不能使用 == '' 做了类型判断
 *
 */
public class DataBeanToXml {

	private String filePath;//   /src/main/java/org/zhao/base/pojo/mapper
	
	private String packageName;// org.zhao.base.pojo.mapper
	
	private String modelPackage; //org.zhao.base.pojo.model
	
	private Class<DataBean> classBean;
	
	private String daoPackeg;
	
	private Map<String, DataColum> colums = new HashMap<String, DataColum>();
	
	private Map<String, DataColum> key = new HashMap<String, DataColum>();
	
	private String tableName;
	
	private List<String> columsName = new ArrayList<String>();
	
	StringBuffer fileText = new StringBuffer();
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public static DataBeanToXml init(String filePath , String packageName ,String modelPackage ,String daoPackeg, Class bean) {
		DataBeanToXml dbtx = new DataBeanToXml();
		dbtx.filePath = filePath;
		dbtx.packageName = packageName;
		dbtx.classBean = bean;
		dbtx.modelPackage = modelPackage;
		dbtx.daoPackeg = daoPackeg;
		//获取类实例的表名
		DataBean data = dbtx.classBean.getAnnotation(DataBean.class);
		dbtx.tableName = data.tableName();
		if(StringUtils.isEmpty(dbtx.tableName)) {
			throw new RuntimeException("数据实体表名不能为空值【"+dbtx.classBean.getName()+"】");
		}
		//字段实例
		Field[] fields = dbtx.classBean.getDeclaredFields();
		for (Field field : fields) {
			if(field.isAnnotationPresent(DataColum.class)) {
				DataColum colum = field.getAnnotation(DataColum.class);
				if(colum.isKey()) dbtx.key.put(field.getName(), colum);
				else dbtx.colums.put(field.getName() ,colum);
			}
		}
		return dbtx;
	}
	
	public void createFile() {
		String path = System.getProperty("user.dir") + filePath;
		File file = new File(path);
		if(!file.exists()) {
			file.mkdirs();
		}
		try {
			file = new File(path+"/"+this.classBean.getSimpleName() + "Mapper.xml");
			if(file.exists()) file.delete();
			Thread.sleep(1000);
			file.createNewFile();
		} catch (IOException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		fileText.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		fileText.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n");
		fileText.append("<mapper namespace=\""+this.daoPackeg+"."+this.classBean.getSimpleName()+"Mapper\" >\n");
		
		fileText.append("	<resultMap id=\"BaseResultMap\" type=\""+this.modelPackage+"."+this.classBean.getSimpleName()+"\" >\n");
		this.createRestDom();
		fileText.append("	</resultMap>\n");
		
		fileText.append("	<sql id=\"Base_Column_List\">\n");
		fileText.append("		" + String.join(",", columsName) + "\n");
		fileText.append("	</sql>\n");
		
		fileText.append("	<select id=\"selectByPrimaryKey\" resultMap=\"BaseResultMap\" parameterType=\"java.lang.String\">\n");
		fileText.append("		select\n");
		fileText.append("		<include refid=\"Base_Column_List\" />\n");
		fileText.append("		from "+this.tableName+"\n");
		fileText.append("		where id = #{id,jdbcType=VARCHAR}\n");
		fileText.append("	</select>\n");
		
		fileText.append("	<delete id=\"deleteByPrimaryKey\" parameterType=\"java.lang.String\">\n");
		fileText.append("		delete from "+this.tableName+"\n");
		fileText.append("		where id = #{id,jdbcType=VARCHAR}\n");
		fileText.append("	</delete>\n");
		
		fileText.append("	<insert id=\"insert\" parameterType=\""+this.modelPackage+"."+this.classBean.getSimpleName()+"\" useGeneratedKeys=\"true\" keyProperty=\"id\">\n");
		fileText.append("		insert into "+this.tableName+" ("+String.join(",", columsName)+")\n");
		fileText.append("		values (\n");
		this.valueCreate("record." ,"			");
		fileText.append("		)\n");
		fileText.append("	</insert>\n");
		
		fileText.append("	<insert id=\"insertSelective\" parameterType=\""+this.modelPackage+"."+this.classBean.getSimpleName()+"\" useGeneratedKeys=\"true\" keyProperty=\"id\">\n");
		fileText.append("		insert into "+this.tableName+"\n");
		fileText.append("		<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
		this.createColumIf("record.");
		fileText.append("		</trim>\n");
		fileText.append("		<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">\n");
		this.createValueIf("record.");
		fileText.append("		</trim>\n");
		fileText.append("	</insert>\n");
		
		fileText.append("	<update id=\"updateByPrimaryKeySelective\" parameterType=\""+this.modelPackage+"."+this.classBean.getSimpleName()+"\">\n");
		fileText.append("		update "+this.tableName+"\n");
		fileText.append("		<set>\n");
		this.createColumAndValueIf("record.", false);
		fileText.append("		</set>\n");
		fileText.append("		where id = #{record.id,jdbcType=VARCHAR}\n");
		fileText.append("	</update>\n");
		
		fileText.append("	<update id=\"updateByPrimaryKey\" parameterType=\""+this.modelPackage+"."+this.classBean.getSimpleName()+"\">\n");
		fileText.append("		update "+this.tableName+"\n");
		fileText.append("		set\n");
		for (Map.Entry<String, DataColum> entry : colums.entrySet()) {
			fileText.append(this.createValueSet("		", "record.",entry.getKey(), entry.getValue()) + "\n");
		}
		fileText.append("		where id = #{record.id,jdbcType=VARCHAR}\n");
		fileText.append("	</update>\n");
		
		fileText.append("	<select id=\"selectPageListByParameters\" resultMap=\"BaseResultMap\">\n");
		fileText.append("		select * from "+this.tableName+" where 1 = 1\n");
		this.createColumAndValueIf("record.", true);
		this.createPage("		");
		fileText.append("	</select>\n");
		
		fileText.append("	<select id=\"selectPageListByParametersCount\" parameterType=\""+this.modelPackage+"."+this.classBean.getSimpleName()+"\" resultType=\"java.lang.Integer\">\n");
		fileText.append("		select count(*) from "+this.tableName+" where 1 = 1\n");
		this.createColumAndValueIf("record.", true);
		fileText.append("	</select>\n");
		
		fileText.append("	<select id=\"selectPageListByParameterRequire\" resultMap=\"BaseResultMap\">\n");
		fileText.append("		select * from "+this.tableName+" where 1 = 1\n");
		this.createRequire();
		this.createPage("		");
		fileText.append("	</select>\n");
		
		fileText.append("	<select id=\"selectPageListByParameterRequireCount\" parameterType=\""+this.modelPackage+"."+this.classBean.getSimpleName()+"\" resultType=\"java.lang.Integer\">\n");
		fileText.append("		select count(*) from "+this.tableName+" where 1 = 1\n");
		this.createRequire();
		fileText.append("	</select>\n");
		
		fileText.append("	<insert id=\"batchCreate\" parameterType=\"java.util.List\">\n");
		fileText.append("		insert into "+this.tableName+" (\n");
		fileText.append("		" + String.join(",", columsName) + "\n");
		fileText.append("		) values\n");
		fileText.append("		<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">\n");
		fileText.append("			(\n");
		this.valueCreate("item." ,"			");
		fileText.append("			)\n");
		fileText.append("		</foreach>\n");
		fileText.append("	</insert>\n");
		
		fileText.append("	<delete id=\"deleteByParames\" parameterType=\""+this.modelPackage+"."+this.classBean.getSimpleName()+"\">\n");
		fileText.append("		delete from "+this.tableName+" where 1 = 1\n");
		this.createColumAndValueIf("record.", true);
		fileText.append("	</delete>\n");
		fileText.append("</mapper>");
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			fw.write(fileText.toString());
			fw.flush();
			logger.info("【"+classBean.getSimpleName()+"】xml文件生成成功【"+path+"/"+this.classBean.getSimpleName() + "Mapper.xml"+"】");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if(fw != null)
				try {
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	private void createRequire() {
		for (Map.Entry<String, DataColum> entry : key.entrySet()) {
			this.requireText(entry);
		}
		for (Map.Entry<String, DataColum> entry : colums.entrySet()) {
			this.requireText(entry);
		}
	}
	
	private void requireText(Map.Entry<String, DataColum> entry) {
		fileText.append(
				"		<if test=\"require."+entry.getKey()+" != null\">\n" +
				"			<if test=\"require."+entry.getKey()+".greaterThan != null\">\n" +
				"					and "+DataParameUtil.convertName(entry.getKey())+" > #{require."+entry.getKey()+".greaterThan , jdbcType=VARCHAR }\n" +
				"		    </if>\n" +
				"			<if test=\"require."+entry.getKey()+".lessThan != null\">\n" +
				"					and "+DataParameUtil.convertName(entry.getKey())+" &lt; #{require."+entry.getKey()+".lessThan , jdbcType=VARCHAR }\n" +
				"		    </if>\n" +
				"			<if test=\"require."+entry.getKey()+".greaterThanAndEquals != null\">\n" +
				"					and "+DataParameUtil.convertName(entry.getKey())+" >= #{require."+entry.getKey()+".greaterThanAndEquals , jdbcType=VARCHAR }\n" +
				"		    </if>\n" +
				"			<if test=\"require."+entry.getKey()+".lessThanAndEquals != null\">\n" +
				"					and "+DataParameUtil.convertName(entry.getKey())+" &lt;= #{require."+entry.getKey()+".lessThanAndEquals , jdbcType=VARCHAR }\n" +
				"		    </if>\n" +
				"			<if test=\"require."+entry.getKey()+".notEquality != null\">\n" +
				"					and "+DataParameUtil.convertName(entry.getKey())+" &lt;> #{require."+entry.getKey()+".notEquality , jdbcType=VARCHAR }\n" +
				"		    </if>\n" +
				"			<if test=\"require."+entry.getKey()+".equality != null\">\n" +
				"					and "+DataParameUtil.convertName(entry.getKey())+" = #{require."+entry.getKey()+".equality , jdbcType=VARCHAR }\n" +
				"		    </if>\n" +
				"			<if test=\"require."+entry.getKey()+".similar != null\">\n" +
				"					and INSTR("+DataParameUtil.convertName(entry.getKey())+", #{require."+entry.getKey()+".similar , jdbcType=VARCHAR }) >0 \n" +
				"		    </if>\n" +
				"		</if>\n"
				);
		
		
		
		
		/*fileText.append(
				"		<if test=\"record."+entry.getKey()+" != null "+(entry.getValue().type() == FieldTypeEnum.DATETIME ?"":("and record."+entry.getKey()+" != ''")) + " \">\n"+
				"			<if test=\"require.greaterThan != null\">\n"+
				"				<if test=\"require.greaterThan."+entry.getKey()+" != null and require.greaterThan."+entry.getKey()+" != '' \">\n"+
				"					and "+DataParameUtil.convertName(entry.getKey())+" > #{record."+entry.getKey()+" , jdbcType="+DataParameUtil.convertValueType(entry.getValue().type())+"}\n"+
				"				</if>\n"+
				"			</if>\n"+
				"			<if test=\"require.lessThan != null\">\n"+
				"				<if test=\"require.lessThan."+entry.getKey()+" != null and require.lessThan."+entry.getKey()+" != '' \">\n"+
				"					and "+DataParameUtil.convertName(entry.getKey())+" &lt; #{record."+entry.getKey()+" , jdbcType="+DataParameUtil.convertValueType(entry.getValue().type())+"}\n"+
				"				</if>\n"+
				"			</if>\n"+
				"			<if test=\"require.greaterThanAndEquals != null\">\n"+
				"				<if test=\"require.greaterThanAndEquals."+entry.getKey()+" != null and require.greaterThanAndEquals."+entry.getKey()+" != '' \">\n"+
				"					and "+DataParameUtil.convertName(entry.getKey())+" >= #{record."+entry.getKey()+" , jdbcType="+DataParameUtil.convertValueType(entry.getValue().type())+"}\n"+
				"				</if>\n"+
				"			</if>\n"+
				"			<if test=\"require.lessThanAndEquals != null\">\n"+
				"				<if\n"+
				"					test=\"require.lessThanAndEquals."+entry.getKey()+" != null and require.lessThanAndEquals."+entry.getKey()+" != '' \">\n"+
				"					and "+DataParameUtil.convertName(entry.getKey())+" &lt;= #{record."+entry.getKey()+" , jdbcType="+DataParameUtil.convertValueType(entry.getValue().type())+"}\n"+
				"				</if>\n"+
				"			</if>\n"+
				"			<if test=\"require.notEquality != null\">\n"+
				"				<if test=\"require.notEquality."+entry.getKey()+" != null and require.notEquality."+entry.getKey()+" != '' \">\n"+
				"					and "+DataParameUtil.convertName(entry.getKey())+" &lt;> #{record."+entry.getKey()+" , jdbcType="+DataParameUtil.convertValueType(entry.getValue().type())+"}\n"+
				"				</if>\n"+
				"			</if>\n"+
				"			<if test=\"require.similar != null\">\n"+
				"				<if test=\"require.similar."+entry.getKey()+" != null and require.similar."+entry.getKey()+" != '' \">\n"+
				"					and  INSTR("+DataParameUtil.convertName(entry.getKey())+" ,#{record."+entry.getKey()+"}) >0 \n"+
				"				</if>\n"+
				"			</if>\n"+
				"			<if test=\"require.allColom."+entry.getKey()+" == null\">\n"+
				"				and "+DataParameUtil.convertName(entry.getKey())+" = #{record."+entry.getKey()+" , jdbcType="+DataParameUtil.convertValueType(entry.getValue().type())+"}\n"+
				"			</if>\n"+
				"		</if>\n"		
				);*/
	}
	
	private void createPage(String befor) {
		fileText.append(
				befor + "<if test=\" page != null \">\n" +
				befor + "	<if test=\" page.order != null  and page.order != '' \">\n" +
				befor + "		order by ${page.order } ${page.orderBy}\n" +
				befor + "	</if>\n" +
				befor + "	<if test=\" page.start  != null  and page.count  != null \">\n" +
				befor + "		limit #{page.start } , #{page.count }\n" +
				befor + "	</if>\n" +
				befor + "</if>\n");
	}
	
	private void createColumAndValueIf( String prefix  , boolean allColum) {
		
		if(allColum) {
			for (Map.Entry<String, DataColum> entry : key.entrySet()) {
				if(allColum)
					fileText.append("		");
				else 
					fileText.append("			");
				fileText.append("<if test=\""+this.ifText(entry.getKey(), entry.getValue(), prefix)+"\">\n");
				if(allColum)
					fileText.append("			and "+this.createValueSet( "",prefix,entry.getKey(), entry.getValue()) + "\n");
				else 
					fileText.append("				"+this.createValueSet("" ,prefix,entry.getKey(), entry.getValue()) + ",\n");
				if(allColum)
					fileText.append("		");
				else 
					fileText.append("			");
				fileText.append("</if>\n");
			}
		}
		
		for (Map.Entry<String, DataColum> entry : colums.entrySet()) {
			if(allColum)
				fileText.append("		");
			else 
				fileText.append("			");
			fileText.append("<if test=\""+this.ifText(entry.getKey(), entry.getValue(), prefix)+"\">\n");
			if(allColum)
				fileText.append("			and "+this.createValueSet("",prefix ,entry.getKey(), entry.getValue()) + "\n");
			else 
				fileText.append("				"+this.createValueSet("",prefix ,entry.getKey(), entry.getValue()) + ",\n");
			if(allColum)
				fileText.append("		");
			else 
				fileText.append("			");
			fileText.append("</if>\n");
		}
	}
	
	private String createValueSet(String befor ,String prefix ,String name ,DataColum data) {
		return befor + DataParameUtil.convertName(name)+" = #{"+prefix+name+",jdbcType="+DataParameUtil.convertValueType(data.type()) + "}";
	}
	
	private String ifText(String name ,DataColum colum , String prefix) {
		if(colum.type() == FieldTypeEnum.DATETIME) {
			return prefix + name + " != null ";
		}
		else {
			return prefix + name + " != null and "+prefix+name+" != '' ";
		}
	}
	
	private void createValueIf(String prefix) {
		for (Map.Entry<String, DataColum> entry : key.entrySet()) {
			if(entry.getValue().type() == FieldTypeEnum.DATETIME) {
				fileText.append("			<if test=\""+prefix+entry.getKey()+" != null \">\n");
			}
			else {
				fileText.append("			<if test=\""+prefix+entry.getKey()+" != null and "+prefix+entry.getKey()+" != '' \">\n");
			}
			fileText.append("				#{"+prefix+entry.getKey()+",jdbcType="+DataParameUtil.convertValueType(entry.getValue().type())+"},\n");
			fileText.append("			</if>\n");
		}
		for (Map.Entry<String, DataColum> entry : colums.entrySet()) {
			if(entry.getValue().type() == FieldTypeEnum.DATETIME) {
				fileText.append("			<if test=\""+prefix+entry.getKey()+" != null \">\n");
			}
			else {
				fileText.append("			<if test=\""+prefix+entry.getKey()+" != null and "+prefix+entry.getKey()+" != '' \">\n");
			}
			fileText.append("				#{"+prefix+entry.getKey()+",jdbcType="+DataParameUtil.convertValueType(entry.getValue().type())+"},\n");
			fileText.append("			</if>\n");
		}
	}
	
	private void createColumIf(String prefix) {
		for (Map.Entry<String, DataColum> entry : key.entrySet()) {
			if(entry.getValue().type() == FieldTypeEnum.DATETIME) {
				fileText.append("			<if test=\""+prefix+entry.getKey()+" != null \">\n");
			}
			else {
				fileText.append("			<if test=\""+prefix+entry.getKey()+" != null and "+prefix+entry.getKey()+" != '' \">\n");
			}
			fileText.append("				"+DataParameUtil.convertName(entry.getKey())+",\n");
			fileText.append("			</if>\n");
		}
		for (Map.Entry<String, DataColum> entry : colums.entrySet()) {
			if(entry.getValue().type() == FieldTypeEnum.DATETIME) {
				fileText.append("			<if test=\""+prefix+entry.getKey()+" != null \">\n");
			}
			else {
				fileText.append("			<if test=\""+prefix+entry.getKey()+" != null and "+prefix+entry.getKey()+" != '' \">\n");
			}
			fileText.append("				"+DataParameUtil.convertName(entry.getKey())+",\n");
			fileText.append("			</if>\n");
		}
	}
	
	private void valueCreate(String prefix ,String befor) {
		boolean fsplit = false;
		for (Map.Entry<String, DataColum> entry : key.entrySet()) {
			if(fsplit) fileText.append(",\n");
			fileText.append(befor+"#{"+prefix+entry.getKey()+",jdbcType="+DataParameUtil.convertValueType(entry.getValue().type())+"}");
			fsplit = true;
		}
		boolean split = false;
		if(fsplit) split = true;
		for (Map.Entry<String, DataColum> entry : colums.entrySet()) {
			if(split) fileText.append(",\n");
			fileText.append(befor+"#{"+prefix+entry.getKey()+",jdbcType="+DataParameUtil.convertValueType(entry.getValue().type())+"}");
			split = true;
		}
		if(split) {
			fileText.append("\n");
		}
	}
	
	private void createRestDom() {
		for (Map.Entry<String, DataColum> entry : key.entrySet()) {
			fileText.append("		<id column=\""+DataParameUtil.convertName(entry.getKey())+"\" property=\""+entry.getKey()+"\" jdbcType=\""+DataParameUtil.convertValueType(entry.getValue().type())+"\" />\n");
			columsName.add(DataParameUtil.convertName(entry.getKey()));
		}
		for (Map.Entry<String, DataColum> entry : colums.entrySet()) {
			fileText.append("		<result column=\""+DataParameUtil.convertName(entry.getKey())+"\" property=\""+entry.getKey()+"\" jdbcType=\""+DataParameUtil.convertValueType(entry.getValue().type())+"\" />\n");
			columsName.add(DataParameUtil.convertName(entry.getKey()));
		}
	}
}
