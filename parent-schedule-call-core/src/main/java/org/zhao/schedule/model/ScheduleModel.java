package org.zhao.schedule.model;

import java.lang.reflect.Method;

public class ScheduleModel {

	private Object cla;
	private Method method;
	
	public Object getCla() {
		return cla;
	}
	public void setCla(Object cla) {
		this.cla = cla;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	
	
}
