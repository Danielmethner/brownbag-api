package com.brownbag_api.model.enums;

public enum ELegalForm {
	NOT_APPL("Not Applicable"), CORP("Public Corporation"), LTD("Limitied Liability Corporation");

	public final String name;

	private ELegalForm(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}