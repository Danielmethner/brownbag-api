package com.brownbag_api.model.json;

import com.brownbag_api.model.enums.EDataKind;
import com.brownbag_api.model.enums.EEntityType;
import com.brownbag_api.model.enums.EFormMenuCmd;
import com.brownbag_api.model.enums.EOrderAction;
import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;

public class JsonEOrderAction {

	private String key;
	private String name;
	private String label;
	private String command;
	public EDataKind dataKind;
	private EOrderStatus oldStatus;
	private EOrderStatus newStatus;
	private EEntityType entityType;
	private EOrderType orderType;

	public JsonEOrderAction(EOrderAction orderAction) {
		this.key = orderAction.toString();
		this.name = orderAction.getName();
		this.command = orderAction.getCommand().getFunctionName();
		this.label = orderAction.getName();
		this.oldStatus = orderAction.getOldStatus();
		this.newStatus = orderAction.getNewStatus();
		this.entityType = orderAction.getEntityType();
		this.orderType = orderAction.getOrderType();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EDataKind getDataKind() {
		return dataKind;
	}

	public void setDataKind(EDataKind dataKind) {
		this.dataKind = dataKind;
	}

	public EOrderStatus getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(EOrderStatus oldStatus) {
		this.oldStatus = oldStatus;
	}

	public EOrderStatus getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(EOrderStatus newStatus) {
		this.newStatus = newStatus;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public EEntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EEntityType entityType) {
		this.entityType = entityType;
	}

	public EOrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(EOrderType orderType) {
		this.orderType = orderType;
	}

}
