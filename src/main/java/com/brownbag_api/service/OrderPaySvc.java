package com.brownbag_api.service;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.BalSheet;
import com.brownbag_api.model.Log;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.User;
import com.brownbag_api.model.data.EAsset;
import com.brownbag_api.model.data.EBookType;
import com.brownbag_api.model.data.EOrderAction;
import com.brownbag_api.model.data.EOrderStatus;
import com.brownbag_api.model.data.EOrderType;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.LogRepo;
import com.brownbag_api.repo.OrderPayRepo;
import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.util.UtilDate;

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

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private LogSvc logSvc;

	public OrderPay createPay(double qty, @NotNull User user, Asset assetCash, String bookText, Pos maccSend,
			Pos maccRcv) {
		String userString = "User: " + user.getName();
		String orderString = "Pay Order: Amount: " + qty + " MACC From: '" + maccSend.getName() + "' MACC To: '"
				+ maccRcv.getName() + "'";
		if (maccSend == maccRcv) {
			logSvc.write(userString + ": 'MACC From' and 'MACC to' must not be identical. " + orderString);
			return null;
		}
		assetCash = assetCash == null ? assetRepo.findByName(EAsset.EUR.getName()) : assetCash;
		if (assetCash == null) {
			assetCash = assetRepo.findByName(EAsset.EUR.getName());
			logSvc.write(userString + ": No Currency set. EUR will be set as default. " + orderString);
		}
		OrderPay orderPay = new OrderPay(qty, assetCash, EOrderType.PAY, EOrderStatus.NEW, user, maccSend, maccRcv,
				null);

		return orderPay;
	}

	public OrderPay execPay(OrderPay orderPay) {

		if (orderPay == null) {
			return null;
		}

		if (orderPay.getId() == null) {
			orderPay = orderRepo.save(orderPay);
		}
		posSvc.debitPos(orderPay);
		posSvc.crebitPos(orderPay);

		return (OrderPay) orderSvc.execAction(orderPay, EOrderAction.VERIFY);
	}

}
