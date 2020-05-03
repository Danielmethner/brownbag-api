package com.brownbag_api.model;

import java.io.Serializable;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.brownbag_api.model.enums.EBalSheetSectionType;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "BAL_SHEET_SECTION")
public class ObjBalSheetSection implements Serializable {

	private static final long serialVersionUID = 7386741814449599918L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotNull
	@ManyToOne(targetEntity = ObjBalSheet.class)
	@JoinColumn(name = "BAL_SHEET_ID")
	@JsonBackReference
	private ObjBalSheet balSheet;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private EBalSheetSectionType section;

	@NotBlank
	@Size(max = 100)
	private String name;

	@NotNull
	@Column(name = "QTY", columnDefinition = "Decimal(20,2)")
	private double qty;

	public ObjBalSheetSection() {

	}

	public ObjBalSheetSection(@NotNull ObjBalSheet balSheet, EBalSheetSectionType eBalSheetSection, double qty) {
		super();
		this.balSheet = balSheet;
		this.section = eBalSheetSection;
		this.qty = qty;
		this.name = balSheet.getParty().getName() + ": " + eBalSheetSection.getName() + ": " + "Year: "
				+ balSheet.getFinYear();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ObjBalSheet getBalSheet() {
		return balSheet;
	}

	public void setBalSheet(ObjBalSheet balSheet) {
		this.balSheet = balSheet;
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
	
	public void increaseQty(double qty) {
		this.qty = this.qty + qty;
	}

}
