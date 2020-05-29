package com.brownbag_api.model.enums;

public enum EFinStmtType {
	// ASSETS
	BAL_SHEET("Balance Sheet"), INCOME_STMT("Income Statement");

	public final String name;

	private EFinStmtType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
