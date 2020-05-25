package com.brownbag_api.model.json;

import com.brownbag_api.model.jpa.ObjPos;
import com.brownbag_api.model.jpa.ObjPosLoan;

import java.time.LocalDate;

import com.brownbag_api.model.jpa.ObjAssetLoan;

public class JsonObjPosLoan {

	private Long id;
	private String name;
	private double balance;
	private double principal;
	private double qtyBlocked;
	private Long lenderId;
	private String lenderName;
	private double intrRate;
	private LocalDate matDate;

	public JsonObjPosLoan(ObjPosLoan jpaPos) {
		super();
		this.id = jpaPos.getId();
		this.name = jpaPos.getName();
		this.balance = jpaPos.getQty();
		this.principal = jpaPos.getAsset().getTotalShares();
		this.qtyBlocked = jpaPos.getQtyBlocked();
		this.lenderId = jpaPos.getParty().getId();
		this.lenderName = jpaPos.getMaccLender().getParty().getName();
		this.intrRate = jpaPos.getAssetLoan().getIntrRate();
		this.matDate = jpaPos.getAssetLoan().getMatDate();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getQty() {
		return balance;
	}

	public void setQty(double qty) {
		this.balance = qty;
	}

	public double getQtyBlocked() {
		return qtyBlocked;
	}

	public void setQtyBlocked(double qtyBlocked) {
		this.qtyBlocked = qtyBlocked;
	}
	public double getIntrRate() {
		return intrRate;
	}

	public void setIntrRate(double intrRate) {
		this.intrRate = intrRate;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getPrincipal() {
		return principal;
	}

	public void setPrincipal(double principal) {
		this.principal = principal;
	}

	public Long getLenderId() {
		return lenderId;
	}

	public void setLenderId(Long lenderId) {
		this.lenderId = lenderId;
	}

	public String getLenderName() {
		return lenderName;
	}

	public void setLenderName(String lenderName) {
		this.lenderName = lenderName;
	}

	public LocalDate getMatDate() {
		return matDate;
	}

	public void setMatDate(LocalDate matDate) {
		this.matDate = matDate;
	}

}
