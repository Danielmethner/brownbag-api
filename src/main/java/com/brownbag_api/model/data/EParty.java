package com.brownbag_api.model.data;

public enum EParty {
	BROKER("Broker"), ECB("European  Central Bank"), DEUTSCHE_BANK("Deutsche Bank"), GOVERNMENT("Government");

	public final String name;

	private EParty(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
