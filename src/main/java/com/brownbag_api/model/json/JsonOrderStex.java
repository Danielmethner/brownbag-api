package com.brownbag_api.model.json;

import com.brownbag_api.model.enums.EOrderDir;
import com.brownbag_api.model.jpa.Order;

public class JsonOrderStex extends JsonOrder {

	private double priceLimit;

	private EOrderDir orderDir;

	private String partyName;

	private double qtyExec;

	public JsonOrderStex(Order order, double priceLimit, EOrderDir orderDir, String partyName, double qtyExec) {
		super(order);
		this.priceLimit = priceLimit;
		this.orderDir = orderDir;
		this.partyName = partyName;
		this.qtyExec = qtyExec;
	}

	public double getPriceLimit() {
		return priceLimit;
	}

	public void setPriceLimit(double priceLimit) {
		this.priceLimit = priceLimit;
	}

	public EOrderDir getOrderDir() {
		return orderDir;
	}

	public void setOrderDir(EOrderDir orderDir) {
		this.orderDir = orderDir;
	}

	public double getQtyExec() {
		return qtyExec;
	}

	public void setQtyExec(double qtyExec) {
		this.qtyExec = qtyExec;
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

}
