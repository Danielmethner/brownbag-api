package com.brownbag_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.ExecStex;
import com.brownbag_api.model.Order;
import com.brownbag_api.model.OrderStex;
import com.brownbag_api.model.OrderTrans;
import com.brownbag_api.model.User;
import com.brownbag_api.model.data.EOrderAction;
import com.brownbag_api.model.data.EOrderDir;
import com.brownbag_api.model.data.EOrderStatus;
import com.brownbag_api.model.data.EOrderType;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.OrderRepo;
//import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.repo.OrderTransRepo;

@Service
public class OrderStexSvc extends OrderSvc  {

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private OrderTransRepo orderTransRepo;

	@Autowired
	private AssetRepo assetRepo;

	@Autowired
	private OrderSvc orderSvc;

	@Autowired
	private PosSvc posSvc;

	@Autowired
	private LogSvc logSvc;

	public Order placeOrder(OrderStex orderStex) {
		if(orderStex.getId() == null) {
			orderStex = orderRepo.save(orderStex);
		}
		if (orderStex.getQty() <= 0 || orderStex.getPrice() <= 0) {
			logSvc.write("OrderStexSvc.placeOrder: Order with ID: " + orderStex.getId() + " must have a qty and price greater 0.");
			return null;
		}
		return orderSvc.execAction(orderStex, EOrderAction.PLACE);
	}

	public Order placeNewOrder(EOrderDir orderDir, EOrderType orderType, Asset asset, int qty, double price, User user,
			EOrderStatus orderStatus) {
		OrderStex orderStex = new OrderStex(orderDir, qty, asset, orderType, orderStatus, user, price);
		return placeOrder(orderStex);

	}

	public void matchOrders(OrderStex orderBuy, OrderStex orderSell) {
		ExecStex execStex;
	}
}
