package org.zhao.common.util.view;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体更新注解
 * @author zhao
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UpdateView {

	boolean key() default false; //当新增参数和更新时的参数显示不一致，并且未显示的参数包含验证时
	String text(); //更新展示文字
	UpdateTypeEm type() default UpdateTypeEm.TEXT; //字段类型
	String name(); //表单名称
	String check(); //效验内容
	//用于select
	String loadValue() default "";
	String loadValueUrl() default "";
	String textField() default "text";
	String valueField() default "id";
	UpdateTypeEm updateType() default UpdateTypeEm.ADD_AND_UPDATE;//使用类型
}
