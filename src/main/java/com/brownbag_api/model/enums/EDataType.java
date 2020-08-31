package com.brownbag_api.model.enums;

public enum EDataType {
	STRING("String"), DOUBLE("double"), DATE("Date"), BOOL("boolean"), TIMESTAMP("timestamp");

	public final String name;

	private EDataType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
