package com.brownbag_api.model.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.brownbag_api.model.enums.EOrderDir;
import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;

@Entity
@Table(name = "order_stex")
public class OrderStex extends Order implements Serializable {

	private static final long serialVersionUID = 4643589803146964779L;

	@NotNull
	@Column(name = "PRICE_LIMIT")
	private double priceLimit;

	@NotNull
	@Column(name = "DIRECTION")
	private EOrderDir orderDir;

	@NotNull
	@ManyToOne(targetEntity = ObjParty.class)
	@JoinColumn(name = "PARTY_ID")
	private ObjParty party;

	@Column(name = "QTY_EXEC", columnDefinition = "Decimal(20,2)")
	private double qtyExec;

	public OrderStex() {
	}

	public OrderStex(@NotNull double price) {
		super();
		this.priceLimit = price;
	}

	public OrderStex(EOrderDir orderDir, @NotNull int qty, @NotNull ObjAsset asset, EOrderType orderType,
			EOrderStatus orderStatus, @NotNull ObjUser user, @NotNull double price, @NotNull ObjParty party, double qtyExec) {
		super(qty, asset, orderType, orderStatus, user, orderType.getName() + " Order: '" + orderDir.toString()
				+ "'; User: '" + user.getName() + "'; Asset: '" + asset.getName() + "'; Qty: '" + qty + "'");
		this.orderDir = orderDir;
		this.priceLimit = price;
		this.party = party;
		this.qtyExec = qtyExec;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public EOrderDir getOrderDir() {
		return orderDir;
	}

	public void setOrderDir(EOrderDir orderDir) {
		this.orderDir = orderDir;
	}

	public double getPriceLimit() {
		return priceLimit;
	}

	public void setPriceLimit(double priceLimit) {
		this.priceLimit = priceLimit;
	}

	public ObjParty getParty() {
		return party;
	}

	public void setParty(ObjParty party) {
		this.party = party;
	}

	public double getQtyExec() {
		return qtyExec;
	}

	public void setQtyExec(double qtyExec) {
		this.qtyExec = qtyExec;
	}
	
	public void raiseQtyExec(double qtyExec) {
		this.qtyExec = this.qtyExec + qtyExec;
	}

	public double getQtyOpn() {
		return this.getQty() - this.qtyExec;
	}

}
