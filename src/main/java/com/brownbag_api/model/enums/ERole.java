package com.brownbag_api.model.enums;

public enum ERole {
	ROLE_MGR("Manager"), ROLE_BROKER("Broker"), ROLE_ORG("Organisation"), ROLE_GLOBAL_ADMIN("Global Administrator");

	public final String name;

	private ERole(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
