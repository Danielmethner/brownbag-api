package com.brownbag_api.model.enums;

public enum EOrderAction {
	// OBJ_ASSET
	CREATE("Create Asset", EOrderStatus.NEW, EOrderStatus.OPEN, EEntityType.OBJ_ASSET, EFormMenuCmd.CREATE_ORDER),
	HOLD("hold", null, EOrderStatus.HOLD, EEntityType.OBJ_ASSET, null),
	PLACE("open_place", null, EOrderStatus.PLACED, EEntityType.OBJ_ASSET, null),
	VERIFY("vfy", null, EOrderStatus.DONE, EEntityType.OBJ_ASSET, null),
	DISCARD("discard", null, EOrderStatus.DISC, EEntityType.OBJ_ASSET, null),
	// ORDER_STEX
	STEX_CREATE("New Order", EOrderStatus.NEW, EOrderStatus.OPEN, EEntityType.ORDER_STEX, EFormMenuCmd.CREATE_ORDER),
	STEX_HOLD("hold", null, EOrderStatus.HOLD, EEntityType.ORDER_STEX, null),
	STEX_PLACE("open_place", null, EOrderStatus.PLACED, EEntityType.ORDER_STEX, null),
	STEX_EXECUTE_PART("partially execute", null, EOrderStatus.EXEC_PART, EEntityType.ORDER_STEX, null),
	STEX_EXECUTE_FULL("fully execute", null, EOrderStatus.EXEC_FULL, EEntityType.ORDER_STEX, null),
	STEX_VERIFY("vfy", null, EOrderStatus.DONE, EEntityType.ORDER_STEX, null),
	STEX_DISCARD("discard", null, EOrderStatus.DISC, EEntityType.ORDER_STEX, null);

	public final String name;
	public final EFormMenuCmd command;
	public final String intlId;
	public final EOrderStatus newStatus;
	public final EOrderStatus oldStatus;
	public final EEntityType entityType;

	private EOrderAction(String name, EOrderStatus oldStatus, EOrderStatus newStatus, EEntityType entityType,
			EFormMenuCmd command) {
		this.name = name;
		this.intlId = this.toString().toLowerCase();
		this.oldStatus = oldStatus;
		this.newStatus = newStatus;
		this.entityType = entityType;
		this.command = command;
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

}