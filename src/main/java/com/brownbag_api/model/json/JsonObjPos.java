package com.brownbag_api.model.json;

import com.brownbag_api.model.jpa.ObjPos;

public class JsonObjPos {

	private Long id;
	private String name;
	private double qty;
	private double qtyBlocked;
	public String assetName;
	private Long partyId;
	private String partyName;
	private double priceAvg;
	private double priceLast;
	private double profitLoss;

	public JsonObjPos(ObjPos jpaPos) {
		super();
		this.id = jpaPos.getId();
		this.name = jpaPos.getName();
		this.qty = jpaPos.getQty();
		this.qtyBlocked = jpaPos.getQtyBlocked();
		this.assetName = jpaPos.getAsset().getName();
		this.setPartyId(jpaPos.getParty().getId());
		this.partyName = jpaPos.getParty().getName();
		this.priceAvg = jpaPos.getPriceAvg();
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

	public double getPriceLast() {
		return priceLast;
	}

	public void setPriceLast(double priceLast) {
		this.priceLast = priceLast;
	}

	public double getProfitLoss() {
		return profitLoss;
	}

	public void setProfitLoss(double profitLoss) {
		this.profitLoss = profitLoss;
	}

	public Long getPartyId() {
		return partyId;
	}

	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}

}
