package com.brownbag_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EOrderAction;
import com.brownbag_api.model.enums.EUser;
import com.brownbag_api.model.jpa.ObjPosLoan;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.model.jpa.OrderIntr;
import com.brownbag_api.repo.OrderIntrRepo;

@Service
public class OrderIntrSvc extends OrderSvc {

	@Autowired
	private OrderSvc orderSvc;

	@Autowired
	private PosLoanSvc posLoanSvc;

	@Autowired
	private OrderIntrRepo orderIntrRepo;

	@Autowired
	private UserSvc userSvc;

	public OrderIntr generateIntrPay(ObjPosLoan posLoan, LocalDateTime currentFinDate) {

		double intrRate = posLoan.getAsset().getIntrRate();
		double amount = posLoan.getQty() * (intrRate / 100);
		amount = Math.round(amount * 100d) / 100d;
		String advText = "Interest Payment. Loan Position: '" + posLoan.getId() + "' Fin Date: '"
				+ currentFinDate.toLocalDate().toString() + "' Amount: '" + amount;
		ObjUser uEOP = userSvc.getByEnum(EUser.U_EOP);
		OrderIntr orderIntr = new OrderIntr(posLoan, uEOP, advText, amount);
		orderSvc.execAction(orderIntr, EOrderAction.HOLD);
		return orderIntr;
	}

	public OrderIntr execIntrPay(OrderIntr orderIntr) {

		if (orderIntr == null) {
			return null;
		}

		if (orderIntr.getId() == null) {
			orderIntr = orderIntrRepo.save(orderIntr);
		}
		orderIntr.setMaccDebtor(posLoanSvc.debitPosMacc(orderIntr));
		orderIntr.setMaccLender(posLoanSvc.creditPosMacc(orderIntr));

		return (OrderIntr) orderSvc.execAction(orderIntr, EOrderAction.OPN_VERIFY);

	}

	public void chargeInterestAll(LocalDateTime currentFinDate) {
		List<ObjPosLoan> posLoanList = posLoanSvc.getAllWithBal();
		for (ObjPosLoan posLoan : posLoanList) {
			OrderIntr orderIntr = generateIntrPay(posLoan, currentFinDate);
			orderIntr = execIntrPay(orderIntr);
		}

	}

}
