package com.brownbag_api.model.json;

import com.brownbag_api.model.enums.EDataKind;
import com.brownbag_api.model.enums.EOrderAction;
import com.brownbag_api.model.enums.EOrderStatus;

public class JsonEOrderAction {

	private String key;
	private String name;
	private String label;
	public EDataKind dataKind;
	private EOrderStatus oldStatus;
	private EOrderStatus newStatus;

	public JsonEOrderAction() {
	}

	public JsonEOrderAction(EOrderAction orderAction) {
		this.key = orderAction.toString();
		this.name = orderAction.getName();
		this.label = orderAction.getName();
		this.oldStatus = orderAction.getOldStatus();
		this.newStatus = orderAction.getNewStatus();
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

}
