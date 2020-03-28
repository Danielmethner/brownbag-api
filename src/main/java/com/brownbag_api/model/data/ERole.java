package com.brownbag_api.model.data;

public enum ERole {
	ROLE_TRADER("Trader"), ROLE_BROKER("Broker"), ROLE_ISSUER("Issuer");
	
	public final String name;

	private ERole(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	
}
