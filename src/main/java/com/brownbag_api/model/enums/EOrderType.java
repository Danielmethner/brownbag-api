package com.brownbag_api.model.enums;

public enum EOrderType {
	// MASTER DATA
	CREATE_ASSET("Create Asset", EEntityType.OBJ_ASSET),
	// TRANSACTION ORDERS
	CREATE_MONEY("Create Money", EEntityType.ORDER_CREATE_MON), PAY("Payment", EEntityType.ORDER_PAY),
	STEX("STEX", EEntityType.ORDER_STEX), LOAN("Loan", EEntityType.ORDER_LOAN),
	INTR("Interest Payment", EEntityType.ORDER_INTR), STEX_IPO("STEX IPO", EEntityType.ORDER_STEX);

	public final String name;
	public final EEntityType entityType;

	private EOrderType(String name, EEntityType entityType) {
		this.name = name;
		this.entityType = entityType;
	}

	public String getName() {
		return name;
	}

	public EEntityType getEntityType() {
		return entityType;
	}

}