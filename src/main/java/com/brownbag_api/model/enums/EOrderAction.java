package com.brownbag_api.model.enums;

public enum EOrderAction {
	HOLD("hold", EOrderStatus.HOLD), PLACE("place", EOrderStatus.PLACED),
	EXECUTE_PART("partially execute", EOrderStatus.EXEC_PART), 
	EXECUTE_FULL("fully execute", EOrderStatus.EXEC_FULL), VERIFY("vfy", EOrderStatus.DONE);

	public final String name;
	public final String intlId;
	public final EOrderStatus newStatus;

	private EOrderAction(String name, EOrderStatus newStatus) {
		this.name = name;
		this.intlId = this.toString().toLowerCase();
		this.newStatus = newStatus;
	}

	public String getName() {
		return name;
	}

	public String getIntlId() {
		return intlId;
	}

	public EOrderStatus getNewStatus() {
		return newStatus;
	}

}