package com.brownbag_api.model.data;

public enum EAsset {
	EUR("Euro"), DEUTSCHE_BANK("Deutsche Bank"), GOVERNMENT_BOND("Government Bond");

	public final String name;

	EAsset(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}