package com.brownbag_api.model.enums;

public enum EAssetGrp {
	BOND("Bond"), CURRY("Currency"), LOAN("Loan"), STOCK("Stock");

	public final String name;

	EAssetGrp(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}