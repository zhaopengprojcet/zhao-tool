package org.zhao.common.databean.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.zhao.common.databean.em.FieldTypeEnum;

/**
 * 声明类为数据库实体属性
 * @author zhao
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataColum {

	boolean isKey() default false;
	boolean notNull() default false;
	String comment() default "";
	FieldTypeEnum type() default FieldTypeEnum.STRING;
	int length() default 255;//字符串专用
	boolean setIndex() default false;//索引
}
