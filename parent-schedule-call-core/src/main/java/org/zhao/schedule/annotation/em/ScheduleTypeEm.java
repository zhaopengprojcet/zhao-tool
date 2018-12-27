package org.zhao.schedule.annotation.em;

public enum ScheduleTypeEm {

	SINGLE("SINGLE") , //定义的定时事件仅向全部同名事件发布一次
	PUSH("PUSH") //定义事件均推送
	;
	
	private String value;

	private ScheduleTypeEm(String value) {
        this.value = value;
    }
	
	public String getValue() {
		return value;
	}
	
	
}
