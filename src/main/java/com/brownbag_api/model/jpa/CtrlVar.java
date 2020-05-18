package com.brownbag_api.model.jpa;

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

@Entity
@Table(name = "CTRL_VAR")
public class CtrlVar {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private EDataType dataType;

	@Size(max = 100)
	@Column(name = "NAME", unique = true)
	private String name;

	@Size(max = 100)
	@Column(name = "KEY_SYM", unique = true)
	private String key;

	@Size(max = 100)
	@Column(name = "VAL_STRING", unique = true)
	private String valString;

	@Column(name = "VAL_DATE")
	private Date valDate;

	@Column(name = "VAL_DOUBLE")
	private double valDouble;

	@Column(name = "VAL_BOOL", columnDefinition = "boolean default false")
	private boolean valBool;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP", updatable = false)
	private Date timestampCreate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TS_LAST_MODIFIED")
	private Date timestampModified;

	public CtrlVar() {
		// TODO Auto-generated constructor stub
	}

	public CtrlVar(@NotNull EDataType dataType, @Size(max = 100) String name, @Size(max = 100) String key,
			@Size(max = 100) String valString, Date valDate, double valDouble, boolean valBool) {
		super();
		this.dataType = dataType;
		this.name = name;
		this.key = key;
		this.valString = valString;
		this.valDate = valDate;
		this.valDouble = valDouble;
		this.valBool = valBool;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
