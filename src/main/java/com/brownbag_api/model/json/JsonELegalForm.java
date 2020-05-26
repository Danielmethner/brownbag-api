package com.brownbag_api.model.json;

import java.util.Date;

import com.brownbag_api.model.enums.ELegalForm;
import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;
import com.brownbag_api.model.jpa.Booking;
import com.brownbag_api.model.jpa.ObjPos;
import com.brownbag_api.model.jpa.Order;

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
