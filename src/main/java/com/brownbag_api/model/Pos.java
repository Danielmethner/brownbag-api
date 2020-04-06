package com.brownbag_api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "POS")
public class Pos implements Serializable {

	private static final long serialVersionUID = 1530699992135610397L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotBlank
	@Size(max = 100)
	private String name;

	@Column(name = "PRICE_AVG")
	private double priceAvg;

	@NotNull
	@Column(name = "QTY")
	private double qty;

	@NotNull
	@Column(name = "QTY_BLOCKED")
	private double qtyBlocked;

	@NotNull
	@Column(name = "OVERDRAFT_LIMIT")
	private double odLimit;

	@NotNull
	@ManyToOne(targetEntity = Asset.class)
	@JoinColumn(name = "ASSET_ID")
	public Asset asset;

	@NotNull
	@ManyToOne(targetEntity = Party.class)
	@JoinColumn(name = "PARTY_ID")
	@JsonBackReference
	private Party party;

	@NotNull
	@Column(name = "IS_MACC", columnDefinition = "tinyint default false")
	private boolean isMacc = false;

	public Pos() {
	}

	public Pos(double priceAvg, @NotNull double qty, @NotNull double qtyBlocked, @NotNull double odLimit,
			@NotNull Asset asset, @NotNull Party owner, @NotNull boolean isMacc) {
		super();
		this.priceAvg = priceAvg;
		this.qty = qty;
		this.qtyBlocked = qtyBlocked;
		this.odLimit = odLimit;
		this.asset = asset;
		this.party = owner;
		this.name = genName();
		this.isMacc = isMacc;
	}

	private String genName() {
		return this.party.getName() + ": " + this.asset.getName();
	}

	public double getPriceAvg() {
		return priceAvg;
	}

	public void setPriceAvg(double priceAvg) {
		this.priceAvg = priceAvg;
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

	public double getOdLimit() {
		return odLimit;
	}

	public void setOdLimit(double odLimit) {
		this.odLimit = odLimit;
	}

	public boolean isMacc() {
		return isMacc;
	}

	public void setMacc(boolean isMacc) {
		this.isMacc = isMacc;
	}

}
