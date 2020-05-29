package com.brownbag_api.model.enums;

public enum ELegalForm {
	CORP("Public Corporation", "Corp."), LTD("Limitied Liability Corporation", "Ltd.");

	private final String name;
	private final String abbr;

	private ELegalForm(String name, String abbr) {
		this.name = name;
		this.abbr = abbr;
	}

	public String getName() {
		return name;
	}

	public String getAbbr() {
		return abbr;
	}

}