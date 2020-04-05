package com.brownbag_api.service;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.Order;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.OrderTrans;
import com.brownbag_api.model.Position;
import com.brownbag_api.model.User;
import com.brownbag_api.model.data.EAsset;
import com.brownbag_api.model.data.EOrderAction;
import com.brownbag_api.model.data.EOrderStatus;
import com.brownbag_api.model.data.EOrderType;
import com.brownbag_api.model.data.EUser;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.OrderPayRepo;
import com.brownbag_api.repo.OrderRepo;
//import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.repo.OrderTransRepo;

@Service
public class OrderPaySvc extends OrderSvc {

	@Autowired
	private AssetRepo assetRepo;

	@Autowired
	private OrderPayRepo orderPayRepo;

	@Autowired
	private OrderSvc orderSvc;

	@Autowired
	private PosSvc posSvc;

	public OrderPay createPay(int qty, @NotNull User user, Asset assetCash,
			String bookText, Position maccSend, Position maccRcv) {
		
		assetCash = assetCash == null ? assetRepo.findByName(EAsset.CASH.getName()) : assetCash;
		
		OrderPay orderPay = new OrderPay(qty, assetCash, EOrderType.PAY, EOrderStatus.NEW, user, maccSend, maccRcv, bookText);

		return orderPay;
	}

	public void execPay(OrderPay orderPay) {

		posSvc.debitPos(orderPay.getPosSend(), orderPay.getQty());
		posSvc.crebitPos(orderPay.getPosRcv(), orderPay.getQty());
		orderSvc.execAction(orderPay, EOrderAction.VERIFY);

	}

}
