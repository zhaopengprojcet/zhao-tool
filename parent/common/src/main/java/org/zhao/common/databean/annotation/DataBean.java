package org.zhao.common.databean.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明类为数据库实体对象
 * @author zhao
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataBean {

	String tableName();
	
	boolean database() default false;//database是否参与更新
	
	boolean mapper() default false;//mapper是否参与更新
	
	boolean xml() default false;//xml是否参与更新
}
