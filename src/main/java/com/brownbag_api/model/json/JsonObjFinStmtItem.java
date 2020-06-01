package com.brownbag_api.model.json;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import com.brownbag_api.model.enums.EFinStmtItemType;
import com.brownbag_api.model.enums.EFinStmtType;
import com.brownbag_api.model.jpa.ObjFinStmtItem;
import com.fasterxml.jackson.annotation.JsonBackReference;

public class JsonObjFinStmtItem implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7740583490908285403L;

	private Long id;

	private String name;

	private EFinStmtItemType itemType;
	private String itemTypeName;
	private int finYear;
	private double qty;
	private Date timestampCreate;

	public JsonObjFinStmtItem() {
	}

	
	public JsonObjFinStmtItem(ObjFinStmtItem jpaFinStmtItem) {
		this.id = jpaFinStmtItem.getId();
		this.itemTypeName = jpaFinStmtItem.getName();
		this.itemType = jpaFinStmtItem.getItemType();
		this.itemTypeName = jpaFinStmtItem.getItemType().getName();
		this.finYear = jpaFinStmtItem.getFinYear();
		this.qty = jpaFinStmtItem.getQty();
		this.timestampCreate = jpaFinStmtItem.getTimestampCreate();
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EFinStmtItemType getItemType() {
		return itemType;
	}

	public void setItemType(EFinStmtItemType itemType) {
		this.itemType = itemType;
	}

	public String getItemTypeName() {
		return itemTypeName;
	}

	public void setItemTypeName(String itemTypeName) {
		this.itemTypeName = itemTypeName;
	}

	public int getFinYear() {
		return finYear;
	}

	public void setFinYear(int finYear) {
		this.finYear = finYear;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public Date getTimestampCreate() {
		return timestampCreate;
	}

	public void setTimestampCreate(Date timestampCreate) {
		this.timestampCreate = timestampCreate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
