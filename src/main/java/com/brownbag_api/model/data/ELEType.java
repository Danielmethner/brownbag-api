package com.brownbag_api.model.data;

public enum ELEType {
	PERSON_NATURAL("Natural Person"), PERSON_LEGAL("Legal Person"), ORG_GOVT("Government Organisation");

	public final String name;

	private ELEType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
