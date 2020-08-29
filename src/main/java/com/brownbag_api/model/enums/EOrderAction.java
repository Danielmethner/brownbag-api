package com.brownbag_api.model.enums;

public enum EOrderAction {
	// OBJ_ASSET
	CREATE("create", EOrderStatus.NEW, EOrderStatus.OPEN, EEntityType.OBJ_ASSET),
	HOLD("hold", null, EOrderStatus.HOLD, EEntityType.OBJ_ASSET),
	PLACE("open_place", null, EOrderStatus.PLACED, EEntityType.OBJ_ASSET),
	EXECUTE_PART("partially execute", null, EOrderStatus.EXEC_PART, EEntityType.OBJ_ASSET),
	EXECUTE_FULL("fully execute", null, EOrderStatus.EXEC_FULL, EEntityType.OBJ_ASSET),
	VERIFY("vfy", null, EOrderStatus.DONE, EEntityType.OBJ_ASSET),
	DISCARD("discard", null, EOrderStatus.DISC, EEntityType.OBJ_ASSET);

	public final String name;
	public final String intlId;
	public final EOrderStatus newStatus;
	public final EOrderStatus oldStatus;
	public final EEntityType entityType;

	private EOrderAction(String name, EOrderStatus oldStatus, EOrderStatus newStatus, EEntityType entityType) {
		this.name = name;
		this.intlId = this.toString().toLowerCase();
		this.oldStatus = oldStatus;
		this.newStatus = newStatus;
		this.entityType = entityType;
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
	

}