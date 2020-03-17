package com.brownbag_api.model.data;

public enum EUser {
	U_BROKER("Broker"), U_TRADER_1("Trader 1");

	public final String name;

	private EUser(String name) {
		this.name = name;
	}
}
