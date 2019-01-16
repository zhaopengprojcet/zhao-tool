package org.zhao.mq.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.zhao.mq.annotation.em.MqTypeEm;

/**
 * 声明函数为消息订阅
 * @author zhao
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MqMethod {
	String mqName() ;//任务关键字 相同的关键字认定为同一任务
}
