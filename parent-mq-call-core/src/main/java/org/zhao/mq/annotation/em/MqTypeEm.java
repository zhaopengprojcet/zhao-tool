package org.zhao.mq.annotation.em;

public enum MqTypeEm {

	SINGLE("SINGLE") , //定义的定时事件仅向全部同名事件发布一次
	PUSH("PUSH") //定义事件均推送
	;
	
	private String value;

	private MqTypeEm(String value) {
        this.value = value;
    }
	
	public String getValue() {
		return value;
	}
	
	
}
