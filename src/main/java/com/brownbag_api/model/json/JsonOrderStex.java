package com.brownbag_api.model.json;

import java.time.LocalDateTime;

import com.brownbag_api.model.enums.EOrderDir;
import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.OrderStex;

public class JsonOrderStex extends JsonOrder {

	private double priceLimit;

	private EOrderDir orderDir;

	private Long partyId;

	private String partyName;

	private double qtyExec;

	private double intrRate;

	private LocalDateTime matDate;
	private double nomVal;

	public JsonOrderStex(OrderStex orderStex) {
		super(orderStex);
		this.priceLimit = orderStex.getPriceLimit();
		this.orderDir = orderStex.getOrderDir();
		ObjParty objParty = orderStex.getParty();
		if (objParty != null) {
			this.partyName = orderStex.getParty().getName();
			this.partyId = orderStex.getParty().getId();

		}
		this.qtyExec = orderStex.getQtyExec();
	}

	public JsonOrderStex(double qty, EOrderType orderType, EOrderStatus orderStatus, Long assetId, Long userId,
			double priceLimit, EOrderDir orderDir, String partyName, double qtyExec, double intrRate,
			LocalDateTime matDate, double nomVal) {
		super(qty, orderType, orderStatus, assetId, userId);
		this.priceLimit = priceLimit;
		this.orderDir = orderDir;
		this.partyName = partyName;
		this.qtyExec = qtyExec;
		this.setIntrRate(intrRate);
		this.setMatDate(matDate);
		this.setNomVal(nomVal);
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

	public LocalDateTime getMatDate() {
		return matDate;
	}

	public void setMatDate(LocalDateTime matDate) {
		this.matDate = matDate;
	}

	public double getNomVal() {
		return nomVal;
	}

	public void setNomVal(double nomVal) {
		this.nomVal = nomVal;
	}

}
