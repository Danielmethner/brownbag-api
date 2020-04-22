package com.brownbag_api.service;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.User;
import com.brownbag_api.model.data.EAsset;
import com.brownbag_api.model.data.EOrderAction;
import com.brownbag_api.model.data.EOrderStatus;
import com.brownbag_api.model.data.EOrderType;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.OrderRepo;

@Service
public class OrderPaySvc extends OrderSvc {

	@Autowired
	private AssetRepo assetRepo;

	@Autowired
	private OrderSvc orderSvc;

	@Autowired
	private PosSvc posSvc;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private LogSvc logSvc;

	/**
	 * Does not persist order!
	 *
	 * @param qty
	 * @param user
	 * @param assetCash
	 * @param bookText
	 * @param maccSend
	 * @param maccRcv
	 * @return
	 */
	public OrderPay createPay(double qty, @NotNull User user, Asset assetCash, String bookText, Pos maccSend,
			Pos maccRcv) {
		String userString = "User: " + user.getName();
		bookText = bookText != null ? bookText
				: "Pay Order: Amount: '" + qty + "'. MACC From: '" + maccSend.getName() + "'. MACC To: '"
						+ maccRcv.getName() + "'";
		if (maccSend == maccRcv) {
			logSvc.write(userString + ": 'MACC From' and 'MACC to' must not be identical. " + bookText);
			return null;
		}
		assetCash = assetCash == null ? assetRepo.findByName(EAsset.EUR.getName()) : assetCash;
		if (assetCash == null) {
			assetCash = assetRepo.findByName(EAsset.EUR.getName());
			logSvc.write(userString + ": No Currency set. EUR will be set as default. " + bookText);
		}
		OrderPay orderPay = new OrderPay(qty, assetCash, EOrderType.PAY, EOrderStatus.NEW, user, maccSend, maccRcv,
				bookText);

		return orderPay;
	}

	@Transactional
	public OrderPay execPay(OrderPay orderPay) {

		if (orderPay == null) {
			return null;
		}

		if (orderPay.getId() == null) {
			orderPay = orderRepo.save(orderPay);
		}
		orderPay.setPosSend(posSvc.debitPos(orderPay));
		orderPay.setPosRcv(posSvc.creditPos(orderPay));

		return (OrderPay) orderSvc.execAction(orderPay, EOrderAction.VERIFY);
	}

}
