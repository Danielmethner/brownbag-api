package com.brownbag_api.model.data;

public enum EParty {
	ECB("European  Central Bank", EUser.MGR_ECB, EPartyType.ORG_GOVT, false),
	BROKER("Broker", EUser.U_BROKER, EPartyType.PERSON_LEGAL, true),
	DEUTSCHE_BANK("Deutsche Bank", EUser.MGR_DEUTSCHE_BANK, EPartyType.PERSON_LEGAL, true),
	GOVERNMENT("Government", EUser.MGR_GOVERNMENT, EPartyType.ORG_GOVT, true);

	public final String name;
	public final EUser user;
	public final EPartyType partyType;
	public final boolean createMACC;

	private EParty(String name, EUser user, EPartyType partyType, boolean createMACC) {
		this.name = name;
		this.user = user;
		this.partyType = partyType;
		this.createMACC = createMACC;
	}

	public String getName() {
		return name;
	}

	public EUser getUser() {
		return user;
	}

	public EPartyType getPartyType() {
		return partyType;
	}

}
