package com.brownbag_api.model.enums;

public enum EUser {
	MGR_ECB("European Central Bank", ERole.ROLE_ORG), MGR_DEUTSCHE_BANK("Deutsche Bank", ERole.ROLE_ORG),
	MGR_GOVERNMENT("Government", ERole.ROLE_ORG), U_BROKER("Broker", ERole.ROLE_BROKER),
	U_TRADER_1("Trader 1", ERole.ROLE_MGR), U_TRADER_2("Trader 2", ERole.ROLE_MGR);

	public final String name;
	public final ERole role;

	private EUser(String name, ERole eRole) {
		this.name = name;
		this.role = eRole;
	}

	public String getName() {
		return name;
	}

	public ERole getRole() {
		return role;
	}

}
