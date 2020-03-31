package com.brownbag_api.model.data;

public enum EUser {
	MGR_CENTRAL_BANK("Central Bank"), MGR_DEUTSCHE_BANK("Deutsche Bank"), MGR_GOVERNMENT("Government"),
	U_BROKER("Broker"), U_TRADER_1("Trader 1"), U_TRADER_2("Trader 2");

	public final String name;

	private EUser(String name) {
		this.name = name;
	}
}
