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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotNull
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
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "USER_ID")
	@JsonBackReference
	private User posUser;

	public Position() {
	}

	public Position(Long id, @NotNull double priceAvg, @NotNull int qty, @NotNull Asset asset, @NotNull User user) {
		super();
		this.id = id;
		this.priceAvg = priceAvg;
		this.qty = qty;
		this.asset = asset;
		this.posUser = user;
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

	public User getUser() {
		return posUser;
	}

	public void setUser(User user) {
		this.posUser = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
