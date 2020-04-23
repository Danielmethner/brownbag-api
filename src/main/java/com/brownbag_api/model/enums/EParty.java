package com.brownbag_api.model.enums;

public enum EParty {
	ECB("European  Central Bank", EUser.MGR_ECB, EPartyType.ORG_GOVT, ELegalForm.NOT_APPL, false),
	BROKER("Broker", EUser.MGR_BROKER, EPartyType.PERSON_LEGAL, ELegalForm.LTD, true),
	DEUTSCHE_BANK("Deutsche Bank", EUser.MGR_DEUTSCHE_BANK, EPartyType.PERSON_LEGAL, ELegalForm.LTD, true),
	GOVERNMENT("Government", EUser.MGR_GOVERNMENT, EPartyType.ORG_GOVT, ELegalForm.NOT_APPL, true);

	public final String name;
	public final EUser user;
	public final EPartyType partyType;
	public final ELegalForm legalForm;
	public final boolean createMACC;

	private EParty(String name, EUser user, EPartyType partyType, ELegalForm legalForm, boolean createMACC) {
		this.name = name;
		this.user = user;
		this.partyType = partyType;
		this.legalForm = legalForm;
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

	public ELegalForm getLegalForm() {
		return legalForm;
	}

	public boolean isCreateMACC() {
		return createMACC;
	}

}
