package com.brownbag_api.model.json;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

import com.brownbag_api.model.enums.EAssetGrp;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjBalSheet;
import com.brownbag_api.model.jpa.ObjBalSheetSection;
import com.brownbag_api.model.jpa.ObjParty;
import com.fasterxml.jackson.annotation.JsonBackReference;

public class JsonObjBalSheet {

	private double id;

	private String name;

	private String partyName;

	private Date timestampCreate;

	private int finYear;

	private JsonObjBalSheetSection sectionAssets;
	private JsonObjBalSheetSection sectionLiablities;
	private JsonObjBalSheetSection sectionEquity;

	public JsonObjBalSheet(ObjBalSheet objBalSheet) {
		this.id = objBalSheet.getId();
		this.name = objBalSheet.getName();
		this.partyName = objBalSheet.getParty().getName();
		this.timestampCreate = objBalSheet.getTimestampCreate();
		this.finYear = objBalSheet.getFinYear();
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

	public Date getTimestampCreate() {
		return timestampCreate;
	}

	public void setTimestampCreate(Date timestampCreate) {
		this.timestampCreate = timestampCreate;
	}

	public int getFinYear() {
		return finYear;
	}

	public void setFinYear(int finYear) {
		this.finYear = finYear;
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public JsonObjBalSheetSection getSectionAssets() {
		return sectionAssets;
	}

	public void setSectionAssets(JsonObjBalSheetSection sectionAssets) {
		this.sectionAssets = sectionAssets;
	}

	public JsonObjBalSheetSection getSectionLiablities() {
		return sectionLiablities;
	}

	public void setSectionLiablities(JsonObjBalSheetSection sectionLiablities) {
		this.sectionLiablities = sectionLiablities;
	}

	public JsonObjBalSheetSection getSectionEquity() {
		return sectionEquity;
	}

	public void setSectionEquity(JsonObjBalSheetSection sectionEquities) {
		this.sectionEquity = sectionEquities;
	}

}