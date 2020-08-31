package com.brownbag_api.model.enums;

import java.util.ArrayList;
import java.util.List;

import com.brownbag_api.model.json.JsonEOrderAction;

public enum EFormField {
	// GLOBAL
	ORDER_ID("Order ID", "id", null, EDataType.STRING, EFormFieldEditable.NO, 1, 1, "col-md-4", null),
	ORDER_STATUS("Order Status", "orderStatus", null, EDataType.STRING, EFormFieldEditable.NO, 1, 2, "col-md-4", null),
	ORDER_TYPE("Order Type", "orderType", null, EDataType.STRING, EFormFieldEditable.NO, 1, 3, "col-md-4", null),
	USER("Username", "userName", null, EDataType.STRING, EFormFieldEditable.NO, 2, 1, "col-md-4", null),
	TIMESTAMP_CREATE("Timestamp Created", "timestampCreate", null, EDataType.TIMESTAMP, EFormFieldEditable.NO, 2, 2,
			"col-md-4", null),
	TIMESTAMP_MDFY("Timestamp Modified", "timestampModified", null, EDataType.TIMESTAMP, EFormFieldEditable.NO, 2, 3,
			"col-md-4", null),
	ADV_TEXT("Advice Text", "advText", null, EDataType.STRING, EFormFieldEditable.NO, 3, 1, "col-md-12", null),
	// ORDER_STEX
	ASSET_NAME("Asset", "assetName", "assetId", EDataType.STRING, EFormFieldEditable.YES, 4, 1, "col-md-12", EEntityType.OBJ_ASSET),
	PRICE_LIMIT("Price Limit", "priceLimit", null, EDataType.STRING, EFormFieldEditable.YES, 5, 1, "col-md-4", null),
	ORDER_DIR("Order Direction", "orderDir", null, EDataType.STRING, EFormFieldEditable.YES, 5, 2, "col-md-4", null),
	PARTY_NAME("Party Name", "partyName", "partyId", EDataType.STRING, EFormFieldEditable.YES, 5, 3, "col-md-4", EEntityType.OBJ_PARTY),
	QTY_EXEC("Executed Quantity", "qtyExec", null, EDataType.STRING, EFormFieldEditable.YES, 6, 1, "col-md-4", null),
	INTR_RATE("Interest Rate", "intrRate", null, EDataType.STRING, EFormFieldEditable.YES, 6, 2, "col-md-4", null),
	MAT_DATE("Maturity Date", "matDate", null, EDataType.STRING, EFormFieldEditable.YES, 6, 3, "col-md-4", null),
	NOM_VAL("Nominal Value", "nomVal", null, EDataType.STRING, EFormFieldEditable.YES, 7, 1, "col-md-4", null);

	public final String label;
	public final String field;
	public final String objId;
	public final EDataType dataType;
	public final EFormFieldEditable editable;
	public final int row;
	public final int col;
	public final String colWidth;
	public final EEntityType entityType;

	private EFormField(String label, String field, String obj, EDataType dataType, EFormFieldEditable editable, int row,
			int col, String colWidth, EEntityType entityType) {
		this.label = label;
		this.field = field;
		this.objId = obj;
		this.dataType = dataType;
		this.editable = editable;
		this.row = row;
		this.col = col;
		this.colWidth = colWidth;
		this.entityType = entityType;
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

	public String getObjId() {
		return objId;
	}

	public EEntityType getEntityType() {
		return entityType;
	}

}
