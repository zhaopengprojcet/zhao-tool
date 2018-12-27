package org.zhao.schedule.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.zhao.schedule.annotation.em.ScheduleTypeEm;

/**
 * 声明函数为定时任务
 * @author zhao
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ScheduleMethod {
	String scheduleName() ;//任务关键字 相同的关键字认定为同一任务
}
