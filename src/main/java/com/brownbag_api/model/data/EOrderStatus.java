package com.brownbag_api.model.data;

public enum EOrderStatus {
	NEW("New"), HOLD("Hold"), PLACED("Placed"), DONE("Done");

	public final String name;

	private EOrderStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}