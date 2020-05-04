package com.brownbag_api.model.json;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

import com.brownbag_api.model.enums.EAssetGrp;
import com.brownbag_api.model.enums.EBalSheetSectionType;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjBalSheet;
import com.brownbag_api.model.jpa.ObjBalSheetItem;
import com.brownbag_api.model.jpa.ObjBalSheetSection;
import com.brownbag_api.model.jpa.ObjParty;
import com.fasterxml.jackson.annotation.JsonBackReference;

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
