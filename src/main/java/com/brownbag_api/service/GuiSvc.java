package com.brownbag_api.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.ECtrlVar;
import com.brownbag_api.model.enums.EEntityType;
import com.brownbag_api.model.enums.EFormField;
import com.brownbag_api.model.enums.ELegalForm;
import com.brownbag_api.model.jpa.CtrlVar;
import com.brownbag_api.model.json.JsonELegalForm;
import com.brownbag_api.model.json.JsonFormField;
import com.brownbag_api.model.json.JsonFormOrder;
import com.brownbag_api.model.json.JsonFormRow;
import com.brownbag_api.repo.CtrlVarRepo;

@Service
public class GuiSvc {

	@Autowired
	private LogSvc logSvc;

	public GuiSvc() {
	}
	
	private void setRowField(JsonFormField jsonFormField, JsonFormRow jsonFormRow) {
		switch (jsonFormField.getCol()) {
		case 1:
			jsonFormRow.setCol1(jsonFormField);
			break;
		case 2:
			jsonFormRow.setCol2(jsonFormField);
			break;
		case 3:
			jsonFormRow.setCol3(jsonFormField);
			break;
		default:
			break;
		}
	}

	public JsonFormOrder getFormByEntityType(EEntityType entityType) {
		
		List<JsonFormRow> jsonFormRowList = new ArrayList<JsonFormRow>();
		EFormField[] eFormFieldList = EFormField.values();
		JsonFormOrder jsonFormOrder = new JsonFormOrder();

		// BUILD FORM BASED ON EFormField
		for (EFormField eFormField : eFormFieldList) {
			
			// TODO: Filter by EntityType
			JsonFormField jsonFormField = new JsonFormField(eFormField);
			boolean rowExists = false;
			
			// CHECK IF ROW EXISTS
			for (JsonFormRow jsonFormRow : jsonFormRowList) {
				if (jsonFormRow.getRowNo() == jsonFormField.getRow()) {
					rowExists = true;
					setRowField(jsonFormField, jsonFormRow);
				}
			}
			// ADD ROW IF NOT EXISTS
			if (rowExists == false) {
				JsonFormRow jsonFormRow = new JsonFormRow(jsonFormField.getRow());
				setRowField(jsonFormField, jsonFormRow);
				jsonFormRowList.add(jsonFormRow);
			}
		}

		jsonFormOrder.setFormRows(jsonFormRowList);
		return jsonFormOrder;
	}
}
