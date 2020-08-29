package com.brownbag_api.model.json;

import java.util.List;

import com.brownbag_api.model.enums.EDataKind;
import com.brownbag_api.model.enums.EEntityType;
import com.brownbag_api.model.enums.EOrderStatus;

public class JsonFormOrder {

	
	private String key;
	private String name;
	private String label;
	public EDataKind dataKind;
	private List<JsonEOrderAction> items;

	public JsonFormOrder() {
	}

	public JsonFormOrder(EEntityType entityType) {
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
