package com.brownbag_api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "ORD")
public class Order implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(length = 4)
	private EDirection direction;

	@NotNull
	@ManyToOne(targetEntity = Asset.class)
	@JoinColumn(name = "ASSET_ID")
	private Asset asset;

	@NotNull
	@Column(name = "QTY")
	private int qty;

	@NotNull
	@Column(name = "PRICE")
	private double price;

	@NotNull
	@Column(name = "AMOUNT")
	private double amount;

	@NotNull
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "USER_ID")
//	@JsonManagedReference
	private User user;

	public Order() {
	}

	public Order(EDirection direction, Asset asset, @NotNull int qty, @NotNull double price, @NotNull User user) {
		super();
		this.direction = direction;
		this.asset = asset;
		this.qty = qty;
		this.price = price;
		this.amount = price * qty;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EDirection getDirection() {
		return direction;
	}

	public void setDirection(EDirection direction) {
		this.direction = direction;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
