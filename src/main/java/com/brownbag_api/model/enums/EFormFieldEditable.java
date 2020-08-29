package com.brownbag_api.model.enums;

public enum EFormFieldEditable {
	// ASSETS
	YES("Yes"), NO("No"), ACC_ORDER_STATUS("According to Order Status");

	public final String name;

	private EFormFieldEditable(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
