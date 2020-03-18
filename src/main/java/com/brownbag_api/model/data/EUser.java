package com.brownbag_api.model.data;

public enum EUser {
	U_BROKER("Broker"), U_TRADER_1("Trader 1"), ISS_CENTRAL_BANK("Central Bank"), ISS_DEUTSCHE_BANK("Deutsche Bank"),
	ISS_GOVERNMENT("Government");

	public final String name;

	private EUser(String name) {
		this.name = name;
	}
}
