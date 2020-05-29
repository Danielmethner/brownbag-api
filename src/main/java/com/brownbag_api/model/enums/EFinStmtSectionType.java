package com.brownbag_api.model.enums;

public enum EFinStmtSectionType {
	ASSETS("Assets"), LIABILITIES("Liabilities"), EQUITY("Equity");

	public final String name;

	private EFinStmtSectionType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
