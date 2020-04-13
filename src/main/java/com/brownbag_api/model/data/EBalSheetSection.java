package com.brownbag_api.model.data;

public enum EBalSheetSection {
	ASSETS("Assets"), LIABILITIES("Liabilities"), EQUITY("Equity");

	public final String name;

	private EBalSheetSection(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
