package com.brownbag_api.service;

import com.brownbag_api.model.Order;
import com.brownbag_api.model.enums.EOrderAction;

public interface OrderSvcIntf {

	public Order execAction(Order order, EOrderAction orderAction);
}
