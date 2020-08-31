package com.brownbag_api.model.enums;

import java.util.ArrayList;
import java.util.List;

import com.brownbag_api.model.json.JsonEOrderAction;

public enum EFormField {
	// GLOBAL
	ORDER_ID("Order ID", "id", EDataType.STRING, EFormFieldEditable.NO, 1, 1, "col-md-4"),
	ORDER_STATUS("Order Status", "orderStatus", EDataType.STRING, EFormFieldEditable.NO, 1, 2, "col-md-4"),
	ORDER_TYPE("Order Type", "orderType", EDataType.STRING, EFormFieldEditable.NO, 1, 3, "col-md-4"),
	USER("Username", "userName", EDataType.STRING, EFormFieldEditable.NO, 2, 1, "col-md-4"),
	TIMESTAMP_CREATE("Timestamp Created", "timestampCreate", EDataType.TIMESTAMP, EFormFieldEditable.NO, 2, 2, "col-md-4"),
	TIMESTAMP_MDFY("Timestamp Modified", "timestampModified", EDataType.TIMESTAMP, EFormFieldEditable.NO, 2, 3, "col-md-4"),
//	ADV_TEXT("Advice Text", "advText", EDataType.STRING, EFormFieldEditable.NO, 3, 1, "col-md-12"),
	// ORDER_STEX
//	ASSET_NAME("Asset Name", "assetName", EDataType.STRING, EFormFieldEditable.NO, 4, 1, "col-md-12"),
	PRICE_LIMIT("Price Limit", "priceLimit", EDataType.STRING, EFormFieldEditable.NO, 5, 1, "col-md-4"),
	ORDER_DIR("Order Direction", "orderDir", EDataType.STRING, EFormFieldEditable.NO, 5, 2, "col-md-4"),
	PARTY_NAME("Party Name", "partyName", EDataType.STRING, EFormFieldEditable.NO, 5, 3, "col-md-4"),
	QTY_EXEC("Executed Quantity", "qtyExec", EDataType.STRING, EFormFieldEditable.NO, 6, 1, "col-md-4"),
	INTR_RATE("Interest Rate", "intrRate", EDataType.STRING, EFormFieldEditable.NO, 6, 2, "col-md-4"),
	MAT_DATE("Maturity Date", "matDate", EDataType.STRING, EFormFieldEditable.NO, 6, 3, "col-md-4"),
	NOM_VAL("Nominal Value", "nomVal", EDataType.STRING, EFormFieldEditable.NO, 7, 1, "col-md-4");

	public final String label;
	public final String field;
	public final EDataType dataType;
	public final EFormFieldEditable editable;
	public final int row;
	public final int col;
	public final String colWidth;

	private EFormField(String label, String field, EDataType dataType, EFormFieldEditable editable, int row, int col,
			String colWidth) {
		this.label = label;
		this.field = field;
		this.dataType = dataType;
		this.editable = editable;
		this.row = row;
		this.col = col;
		this.colWidth = colWidth;
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

	public String getColWidth() {
		return colWidth;
	}

}
