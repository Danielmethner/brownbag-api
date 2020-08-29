package com.brownbag_api.model.json;

import com.brownbag_api.model.enums.EDataKind;
import com.brownbag_api.model.enums.EEntityType;

public class JsonEEntityType {

	private String key;
	private String name;
	public EDataKind dataKind;

	public JsonEEntityType() {
	}

	public JsonEEntityType(EEntityType entityType) {
		this.key = entityType.toString();
		this.name = entityType.getName();
		this.dataKind = entityType.getDataKind();
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

}
