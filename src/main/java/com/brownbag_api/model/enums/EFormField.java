package com.brownbag_api.model.enums;

import java.util.ArrayList;
import java.util.List;

import com.brownbag_api.model.json.JsonEOrderAction;

public enum EFormField {
	// MASTER DATA
	ORDER_ID("Order ID", "id", EDataType.STRING, EFormFieldEditable.NO, 1, 1, 1);

	public final String name;
	public final EDataType dataType;
	public final EFormFieldEditable editable;
	public final int col;
	public final int colWidth;
	public final int row;
	public final String field;

	private EFormField(String name, String field, EDataType dataType, EFormFieldEditable editable, int col, int colWidth, int row) {
		this.name = name;
		this.field = field;
		this.dataType = dataType;
		this.editable = editable;
		this.col = col;
		this.colWidth = colWidth;
		this.row = row;
	}
}
