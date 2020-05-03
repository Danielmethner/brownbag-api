package com.brownbag_api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "POS")
@Inheritance(strategy = InheritanceType.JOINED)
public class ObjPos implements Serializable {

	private static final long serialVersionUID = 1530699992135610397L;

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

	@NotNull
	@Column(name = "QTY_BLOCKED", columnDefinition = "Decimal(20,2)")
	private double qtyBlocked;

	@NotNull
	@ManyToOne(targetEntity = ObjAsset.class)
	@JoinColumn(name = "ASSET_ID")
	public ObjAsset asset;

	@NotNull
	@ManyToOne(targetEntity = ObjParty.class)
	@JoinColumn(name = "PARTY_ID")
	private ObjParty party;

	public ObjPos() {
	}

	public ObjPos(@NotNull double qty, @NotNull double qtyBlocked, @NotNull ObjAsset asset, @NotNull ObjParty owner) {
		super();
		this.qty = qty;
		this.qtyBlocked = qtyBlocked;
		this.asset = asset;
		this.party = owner;
		this.name = genName();
	}

	private String genName() {
		return this.party.getName() + ": " + this.asset.getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ObjAsset getAsset() {
		return asset;
	}

	public void setAsset(ObjAsset asset) {
		this.asset = asset;
	}

	public ObjParty getParty() {
		return party;
	}

	public void setParty(ObjParty party) {
		this.party = party;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public double getQtyBlocked() {
		return qtyBlocked;
	}

	public void setQtyBlocked(double qtyBlocked) {
		this.qtyBlocked = qtyBlocked;
	}

	public ObjPos raiseQtyBlocked(double qty) {
		setQtyBlocked(getQtyBlocked() + qty);
		return this;
	}

	public ObjPos lowerQtyBlocked(double qty) {
		setQtyBlocked(getQtyBlocked() - qty);
		return this;
	}
}
