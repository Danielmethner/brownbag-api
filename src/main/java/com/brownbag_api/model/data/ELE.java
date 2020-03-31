package com.brownbag_api.model.data;

public enum ELE {
	CENTRAL_BANK("Central Bank"), DEUTSCHE_BANK("Deutsche Bank"), GOVERNMENT("Government");

	public final String name;

	private ELE(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	
}
