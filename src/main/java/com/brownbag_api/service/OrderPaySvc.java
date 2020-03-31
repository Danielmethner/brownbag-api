package com.brownbag_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Order;
import com.brownbag_api.model.OrderTrans;
import com.brownbag_api.model.data.EOrderAction;
import com.brownbag_api.repo.OrderPayRepo;
import com.brownbag_api.repo.OrderRepo;
//import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.repo.OrderTransRepo;

@Service
public class OrderPaySvc {

	@Autowired
	private OrderPayRepo orderPayRepo;

	public void execAction(Order order, EOrderAction orderAction) {
		
	}

}
