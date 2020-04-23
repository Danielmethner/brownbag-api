package com.brownbag_api.model.enums;

public enum EOrderType {
	CREATE_MONEY("Create Money"), PAY("Payment"), STEX("STEX"), LOAN("Loan"),
	STEX_IPO("STEX IPO");

	public final String name;

	private EOrderType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}