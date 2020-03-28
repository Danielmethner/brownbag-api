package com.brownbag_api.model.data;

public enum EOrderStatus {
	NEW("New"), HOLD("Hold"), PLACED("Placed"), DONE("Done");
	
	public final String name;
	public final String intlId;

	private EOrderStatus(String name) {
		this.name = name;
		this.intlId = this.toString().toLowerCase();
	}

	public String getName() {
		return name;
	}

	public String getIntlId() {
		return intlId;
	}
	
	
}