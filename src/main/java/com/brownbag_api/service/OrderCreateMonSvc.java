package com.brownbag_api.service;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brownbag_api.model.enums.EAsset;
import com.brownbag_api.model.enums.EOrderAction;
import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;
import com.brownbag_api.model.enums.EParty;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjPos;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.model.jpa.OrderCreateMon;
import com.brownbag_api.model.jpa.OrderPay;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.repo.PartyRepo;

@Service
public class OrderCreateMonSvc extends OrderSvc {

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

	@Autowired
	private PartyRepo partyRepo;

	@Autowired
	private PartySvc partySvc;

	public OrderPay createPay(double qty, @NotNull ObjUser user, ObjAsset assetCash, String bookText, ObjPos maccSend,
			ObjPos maccRcv) {
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
//		posSvc.crebitPos(orderPay);

		return (OrderPay) orderSvc.execAction(orderPay, EOrderAction.VERIFY);
	}

	@Transactional
	public OrderCreateMon createMon(ObjParty partySend, @NotNull double amount) {
		ObjPos maccCentralBank = partySvc.getMacc(partySend);
		OrderCreateMon orderCreateMon = new OrderCreateMon(amount, maccCentralBank.getAsset(), EOrderType.CREATE_MONEY,
				EOrderStatus.NEW, partySend.getUser(), maccCentralBank, "Money Creation: " + partySend.getName());
		orderCreateMon = (OrderCreateMon) orderSvc.execAction(orderCreateMon, EOrderAction.HOLD);
		orderCreateMon.setPosRcv(posSvc.creditPos(orderCreateMon));
		ObjAsset curry = maccCentralBank.getAsset();
		curry.raiseTotalShares((@NotNull int) amount);

		return (OrderCreateMon) orderSvc.execAction(orderCreateMon, EOrderAction.VERIFY);
	}

	/**
	 * DEMO DATA
	 *
	 * @param eParty
	 * @param amount
	 */
	public void createMon(EParty eParty, @NotNull int amount) {
		ObjParty partySend = partyRepo.findByName(EParty.ECB.toString());
		createMon(partySend, amount);
	}

}
