package com.brownbag_api.model.json;

import java.util.List;

import com.brownbag_api.model.enums.EDataKind;
import com.brownbag_api.model.enums.EEntityType;
import com.brownbag_api.model.enums.EFormField;
import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.trans.FinStmtTrxTrans;

public class JsonFormOrder {

	private List<JsonFormRow> formRows;

	public JsonFormOrder() {
	}

	public JsonFormOrder(EEntityType entityType) {
	}

	public JsonFormOrder(EFormField enumeration) {
		// TODO Auto-generated constructor stub
	}

	public List<JsonFormRow> getFormRows() {
		return formRows;
	}

	public void setFormRows(List<JsonFormRow> formRows) {
		this.formRows = formRows;
	}

	public JsonFormRow getJsonFormRowByRowNo(int rowNo) {
		for (JsonFormRow jsonFormRow : this.formRows) {
			if (jsonFormRow.getRowNo() == rowNo) {
				return jsonFormRow;
			}
		}
		return null;
	}
}
