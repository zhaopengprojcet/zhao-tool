package org.zhao.common.databean.model;

import org.zhao.common.databean.em.FieldTypeEnum;

public class DataColumObject {

	private boolean isKey = false;
	private boolean notNull = false;
	private String comment;
	private FieldTypeEnum type = FieldTypeEnum.STRING;
	private int length = 255;//字符串专用
	private boolean setIndex = false;//索引
	
	
	public boolean isKey() {
		return isKey;
	}
	public void setKey(boolean isKey) {
		this.isKey = isKey;
	}
	public boolean isNotNull() {
		return notNull;
	}
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public FieldTypeEnum getType() {
		return type;
	}
	public void setType(FieldTypeEnum type) {
		this.type = type;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public boolean isSetIndex() {
		return setIndex;
	}
	public void setSetIndex(boolean setIndex) {
		this.setIndex = setIndex;
	}
	
	
}
