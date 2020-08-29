package com.brownbag_api.model.json;

import java.util.List;

import com.brownbag_api.model.enums.EDataKind;
import com.brownbag_api.model.enums.EEntityType;
import com.brownbag_api.model.enums.EOrderStatus;

public class JsonFormMenuItem {

	private String key;
	private String name;
	private String label;
	public EDataKind dataKind;
	private List<JsonEOrderAction> items;

	public JsonFormMenuItem() {
	}

	public JsonFormMenuItem(EEntityType entityType) {
		List<JsonEOrderAction> orderActions = entityType.getJsonEOrderActionListByOldOrderStatus(EOrderStatus.NEW);
		this.key = entityType.toString();
		this.name = entityType.getName();
		this.label = entityType.getName();
		this.dataKind = entityType.getDataKind();
		if (orderActions.size() > 0) {
			this.items = entityType.getJsonEOrderActionListByOldOrderStatus(EOrderStatus.NEW);
		}
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public EDataKind getDataKind() {
		return dataKind;
	}

	public void setDataKind(EDataKind dataKind) {
		this.dataKind = dataKind;
	}

	public List<JsonEOrderAction> getItems() {
		return items;
	}

	public void setItems(List<JsonEOrderAction> items) {
		this.items = items;
	}

}
