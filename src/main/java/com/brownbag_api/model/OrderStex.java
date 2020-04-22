package com.brownbag_api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.brownbag_api.model.data.EOrderDir;
import com.brownbag_api.model.data.EOrderStatus;
import com.brownbag_api.model.data.EOrderType;

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

	public OrderStex() {
	}

	public OrderStex(@NotNull double price) {
		super();
		this.priceLimit = price;
	}

	public OrderStex(EOrderDir orderDir, @NotNull int qty, @NotNull Asset asset, EOrderType orderType,
			EOrderStatus orderStatus, @NotNull User user, @NotNull double price) {
		super(qty, asset, orderType, orderStatus, user, "STEX Order: '" + orderDir.toString() + "'; User: '"
				+ user.getName() + "'; Asset: '" + asset.getName() + "'; Qty: '" + qty + "'");
		this.orderDir = orderDir;
		this.priceLimit = price;
	}

	public double getPrice() {
		return priceLimit;
	}

	public void setPrice(double price) {
		this.priceLimit = price;
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

}
