package com.brownbag_api.model.enums;

public enum EBalSheetSectionType {
	ASSETS("Assets"), LIABILITIES("Liabilities"), EQUITY("Equity");

	public final String name;

	private EBalSheetSectionType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
