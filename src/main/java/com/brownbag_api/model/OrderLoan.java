package com.brownbag_api.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.brownbag_api.model.data.EOrderStatus;
import com.brownbag_api.model.data.EOrderType;

@Entity
@Table(name = "order_loan")
public class OrderLoan extends Order implements Serializable {

	private static final long serialVersionUID = 4643589803146964779L;

	public OrderLoan() {
	}

	public OrderLoan(@NotNull int qty, @NotNull Asset asset, EOrderType orderType, EOrderStatus orderStatus,
			@NotNull User user) {
		super(qty, asset, orderType, orderStatus, user, null);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
