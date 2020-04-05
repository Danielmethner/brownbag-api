package com.brownbag_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Order;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.OrderTrans;
import com.brownbag_api.model.data.EOrderAction;
import com.brownbag_api.repo.OrderPayRepo;
import com.brownbag_api.repo.OrderRepo;
//import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.repo.OrderTransRepo;

@Service
public class OrderPaySvc extends OrderSvc {

	@Autowired
	private OrderPayRepo orderPayRepo;
	
	@Autowired
	private OrderSvc orderSvc;

	public void execPay(OrderPay orderPay) {
//		orderPay.get
		
		orderSvc.execAction(orderPay, EOrderAction.VERIFY);
		
	}


}
