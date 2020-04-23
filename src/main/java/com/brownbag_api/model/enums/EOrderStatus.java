package com.brownbag_api.model.enums;

public enum EOrderStatus {
	NEW("New"), HOLD("Hold"), PLACED("Placed"), EXEC_FULL("Fully Executed"), EXEC_PART("Partially Executed"),
	DONE("Done"), DISC("Discarded");

	public final String name;

	private EOrderStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}