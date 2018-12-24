package org.zhao.common.databean.util;

import org.zhao.common.databean.em.FieldTypeEnum;

public class DataParameUtil {

	/**
	 * 字段类型转换
	 * @param field
	 * @return
	 */
	public static String convertAnnnotationType(FieldTypeEnum field) {
		if(field == FieldTypeEnum.STRING) {
			return "javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR";
		}
		else if(field == FieldTypeEnum.DOUBLE) {
			return "javaType=java.lang.Double.class,jdbcType=JdbcType.DOUBLE";
		}
		else if(field == FieldTypeEnum.INT) {
			return "javaType=java.lang.Integer.class,jdbcType=JdbcType.INTEGER";
		}
		else if(field == FieldTypeEnum.DATETIME) {
			return "javaType=java.util.Date.class,jdbcType=JdbcType.TIMESTAMP";
		}
		else {
			return "javaType=java.lang.String.class,jdbcType=JdbcType.VARCHAR";
		}
	}
	
	/**
	 * 驼峰转换为下划线的数据库字段
	 * @param name
	 * @return
	 */
	public static String convertName(String name) {
		StringBuffer sbf = new StringBuffer();
		for (int i = 0 ; i < name.length() ; i++) {
			char n = name.charAt(i);
			if(n >= 'A' && n <= 'Z') { //驼峰
				sbf.append("_"+(char)(n+32));
			}
			else sbf.append(n);
		}
		return sbf.toString();
	}
	
	public static String convertDataName(String name) {
		StringBuffer sbf = new StringBuffer();
		boolean up = false;
		for (int i = 0 ; i < name.length() ; i++) {
			char n = name.charAt(i);
			if(n == '_') { //驼峰
				up = true;
				continue;
				//sbf.append("_"+(char)(n+32));
			}
			else {
				if(up) {
					sbf.append((char)(n-32));
					up = false;
				}
				else sbf.append(n);
			}
		}
		return sbf.toString();
	}
	
	/**
	 * 字段类型转换
	 * @param field
	 * @return
	 */
	public static String convertType(FieldTypeEnum field) {
		if(field == FieldTypeEnum.STRING) {
			return "VARCHAR";
		}
		else if(field == FieldTypeEnum.TEST) {
			return "TEXT";
		}
		else if(field == FieldTypeEnum.DOUBLE) {
			return "DOUBLE";
		}
		else if(field == FieldTypeEnum.INT) {
			return "INT";
		}
		else if(field == FieldTypeEnum.DATETIME) {
			return "DATETIME";
		}
		else {
			return "VARCHAR";
		}
	}
	
	/**
	 * 字段类型转换
	 * @param field
	 * @return
	 */
	public static String convertValueType(FieldTypeEnum field) {
		if(field == FieldTypeEnum.STRING) {
			return "VARCHAR";
		}
		else if(field == FieldTypeEnum.DOUBLE) {
			return "DOUBLE";
		}
		else if(field == FieldTypeEnum.INT) {
			return "INTEGER";
		}
		else if(field == FieldTypeEnum.DATETIME) {
			return "TIMESTAMP";
		}
		else {
			return "VARCHAR";
		}
	}
	
	/**
	 * 空转换
	 * @param notNull
	 * @return
	 */
	public static String convertNotNull(boolean notNull) {
		if(notNull) return "NOT NULL";
		return "DEFAULT NULL";
	}
	
	public static String convertLength(int length) {
		return "("+length+")";
	}
}
