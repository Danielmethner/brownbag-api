package com.brownbag_api.service;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.AssetLoan;
import com.brownbag_api.model.OrderLoan;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.PosLoan;
import com.brownbag_api.model.User;
import com.brownbag_api.model.enums.EAsset;
import com.brownbag_api.model.enums.EAssetGrp;
import com.brownbag_api.model.enums.EOrderAction;
import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;

@Service
public class OrderLoanSvc extends OrderSvc {

	@Autowired
	private AssetSvc assetSvc;

	@Autowired
	private OrderSvc orderSvc;

	@Autowired
	private OrderPaySvc orderPaySvc;

	@Autowired
	private PosSvc posSvc;

	@Autowired
	private LogSvc logSvc;

	/**
	 * Does not persist order!
	 *
	 */
	public OrderLoan createLoan(double qty, @NotNull User user, Pos maccGrant, Pos maccRcv, Date matDate,
			double intrRate) {

		String bookText = "Loan from: '" + maccGrant.getParty().getName() + "' to: '" + maccRcv.getParty().getName()
				+ "'. Intr Rate: " + intrRate;

		String userString = "User: " + user.getName();

		if (maccGrant == maccRcv) {
			logSvc.write("OrderLoanSvc.createLoan(): " + userString
					+ ": 'MACC From' and 'MACC to' must not be identical. " + bookText);
			return null;
		}
		AssetLoan assetLoan = (AssetLoan) assetSvc.getByEnum(EAsset.LOAN_GENERIC);
		OrderLoan orderLoan = new OrderLoan(qty, assetLoan, EOrderType.LOAN, EOrderStatus.NEW, user, bookText,
				maccGrant, maccRcv, null, matDate, intrRate);

		orderSvc.execAction(orderLoan, EOrderAction.HOLD);

		return orderLoan;
	}

	public OrderLoan grantLoan(OrderLoan orderLoan) {

		if (orderLoan == null) {
			logSvc.write("OrderLoanSvc.grantLoan(): OrderLoan is null!");
			return null;
		}

		if (orderLoan.getId() == null) {
			logSvc.write("OrderLoanSvc.grantLoan(): OrderLoan has not been successfully persisted!");
			return null;
		}

		Party partyLender = orderLoan.getMaccLender().getParty();

		// TRANSFER CASH
		OrderPay orderPay = orderPaySvc.createPay(orderLoan.getQty(), orderLoan.getUser(),
				orderLoan.getMaccLender().getAsset(), "Payout for: " + orderLoan.getAdvText(), orderLoan.getMaccLender(),
				orderLoan.getMaccDebtor());
		orderPay = orderPaySvc.execPay(orderPay);

		// CREATE LOAN ASSET
		AssetLoan assetLoan = assetSvc.createAssetLoan(orderLoan.getAdvText(), EAssetGrp.LOAN, partyLender,
				orderLoan.getMatDate(), orderLoan.getIntrRate());

		// CREATE LOAN POSITION
		PosLoan posLoan = posSvc.createPosLoan(0, assetLoan, partyLender, orderLoan.getMaccLender(),
				orderLoan.getMaccDebtor());

		orderLoan.setPosLoan(posLoan);

		posLoan = posSvc.creditPos(orderLoan);

		orderLoan.setPosLoan(posLoan);

		return (OrderLoan) orderSvc.execAction(orderLoan, EOrderAction.VERIFY);
	}

//	@Transactional
//	public OrderLoan execPay(OrderLoan OrderLoan) {
//
//		if (OrderLoan == null) {
//			return null;
//		}
//
//		if (OrderLoan.getId() == null) {
//			OrderLoan = orderRepo.save(OrderLoan);
//		}
//		OrderLoan.setPosSend(posSvc.debitPos(OrderLoan));
//		OrderLoan.setPosRcv(posSvc.crebitPos(OrderLoan));
//
//		return (OrderLoan) orderSvc.execAction(OrderLoan, EOrderAction.VERIFY);
//	}

}
