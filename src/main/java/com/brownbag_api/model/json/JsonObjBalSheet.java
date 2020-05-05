package com.brownbag_api.model.json;

import java.util.Date;
import java.util.List;

import com.brownbag_api.model.jpa.ObjBalSheet;

public class JsonObjBalSheet {

	private double id;

	private String name;

	private String partyName;

	private Date timestampCreate;

	private int finYear;

	private List<JsonObjBalSheetSection> sections;

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

	public List<JsonObjBalSheetSection> getSections() {
		return sections;
	}

	public void setSections(List<JsonObjBalSheetSection> sections) {
		this.sections = sections;
	}
	
	

}
