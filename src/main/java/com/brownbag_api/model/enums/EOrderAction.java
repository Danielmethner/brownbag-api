package com.brownbag_api.model.enums;

public enum EOrderAction {
	// OBJ_ASSET
	CREATE("Create Asset", EOrderStatus.NEW, EOrderStatus.OPEN, EEntityType.OBJ_ASSET, EFormMenuCmd.CREATE_ORDER, null),
	HOLD("hold", null, EOrderStatus.HOLD, EEntityType.OBJ_ASSET, null, null),
	OPN_VERIFY("vfy", EOrderStatus.OPEN, EOrderStatus.DONE, EEntityType.OBJ_ASSET, null, null),
	HOLD_VERIFY("vfy", EOrderStatus.HOLD, EOrderStatus.DONE, EEntityType.OBJ_ASSET, null, null),
	DISCARD("discard", null, EOrderStatus.DISC, EEntityType.OBJ_ASSET, null, null),

	// ORDER_LOAN
	LOAN_CREATE("New Loan", EOrderStatus.NEW, EOrderStatus.OPEN, EEntityType.ORDER_LOAN, EFormMenuCmd.CREATE_ORDER, EOrderType.LOAN),
	LOAN_OPN_HOLD("hold", null, EOrderStatus.HOLD, EEntityType.ORDER_LOAN, null, null),
	LOAN_DISCARD("discard", null, EOrderStatus.DISC, EEntityType.ORDER_LOAN, null, null),

	// ORDER_PAY
	PAY_CREATE("New Payment", EOrderStatus.NEW, EOrderStatus.OPEN, EEntityType.ORDER_PAY, EFormMenuCmd.CREATE_ORDER, EOrderType.PAY),
	PAY_OPN_HOLD("hold", null, EOrderStatus.HOLD, EEntityType.ORDER_PAY, null, null),
	PAY_DISCARD("discard", null, EOrderStatus.DISC, EEntityType.ORDER_PAY, null, null),
	// ORDER_INTR
	INTR_CREATE("New Interest Payment", EOrderStatus.NEW, EOrderStatus.OPEN, EEntityType.ORDER_INTR, EFormMenuCmd.CREATE_ORDER, EOrderType.INTR),
	INTR_OPN_HOLD("hold", null, EOrderStatus.HOLD, EEntityType.ORDER_INTR, null, null),
	INTR_DISCARD("discard", null, EOrderStatus.DISC, EEntityType.ORDER_INTR, null, null),
	// ORDER_STEX
	STEX_CREATE("New Trade", EOrderStatus.NEW, EOrderStatus.OPEN, EEntityType.ORDER_STEX, EFormMenuCmd.CREATE_ORDER, EOrderType.STEX),
	STEX_CREATE_IPO("New Order IPO", EOrderStatus.NEW, EOrderStatus.OPEN, EEntityType.ORDER_STEX, EFormMenuCmd.CREATE_ORDER, EOrderType.STEX_IPO),
	STEX_OPN_HOLD("hold", null, EOrderStatus.HOLD, EEntityType.ORDER_STEX, null, null),
	STEX_PLACE("open_place", null, EOrderStatus.PLACED, EEntityType.ORDER_STEX, null, null),
	STEX_EXECUTE_PART("partially execute", null, EOrderStatus.EXEC_PART, EEntityType.ORDER_STEX, null, null),
	STEX_EXECUTE_FULL("fully execute", null, EOrderStatus.EXEC_FULL, EEntityType.ORDER_STEX, null, null),
	STEX_DISCARD("discard", null, EOrderStatus.DISC, EEntityType.ORDER_STEX, null, null),
	// ORDER_CREATE_MONEY
	CREATE_MONEY_CREATE("New Money Creation Order", EOrderStatus.NEW, EOrderStatus.OPEN, EEntityType.ORDER_CREATE_MON, EFormMenuCmd.CREATE_ORDER, EOrderType.CREATE_MONEY),
	CREATE_MONEY_OPN_HOLD("hold", null, EOrderStatus.HOLD, EEntityType.ORDER_CREATE_MON, null, null),
	CREATE_MONEY_DISCARD("discard", null, EOrderStatus.DISC, EEntityType.ORDER_CREATE_MON, null, null);
	
	public final String name;
	public final EFormMenuCmd command;
	public final String intlId;
	public final EOrderStatus newStatus;
	public final EOrderStatus oldStatus;
	public final EEntityType entityType;
	public final EOrderType orderType;

	private EOrderAction(String name, EOrderStatus oldStatus, EOrderStatus newStatus, EEntityType entityType,
			EFormMenuCmd command, EOrderType orderType) {
		this.name = name;
		this.intlId = this.toString().toLowerCase();
		this.oldStatus = oldStatus;
		this.newStatus = newStatus;
		this.entityType = entityType;
		this.command = command;
		this.orderType = orderType;
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

	public EOrderStatus getOldStatus() {
		return oldStatus;
	}

	public EEntityType getEntityType() {
		return entityType;
	}

	public EFormMenuCmd getCommand() {
		return command;
	}

	public EOrderType getOrderType() {
		return orderType;
	}

	
}