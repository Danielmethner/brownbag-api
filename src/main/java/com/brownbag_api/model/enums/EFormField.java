package com.brownbag_api.model.enums;

import java.util.ArrayList;
import java.util.List;

import com.brownbag_api.model.json.JsonEOrderAction;

public enum EFormField {
	// MASTER DATA
	ORDER_ID("Order ID", "id", EDataType.STRING, EFormFieldEditable.NO, 1, 1, 1),
	ORDER_STATUS("Order Status", "orderStatus", EDataType.STRING, EFormFieldEditable.NO, 2, 1, 1);

	public final String label;
	public final String field;
	public final EDataType dataType;
	public final EFormFieldEditable editable;
	public final int col;
	public final int row;

	private EFormField(String label, String field, EDataType dataType, EFormFieldEditable editable, int col,
			int colWidth, int row) {
		this.label = label;
		this.field = field;
		this.dataType = dataType;
		this.editable = editable;
		this.col = col;
		this.row = row;
	}

	public String getLabel() {
		return label;
	}

	public String getField() {
		return field;
	}

	public EDataType getDataType() {
		return dataType;
	}

	public EFormFieldEditable getEditable() {
		return editable;
	}

	public int getCol() {
		return col;
	}

	public int getRow() {
		return row;
	}
	
	

}
