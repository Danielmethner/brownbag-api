package com.brownbag_api.model.json;

import java.util.List;

import com.brownbag_api.model.enums.EBalSheetSectionType;
import com.brownbag_api.model.jpa.ObjBalSheetItem;
import com.brownbag_api.model.jpa.ObjBalSheetSection;

public class JsonObjBalSheetSection {

	private double id;

	private String name;

	private EBalSheetSectionType section;

	private double qty;

	private List<ObjBalSheetItem> items;

	public JsonObjBalSheetSection(ObjBalSheetSection objBalSheetSection, List<ObjBalSheetItem> items) {
		this.id = objBalSheetSection.getId();
		this.name = objBalSheetSection.getName();
		this.section = objBalSheetSection.getSection();
		this.items = items;
	}

	public JsonObjBalSheetSection(ObjBalSheetSection objBalSheetSection) {
		this.id = objBalSheetSection.getId();
		this.name = objBalSheetSection.getName();
		this.section = objBalSheetSection.getSection();
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

	public EBalSheetSectionType getSection() {
		return section;
	}

	public void setSection(EBalSheetSectionType section) {
		this.section = section;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public List<ObjBalSheetItem> getItems() {
		return items;
	}

	public void setItems(List<ObjBalSheetItem> items) {
		this.items = items;
	}

}
