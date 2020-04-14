package com.brownbag_api.model.data;

public enum ERole {
	ROLE_MGR("Manager"), ROLE_BROKER("Broker"), ROLE_ORG("Organisation");

	public final String name;

	private ERole(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
