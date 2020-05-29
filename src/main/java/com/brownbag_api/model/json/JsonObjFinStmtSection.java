package com.brownbag_api.model.json;

import java.util.List;

import com.brownbag_api.model.enums.EFinStmtSectionType;
import com.brownbag_api.model.jpa.ObjFinStmtItem;
import com.brownbag_api.model.jpa.ObjFinStmtSection;

public class JsonObjFinStmtSection {

	private double id;

	private String name;

	private EFinStmtSectionType section;

	private double qty;

	private String style;

	private List<ObjFinStmtItem> items;

	public JsonObjFinStmtSection(ObjFinStmtSection objBalSheetSection, List<ObjFinStmtItem> items) {
		this.id = objBalSheetSection.getId();
		this.name = objBalSheetSection.getName();
		this.section = objBalSheetSection.getSection();
		this.qty = objBalSheetSection.getQty();
		this.items = items;
	}

	public JsonObjFinStmtSection(ObjFinStmtSection objBalSheetSection) {
		this.id = objBalSheetSection.getId();
		this.name = objBalSheetSection.getName();
		this.section = objBalSheetSection.getSection();
		this.qty = objBalSheetSection.getQty();
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

	public EFinStmtSectionType getSection() {
		return section;
	}

	public void setSection(EFinStmtSectionType section) {
		this.section = section;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public List<ObjFinStmtItem> getItems() {
		return items;
	}

	public void setItems(List<ObjFinStmtItem> items) {
		this.items = items;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

}
