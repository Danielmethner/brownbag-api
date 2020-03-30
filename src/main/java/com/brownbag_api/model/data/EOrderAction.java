package com.brownbag_api.model.data;

public enum EOrderAction {
	CREATE("create", EOrderStatus.NEW), HOLD("hold", EOrderStatus.HOLD), PLACE("place", EOrderStatus.PLACED),
	VERIFY("vfy", EOrderStatus.DONE);

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