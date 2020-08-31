package com.brownbag_api.model.json;

import java.util.List;

import com.brownbag_api.model.enums.EDataKind;
import com.brownbag_api.model.enums.EDataType;
import com.brownbag_api.model.enums.EEntityType;
import com.brownbag_api.model.enums.EFormField;
import com.brownbag_api.model.enums.EFormFieldEditable;
import com.brownbag_api.model.enums.EOrderStatus;

import net.bytebuddy.description.modifier.EnumerationState;

public class JsonFormField {

	private String label;
	private EFormFieldEditable editable;
	private int col;
	private int row;
	private String colSpan;
	private String val;
	private String objId;
	private EDataType dataType;
	private EEntityType entityType;

	public JsonFormField() {
	}

	public JsonFormField(EFormField enumeration) {
		this.label = enumeration.getLabel();
		this.editable = enumeration.getEditable();
		this.col = enumeration.getCol();
		this.row = enumeration.getRow();
		this.val = enumeration.getField();
		this.dataType = enumeration.getDataType();
		this.colSpan = enumeration.getColWidth();
		this.objId = enumeration.getObjId();
		this.entityType = enumeration.getEntityType();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getColSpan() {
		return colSpan;
	}

	public void setColSpan(String colSpan) {
		this.colSpan = colSpan;
	}

	public EFormFieldEditable getEditable() {
		return editable;
	}

	public void setEditable(EFormFieldEditable editable) {
		this.editable = editable;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public EDataType getDataType() {
		return dataType;
	}

	public void setDataType(EDataType dataType) {
		this.dataType = dataType;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public EEntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EEntityType entityType) {
		this.entityType = entityType;
	}

}
