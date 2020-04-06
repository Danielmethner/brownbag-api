package com.brownbag_api.model.data;

public enum EPartyType {
	PERSON_NATURAL("Natural Person"), PERSON_LEGAL("Legal Person"), ORG_GOVT("Government Organisation");

	public final String name;

	private EPartyType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
