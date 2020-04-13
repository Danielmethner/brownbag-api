package com.brownbag_api.service;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.BalSheet;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.User;
import com.brownbag_api.model.data.EAsset;
import com.brownbag_api.model.data.EBookType;
import com.brownbag_api.model.data.EOrderAction;
import com.brownbag_api.model.data.EOrderStatus;
import com.brownbag_api.model.data.EOrderType;
import com.brownbag_api.repo.AssetRepo;
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
	private BalSheetSvc balSheetSvc;

	@Autowired
	private OrderSvc orderSvc;

	@Autowired
	private PosSvc posSvc;
	
	@Autowired
	private OrderRepo orderRepo;

	public OrderPay createPay(int qty, @NotNull User user, Asset assetCash,
			String bookText, Pos maccSend, Pos maccRcv, @NotNull EBookType bookType) {
		
		assetCash = assetCash == null ? assetRepo.findByName(EAsset.EUR.getName()) : assetCash;
		
		OrderPay orderPay = new OrderPay(qty, assetCash, EOrderType.PAY, EOrderStatus.NEW, user, maccSend, maccRcv, bookType, null);

		return orderPay;
	}

	public void execPay(OrderPay orderPay) {

		if (orderPay.getId() == null) {
			orderPay = orderRepo.save(orderPay);
		}
		posSvc.debitPos(orderPay);
		BalSheet balSheet = balSheetSvc.getBalSheet(orderPay.getPosSend().getParty(), UtilDate.finYear);
		System.err.println("balSheetSvc: " + balSheet.getName());
		posSvc.crebitPos(orderPay);
		
		orderSvc.execAction(orderPay, EOrderAction.VERIFY);
	}
	
	

}
