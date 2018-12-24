package org.zhao.common.databean.build;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zhao.common.databean.annotation.DataColum;
import org.zhao.common.databean.util.DataParameUtil;

/**
 * 实体bean转换为mapper文件
 * @author zhao
 *
 */
public class DataBeanToMapper {
	
	private String filePath;//   /src/main/java/org/zhao/base/pojo/dao
	
	private String packageName;//org.zhao.base.pojo.dao
	
	private Class classBean;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public static DataBeanToMapper init(String filePath , String packageName , Class bean) {
		DataBeanToMapper dbtm = new DataBeanToMapper();
		dbtm.filePath = filePath;
		dbtm.packageName = packageName;
		dbtm.classBean = bean;
		return dbtm;
	}
	
	public void createFile() {
		String path = System.getProperty("user.dir") + filePath;
		File file = new File(path);
		if(!file.exists()) {
			file.mkdirs();
		}
		try {
			file = new File(path+"/"+this.classBean.getSimpleName() + "Mapper.java");
			if(file.exists()) file.delete();
			Thread.sleep(1000);
			file.createNewFile();
		} catch (IOException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		StringBuffer fileText = new StringBuffer();
		
		fileText.append("package "+this.packageName+";\n\n");//包
		fileText.append("import "+this.classBean.getCanonicalName()+";\n"); //关联实体
		/*fileText.append("import org.zp.common.databean.util.MapperBase;\n\n");//集成的通用mapper
*/		
		fileText.append("public interface "+this.classBean.getSimpleName()+"Mapper extends MapperBase<"+this.classBean.getSimpleName()+">{\n\n");
		
		fileText.append("    /**\n");
		fileText.append("     @Results(value={\n");
		
		Map<String, DataColum> colums = new HashMap<String, DataColum>();
		Field[] fields = this.classBean.getDeclaredFields();
		for (Field field : fields) {
			if(field.isAnnotationPresent(DataColum.class)) {
				DataColum colum = field.getAnnotation(DataColum.class);
				colums.put(field.getName() , colum);
			}
		}
		boolean split = false;
		for (Map.Entry<String, DataColum> entry : colums.entrySet()) {
			if(split) {
				fileText.append(",\n");
			}
			fileText.append("		@Result(column=\""+DataParameUtil.convertName(entry.getKey())+"\",property=\""+entry.getKey()+"\","+DataParameUtil.convertAnnnotationType(entry.getValue().type())+")");
			split = true;
		}
		fileText.append("\n");
		fileText.append("	 })\n");
		fileText.append("     */\n\n");
		
		fileText.append("}");
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			fw.write(fileText.toString());
			fw.flush();
			logger.info("【"+classBean.getSimpleName()+"】mapper文件生成成功【"+path+"/"+this.classBean.getSimpleName() + "Mapper.java"+"】");
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
	
}
