package com.brownbag_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EBookingDir;
import com.brownbag_api.model.enums.EFinStmtItemType;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjPosLoan;
import com.brownbag_api.model.jpa.ObjPosMacc;
import com.brownbag_api.model.jpa.OrderIntr;
import com.brownbag_api.model.jpa.OrderLoan;
import com.brownbag_api.model.trans.FinStmtTrxTrans;
import com.brownbag_api.repo.PosLoanRepo;
import com.brownbag_api.repo.PosRepo;

@Service
public class PosLoanSvc extends PosSvc {

	@Autowired
	private PosRepo posRepo;
	@Autowired
	private PosLoanRepo posLoanRepo;
	@Autowired
	private BookingSvc bookingSvc;

	public ObjPosLoan createPosLoan(double qty, ObjAsset assetLoan, ObjParty partyLender, ObjPosMacc maccLender,
			ObjPosMacc maccDebtor) {
		ObjPosLoan posLoan = new ObjPosLoan(qty, assetLoan, partyLender, maccLender, maccDebtor);
		return posRepo.save(posLoan);
	}

	// -----------------------------------------------------------------
	// LOAN - CREDIT LENDER
	// -----------------------------------------------------------------
	public ObjPosLoan creditPosLoan(OrderLoan orderLoan) {
		ObjParty partyLender = orderLoan.getMaccLender().getParty();
		double qty = orderLoan.getQty();

		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();
		// ASSETS - LENDER
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.BAL_A_LOANS, qty, EBookingDir.CREDIT, partyLender));
		// EQUITY - LENDER
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.BAL_E_RET_EARN, qty, EBookingDir.CREDIT, partyLender));

		// BOOKING
		return (ObjPosLoan) bookingSvc.createBooking(orderLoan, orderLoan.getPosLoanLender(), EBookingDir.CREDIT,
				balTrxList, orderLoan.getQty(), orderLoan.getAdvText());
	}

	// -----------------------------------------------------------------
	// LOAN - DEBIT BORROWER
	// -----------------------------------------------------------------
	public ObjPosLoan debitPosLoan(OrderLoan orderLoan) {
		ObjParty partyDebtor = orderLoan.getMaccDebtor().getParty();
		double qty = orderLoan.getQty();

		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();

		// LIABILITIES - BORROWER
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.BAL_L_LOANS, qty, EBookingDir.CREDIT, partyDebtor));
		// EQUITY - BORROWER
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.BAL_E_RET_EARN, qty, EBookingDir.DEBIT, partyDebtor));

		// BOOKING
		return (ObjPosLoan) bookingSvc.createBooking(orderLoan, orderLoan.getPosLoanBorrower(), EBookingDir.DEBIT,
				balTrxList, orderLoan.getQty(), orderLoan.getAdvText());
	}

	public List<ObjPosLoan> getFinancingByParty(ObjParty jpaParty) {
		return posLoanRepo.findByParty(jpaParty);
	}

	public List<ObjPosLoan> getAllWithBal() {
		return posLoanRepo.findByQtyGreaterThan(0);
	}

	// -----------------------------------------------------------------
	// CHARGE INTEREST - DEBIT
	// -----------------------------------------------------------------
	public ObjPosMacc debitPosMacc(OrderIntr orderIntr) {
		ObjParty partyPayer = orderIntr.getMaccDebtor().getParty();
		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();

		// DEBIT ASSETS
		balTrxList.add(
				new FinStmtTrxTrans(EFinStmtItemType.BAL_A_CASH, orderIntr.getQty(), EBookingDir.DEBIT, partyPayer));
		// DEBIT EQUITY
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.BAL_E_RET_EARN, orderIntr.getQty(), EBookingDir.DEBIT,
				partyPayer));

		// DEBIT PROFIT
		balTrxList
				.add(new FinStmtTrxTrans(EFinStmtItemType.PL_INTR, orderIntr.getQty(), EBookingDir.DEBIT, partyPayer));

		return (ObjPosMacc) bookingSvc.createBooking(orderIntr, orderIntr.getMaccDebtor(), EBookingDir.DEBIT,
				balTrxList, orderIntr.getQty(), orderIntr.getAdvText());
	}

	public ObjPosMacc creditPosMacc(OrderIntr orderIntr) {

		orderIntr.getMaccLender();
		ObjParty partyBenef = orderIntr.getMaccLender().getParty();
		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();
		double qty = orderIntr.getQty();
		EBookingDir bookingDir = EBookingDir.CREDIT;

		// DEBIT ASSETS
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.BAL_A_CASH, qty, bookingDir, partyBenef));
		// DEBIT EQUITY
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.BAL_E_RET_EARN, qty, bookingDir, partyBenef));

		// DEBIT PROFIT
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.PL_INTR, qty, bookingDir, partyBenef));

		return (ObjPosMacc) bookingSvc.createBooking(orderIntr, orderIntr.getMaccLender(), bookingDir, balTrxList, qty,
				orderIntr.getAdvText());
	}

}
