package com.brownbag_api.model.enums;

public enum EOrderDir {
	BUY("Buy"), SELL("Sell");

	public final String name;

	private EOrderDir(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
