package com.brownbag_api.model.json;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.brownbag_api.model.enums.EDataType;
import com.brownbag_api.model.jpa.Booking;
import com.brownbag_api.model.jpa.CtrlVar;

public class JsonCtrlVar {

	private Long id;
	private EDataType dataType;
	private String name;
	private String key;
	private String valString;
	private Date valDate;
	private double valDouble;
	private boolean valBool;
	private Date timestampCreate;
	private Date timestampModified;

	public JsonCtrlVar() {
	}

	public JsonCtrlVar(CtrlVar ctrlVar) {
		this.id = ctrlVar.getId();
		this.dataType = ctrlVar.getDataType();
		this.name = ctrlVar.getName();
		this.key = ctrlVar.getKey();
		this.valString = ctrlVar.getValString();
		this.valDate = ctrlVar.getValDate();
		this.valDouble = ctrlVar.getValDouble();
		this.valBool = ctrlVar.isValBool();
		this.timestampCreate = ctrlVar.getTimestampCreate();
		this.timestampModified = ctrlVar.getTimestampModified();

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EDataType getDataType() {
		return dataType;
	}

	public void setDataType(EDataType dataType) {
		this.dataType = dataType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValString() {
		return valString;
	}

	public void setValString(String valString) {
		this.valString = valString;
	}

	public Date getValDate() {
		return valDate;
	}

	public void setValDate(Date valDate) {
		this.valDate = valDate;
	}

	public double getValDouble() {
		return valDouble;
	}

	public void setValDouble(double valDouble) {
		this.valDouble = valDouble;
	}

	public boolean isValBool() {
		return valBool;
	}

	public void setValBool(boolean valBool) {
		this.valBool = valBool;
	}

	public Date getTimestampCreate() {
		return timestampCreate;
	}

	public void setTimestampCreate(Date timestampCreate) {
		this.timestampCreate = timestampCreate;
	}

	public Date getTimestampModified() {
		return timestampModified;
	}

	public void setTimestampModified(Date timestampModified) {
		this.timestampModified = timestampModified;
	}

}
