package com.brownbag_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EOrderAction;
import com.brownbag_api.model.jpa.Order;
import com.brownbag_api.model.jpa.OrderTrans;
import com.brownbag_api.repo.OrderRepo;
//import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.repo.OrderTransRepo;

@Service
public class OrderSvc implements OrderSvcIntf {

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private OrderTransRepo orderTransRepo;

	@Autowired
	private LogSvc logSvc;

	@Override
	public Order execAction(Order order, EOrderAction orderAction) {
		if (order.getOrderStatus() == orderAction.getNewStatus()) {
			logSvc.write("Old WFC Status and New WFC Status are identical! Order cannot proceed");
			return null;
		}
		if (orderAction == EOrderAction.ASSET_DISCARD && !order.getOrderStatus().discardeable) {
			logSvc.write("Order Status does not allow the order to be discarded!");
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

	public Order getById(Long id) {
		return orderRepo.getOne(id);
	}
}
