package com.brownbag_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Order;
import com.brownbag_api.model.OrderTrans;
import com.brownbag_api.model.data.EOrderAction;
import com.brownbag_api.repo.OrderRepo;
//import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.repo.OrderTransRepo;

@Service
public class OrderSvc {

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private OrderTransRepo orderTransRepo;
	
	@Autowired
	private LogSvc logSvc;

	public Order execAction(Order order, EOrderAction orderAction) {
		if(order.getOrderStatus() == orderAction.getNewStatus()) {
			logSvc.write("Old WFC Status and New WFC Status are identical! Order cannot proceed");
			return null;
		}
		OrderTrans orderTrans = new OrderTrans();
		orderTrans.setOrderStatusOld(order.getOrderStatus());
		order.setOrderStatus(orderAction.getNewStatus());
		orderTrans.setOrderStatusNew(order.getOrderStatus());
		orderTrans.setOrderAction(orderAction);
		order = orderRepo.save(order);
		orderTrans.setOrder(order);
		orderTransRepo.save(orderTrans);
		return order;
	}

}
