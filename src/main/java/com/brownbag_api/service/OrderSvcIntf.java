package com.brownbag_api.service;

import com.brownbag_api.model.enums.EOrderAction;
import com.brownbag_api.model.jpa.Order;

public interface OrderSvcIntf {

	public Order execAction(Order order, EOrderAction orderAction);
}
