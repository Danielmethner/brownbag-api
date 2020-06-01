package com.brownbag_api.model.json;

import java.util.List;

import com.brownbag_api.model.jpa.ObjFinStmtItem;
import com.brownbag_api.model.jpa.ObjFinStmtSection;

public class JsonObjFinStmtSection {

	private double id;

	private String name;

	private String section;

	private double totalQty;
	private String totalCaption;

	private String style;

	private List<JsonObjFinStmtItem> items;

	public JsonObjFinStmtSection(ObjFinStmtSection objBalSheetSection, List<JsonObjFinStmtItem> items) {
		this.id = objBalSheetSection.getId();
		this.name = objBalSheetSection.getName();
		this.section = objBalSheetSection.getSection().getName();
		this.totalQty = objBalSheetSection.getQty();
		this.totalCaption = objBalSheetSection.getSection().getTotalCaption();
		this.items = items;
	}

	public JsonObjFinStmtSection(ObjFinStmtSection objBalSheetSection) {
		this.id = objBalSheetSection.getId();
		this.name = objBalSheetSection.getName();
		this.section = objBalSheetSection.getSection().getName();
		this.totalQty = objBalSheetSection.getQty();
		this.totalCaption = objBalSheetSection.getSection().getTotalCaption();
	}

	public double getId() {
		return id;
	}

	public void setId(double id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public double getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(double totalQty) {
		this.totalQty = totalQty;
	}


	public List<JsonObjFinStmtItem> getItems() {
		return items;
	}

	public void setItems(List<JsonObjFinStmtItem> jsonItemList) {
		this.items = jsonItemList;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getTotalCaption() {
		return totalCaption;
	}

	public void setTotalCaption(String totalCaption) {
		this.totalCaption = totalCaption;
	}

}
