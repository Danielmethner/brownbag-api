package com.brownbag_api.model.jpa;

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
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "OBJ_BAL_SHEET_ITEM")
public class ObjFinStmtItem implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7740583490908285403L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotBlank
	@Size(max = 100)
	private String name;

	@NotNull
	@Column(name = "QTY", columnDefinition = "Decimal(20,2)")
	private double qty;

	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private EFinStmtItemType itemType;

	@NotNull
	@Column(name = "FIN_YEAR", updatable = false)
	private int finYear;

	@NotNull
	@ManyToOne(targetEntity = ObjParty.class)
	@JoinColumn(name = "PARTY_ID")
	@JsonBackReference
	private ObjParty party;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_CREATED", updatable = false)
	private Date timestampCreate;

	@NotNull
	@ManyToOne(targetEntity = ObjFinStmtSection.class)
	@JoinColumn(name = "FIN_STMT_SECTION_ID")
	@JsonBackReference
	private ObjFinStmtSection finStmtSection;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private EFinStmtType finStmtType;

	public ObjFinStmtItem() {
	}

	public ObjFinStmtItem(@NotNull double qty, EFinStmtItemType itemType, @NotNull int finYear, @NotNull ObjParty party,
			@NotNull ObjFinStmtSection balSheetSection, @NotNull EFinStmtType finStmtType) {
		super();
		this.name = balSheetSection.getName() + ": " + itemType.getName();
		this.qty = qty;
		this.itemType = itemType;
		this.finYear = finYear;
		this.party = party;
		this.finStmtSection = balSheetSection;
		this.finStmtType = finStmtType;
	}

	public ObjFinStmtSection getFinStmtSection() {
		return finStmtSection;
	}

	public void setBalSheetSection(ObjFinStmtSection finStmtSection) {
		this.finStmtSection = finStmtSection;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public EFinStmtItemType getItemType() {
		return itemType;
	}

	public void setItemType(EFinStmtItemType itemType) {
		this.itemType = itemType;
	}

	public int getFinYear() {
		return finYear;
	}

	public void setFinYear(int finYear) {
		this.finYear = finYear;
	}

	public ObjParty getParty() {
		return party;
	}

	public void setParty(ObjParty party) {
		this.party = party;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTimestampCreate() {
		return timestampCreate;
	}

	public void setTimestampCreate(Date timestampCreate) {
		this.timestampCreate = timestampCreate;
	}

	public EFinStmtType getFinStmtType() {
		return finStmtType;
	}

	public void setFinStmtType(EFinStmtType finStmtType) {
		this.finStmtType = finStmtType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
