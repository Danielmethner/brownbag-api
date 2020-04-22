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
public class Pos implements Serializable {

	private static final long serialVersionUID = 1530699992135610397L;

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

	@NotNull
	@Column(name = "QTY_BLOCKED")
	private double qtyBlocked;

	@NotNull
	@ManyToOne(targetEntity = Asset.class)
	@JoinColumn(name = "ASSET_ID")
	public Asset asset;

	@NotNull
	@ManyToOne(targetEntity = Party.class)
	@JoinColumn(name = "PARTY_ID")
	private Party party;

	@NotNull
	@Column(name = "IS_MACC", columnDefinition = "tinyint default false")
	private boolean isMacc = false;

	public Pos() {
	}

	public Pos(@NotNull double qty, @NotNull double qtyBlocked, @NotNull Asset asset, @NotNull Party owner) {
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

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
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

	public boolean isMacc() {
		return isMacc;
	}

	public void setMacc(boolean isMacc) {
		this.isMacc = isMacc;
	}

}
