package com.brownbag_api.model.json;

import com.brownbag_api.model.jpa.ObjPos;
import com.brownbag_api.model.jpa.ObjPosLoan;
import com.brownbag_api.model.jpa.ObjAssetLoan;

public class JsonObjPosLoan {

	private Long id;
	private String name;
	private double qty;
	private double qtyBlocked;
	public String assetName;
	private String partyName;
	private double priceAvg;
	private Long partyId;
	private double intrRate;

	public JsonObjPosLoan(ObjPosLoan jpaPos) {
		super();
		this.id = jpaPos.getId();
		this.name = jpaPos.getName();
		this.qty = jpaPos.getQty();
		this.qtyBlocked = jpaPos.getQtyBlocked();
		this.partyId = jpaPos.getParty().getId();
		this.partyName = jpaPos.getParty().getName();
		this.intrRate = jpaPos.getAssetLoan().getIntrRate();
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

	public double getPriceAvg() {
		return priceAvg;
	}

	public void setPriceAvg(double priceAvg) {
		this.priceAvg = priceAvg;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public Long getPartyId() {
		return partyId;
	}

	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}

	public double getIntrRate() {
		return intrRate;
	}

	public void setIntrRate(double intrRate) {
		this.intrRate = intrRate;
	}

	
}
