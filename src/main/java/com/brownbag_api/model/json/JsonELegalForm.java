package com.brownbag_api.model.json;

import com.brownbag_api.model.enums.ELegalForm;

public class JsonELegalForm {

	private String key;
	private String name;

	public JsonELegalForm() {
	}

	public JsonELegalForm(ELegalForm legalForm) {
		this.key = legalForm.toString();
		this.name = legalForm.getName();
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

}
