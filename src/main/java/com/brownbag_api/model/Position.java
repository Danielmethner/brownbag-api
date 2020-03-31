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
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "POSITION")
public class Position implements Serializable {

	private static final long serialVersionUID = 1530699992135610397L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "PRICE_AVG")
	private double priceAvg;

	@NotNull
	@Column(name = "QTY")
	private int qty;
	
	@NotNull
	@ManyToOne(targetEntity = Asset.class)
	@JoinColumn(name = "ASSET_ID")
	public Asset asset;

	@NotNull
	@ManyToOne(targetEntity = LegalEntity.class)
	@JoinColumn(name = "LE_OWNER_ID")
	@JsonBackReference
	private LegalEntity owner;

	public Position() {
	}

	public Position(@NotNull double priceAvg, @NotNull int qty, @NotNull Asset asset, @NotNull LegalEntity owner) {
		super();
		this.priceAvg = priceAvg;
		this.qty = qty;
		this.asset = asset;
		this.owner = owner;
	}

	public double getPriceAvg() {
		return priceAvg;
	}

	public void setPriceAvg(double priceAvg) {
		this.priceAvg = priceAvg;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
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

	public LegalEntity getOwner() {
		return owner;
	}

	public void setOwner(LegalEntity owner) {
		this.owner = owner;
	}

}
