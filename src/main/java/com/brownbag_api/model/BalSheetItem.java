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

import com.brownbag_api.model.data.EBalSheetItem;
import com.brownbag_api.model.data.EBalSheetSection;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "BAL_SHEET_ITEM")
public class BalSheetItem implements Serializable {

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
	private EBalSheetItem eBalSheetItem;

	@NotNull
	@ManyToOne(targetEntity = BalSheetSection.class)
	@JoinColumn(name = "BAL_SHEET_SECTION_ID")
	@JsonBackReference
	private BalSheetSection balSheetSection;

	public BalSheetItem() {
	}

	public BalSheetItem(EBalSheetItem eBalSheetItem, @NotNull BalSheetSection balSheetSection, double qty) {
		super();
		this.name = balSheetSection.getName() + ": " + eBalSheetItem.getName();
		this.eBalSheetItem = eBalSheetItem;
		this.balSheetSection = balSheetSection;
		this.qty = qty;
	}

	public BalSheetSection getBalSheetSection() {
		return balSheetSection;
	}

	public void setBalSheetSection(BalSheetSection balSheetSection) {
		this.balSheetSection = balSheetSection;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EBalSheetItem geteBalSheetItem() {
		return eBalSheetItem;
	}

	public void seteBalSheetItem(EBalSheetItem eBalSheetItem) {
		this.eBalSheetItem = eBalSheetItem;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

}
