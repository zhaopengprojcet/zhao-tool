package org.zhao.common.util.view;

public enum UpdateTypeEm {

	TEXT("text") , 
	SELECT("select") ,
	TEXTARRAY("textarray") , 
	DATE("date") , 
	DATETIME("datetime") , 
	HIDDEN("hidden") ,
	SELECT_TREE("selectree") ,
	PASSWORD("password"),
	
	
	
	ADD_AND_UPDATE("AAU"),
	ADD("A"),
	UPDATE("U")
	;
	
	
	private String value;
	
	private UpdateTypeEm(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
