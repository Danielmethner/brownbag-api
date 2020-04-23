package com.brownbag_api.model;

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

import com.brownbag_api.model.enums.EBalSheetItemType;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "BAL_SHEET_ITEM")
public class BalSheetItem implements Serializable {

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
	@Column(name = "QTY")
	private double qty;

	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private EBalSheetItemType itemType;

	@NotNull
	@Column(name = "FIN_YEAR", updatable = false)
	private int finYear;

	@NotNull
	@ManyToOne(targetEntity = Party.class)
	@JoinColumn(name = "PARTY_ID")
	@JsonBackReference
	private Party party;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_CREATED", updatable = false)
	private Date timestampCreate;

	@NotNull
	@ManyToOne(targetEntity = BalSheetSectionType.class)
	@JoinColumn(name = "BAL_SHEET_SECTION_ID")
	@JsonBackReference
	private BalSheetSectionType balSheetSection;

	public BalSheetItem() {
	}

	public BalSheetItem(@NotNull double qty, EBalSheetItemType itemType, @NotNull int finYear, @NotNull Party party,
			@NotNull BalSheetSectionType balSheetSection) {
		super();
		this.name = balSheetSection.getName() + ": " + itemType.getName();
		this.qty = qty;
		this.itemType = itemType;
		this.finYear = finYear;
		this.party = party;
		this.balSheetSection = balSheetSection;
	}

	public BalSheetSectionType getBalSheetSection() {
		return balSheetSection;
	}

	public void setBalSheetSection(BalSheetSectionType balSheetSection) {
		this.balSheetSection = balSheetSection;
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

	public EBalSheetItemType getItemType() {
		return itemType;
	}

	public void setItemType(EBalSheetItemType itemType) {
		this.itemType = itemType;
	}

	public int getFinYear() {
		return finYear;
	}

	public void setFinYear(int finYear) {
		this.finYear = finYear;
	}

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

}
