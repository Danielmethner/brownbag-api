package com.brownbag_api.model.json;

import com.brownbag_api.model.enums.EOrderDir;
import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;
import com.brownbag_api.model.jpa.OrderStex;

public class JsonOrderStex extends JsonOrder {

	private double priceLimit;

	private EOrderDir orderDir;

	private String partyName;

	private double qtyExec;

	public JsonOrderStex(OrderStex orderStex) {
		super(orderStex);
		this.priceLimit = orderStex.getPriceLimit();
		this.orderDir = orderStex.getOrderDir();
		this.partyName = orderStex.getParty().getName();
		this.qtyExec = orderStex.getQtyExec();
	}

	public JsonOrderStex(double qty, EOrderType orderType, EOrderStatus orderStatus, Long assetId, Long userId,
			double priceLimit, EOrderDir orderDir, String partyName, double qtyExec) {
		super(qty, orderType, orderStatus, assetId, userId);
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
