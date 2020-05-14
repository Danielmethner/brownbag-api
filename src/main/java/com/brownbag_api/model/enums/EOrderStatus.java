package com.brownbag_api.model.enums;

public enum EOrderStatus {
	NEW("New", true), HOLD("Hold", true), PLACED("Placed", true), EXEC_FULL("Fully Executed", false), EXEC_PART("Partially Executed", false),
	DONE("Done", false), DISC("Discarded", false);

	public final String name;
	public final boolean discardeable;
	private EOrderStatus(String name, boolean discardeable) {
		this.name = name;
		this.discardeable = discardeable;
	}

	public String getName() {
		return name;
	}


	public boolean isDiscardeable() {
		return discardeable;
	}
}