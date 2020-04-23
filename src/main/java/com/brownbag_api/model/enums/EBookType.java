package com.brownbag_api.model.enums;

public enum EBookType {
	REVENUE("Salary");

	public final String name;

	private EBookType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
