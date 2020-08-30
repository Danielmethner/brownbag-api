package com.brownbag_api.model.json;

import java.util.List;

import com.brownbag_api.model.enums.EDataKind;
import com.brownbag_api.model.enums.EDataType;
import com.brownbag_api.model.enums.EEntityType;
import com.brownbag_api.model.enums.EOrderStatus;

public class JsonFormRow {

	private int rowNo;
	private JsonFormField col1;
	private JsonFormField col2;
	private JsonFormField col3;

	public JsonFormRow(int rowNo) {
		this.rowNo = rowNo;
	}

	public JsonFormField getCol1() {
		return col1;
	}

	public void setCol1(JsonFormField col1) {
		this.col1 = col1;
	}

	public JsonFormField getCol2() {
		return col2;
	}

	public void setCol2(JsonFormField col2) {
		this.col2 = col2;
	}

	public JsonFormField getCol3() {
		return col3;
	}

	public void setCol3(JsonFormField col3) {
		this.col3 = col3;
	}

	public int getRowNo() {
		return rowNo;
	}

	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	
}
