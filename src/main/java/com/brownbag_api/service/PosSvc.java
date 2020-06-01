package com.brownbag_api.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EAsset;
import com.brownbag_api.model.enums.EBookingDir;
import com.brownbag_api.model.enums.EFinStmtItemType;
import com.brownbag_api.model.enums.EFinStmtType;
import com.brownbag_api.model.jpa.ExecStex;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjAssetLoan;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjPos;
import com.brownbag_api.model.jpa.ObjPosLoan;
import com.brownbag_api.model.jpa.ObjPosMacc;
import com.brownbag_api.model.jpa.ObjPosStex;
import com.brownbag_api.model.jpa.OrderCreateMon;
import com.brownbag_api.model.jpa.OrderLoan;
import com.brownbag_api.model.jpa.OrderPay;
import com.brownbag_api.model.jpa.OrderStex;
import com.brownbag_api.model.trans.FinStmtTrxTrans;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.PartyRepo;
import com.brownbag_api.repo.PosLoanRepo;
import com.brownbag_api.repo.PosMaccRepo;
import com.brownbag_api.repo.PosRepo;
import com.brownbag_api.repo.PosStexRepo;

@Service
public class PosSvc {

	@Autowired
	private AssetRepo assetRepo;
	@Autowired
	private PosRepo posRepo;
	@Autowired
	private PosMaccRepo posMaccRepo;
	@Autowired
	private PosStexRepo posStexRepo;
	@Autowired
	private PosLoanRepo posLoanRepo;
	@Autowired
	private BookingSvc bookingSvc;
	@Autowired
	private OrderCreateMonSvc orderCreateMonSvc;
	@Autowired
	private OrderLoanSvc orderLoanSvc;
	@Autowired
	private PartyRepo partyRepo;
	@Autowired
	private PartySvc partySvc;


	public ObjPos save(ObjPos pos) {
		return posRepo.save(pos);
	}

	public ObjPosLoan createPosLoan(double qty, ObjAssetLoan assetLoan, ObjParty partyLender, ObjPos maccLender,
			ObjPos maccDebtor) {
		ObjPosLoan posLoan = new ObjPosLoan(qty, assetLoan, partyLender, maccLender, maccDebtor);
		return posRepo.save(posLoan);
	}

	public ObjPosMacc createPosMacc(double qty, ObjAsset assetCurry, ObjParty party) {
		ObjPosMacc posMacc = new ObjPosMacc(qty, assetCurry, party, 1);
		return posRepo.save(posMacc);
	}

	public ObjPosStex createPosStex(ObjAsset asset, ObjParty party) {
		ObjPosStex posStex = new ObjPosStex(0, 0, asset, party, 0);
		return posRepo.save(posStex);
	}

	public ObjPosStex createPosStex(ObjAsset asset, ObjParty partyOwner, int qty) {
		if (partyOwner == null) {
			System.err.println("Party Owner is null");
		}
		ObjPosStex posStex = new ObjPosStex(qty, 0, asset, partyOwner, 0);
		return posRepo.save(posStex);
	}

	public ObjPosMacc createMacc(@NotNull ObjParty owner, double odLimit, ObjAsset currency) {
		
		// SET EUR AS DEFAULT CURRENCY
		if (currency == null) {
			currency = assetRepo.findByName(EAsset.EUR.getName());
		}

		ObjPosMacc newMacc = createPosMacc(0, currency, owner);

		return newMacc;
	}

	public ObjPosMacc findByParty(ObjParty party) {
		List<ObjPosMacc> maccList = posMaccRepo.findByParty(party);
		return maccList.isEmpty() ? null : maccList.get(0);
	}

	public List<ObjPosStex> getByAsset(ObjAsset asset) {
		return posStexRepo.findByAsset(asset);
	}

	public ObjPos getByAssetAndParty(ObjAsset asset, ObjParty party) {
		ObjPos posStex = posRepo.findByAssetAndParty(asset, party);

		return posStex;
	}

	public double getQtyAvbl(ObjPos pos) {
		return pos.getQty() - pos.getQtyBlocked();
	}

	// -----------------------------------------------------------------
	// PAYMENT - DEBIT
	// -----------------------------------------------------------------
	public ObjPos debitPos(OrderPay orderPay) {
		ObjParty partyPayer = orderPay.getPosSend().getParty();
		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();
		// ASSETS
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.CASH, orderPay.getQty(), EBookingDir.DEBIT, partyPayer, EFinStmtType.BAL_SHEET));
		// LIABILITIES
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.EQUITY, orderPay.getQty(), EBookingDir.DEBIT, partyPayer, EFinStmtType.BAL_SHEET));

		// BOOKING
		return bookingSvc.createBooking(orderPay, orderPay.getPosSend(), EBookingDir.DEBIT, balTrxList,
				orderPay.getQty(), orderPay.getAdvText());

	}

	// -----------------------------------------------------------------
	// PAYMENT - CREDIT
	// -----------------------------------------------------------------
	public ObjPos creditPos(OrderPay orderPay) {
		ObjParty partyRecipient = orderPay.getPosRcv().getParty();
		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();
		// ASSETS
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.CASH, orderPay.getQty(), EBookingDir.CREDIT, partyRecipient, EFinStmtType.BAL_SHEET));
		// EQUITY
		balTrxList
				.add(new FinStmtTrxTrans(EFinStmtItemType.EQUITY, orderPay.getQty(), EBookingDir.CREDIT, partyRecipient, EFinStmtType.BAL_SHEET));

		// BOOKING
		return bookingSvc.createBooking(orderPay, orderPay.getPosRcv(), EBookingDir.CREDIT, balTrxList,
				orderPay.getQty(), orderPay.getAdvText());

	}

	// -----------------------------------------------------------------
	// MONEY CREATION - CREDIT
	// -----------------------------------------------------------------
	public ObjPos creditPos(OrderCreateMon orderCreateMon) {
		ObjParty party = orderCreateMon.getPosRcv().getParty();
		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();
		// ASSETS
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.CASH, orderCreateMon.getQty(), EBookingDir.CREDIT, party, EFinStmtType.BAL_SHEET));
		// EQUITY
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.EQUITY, orderCreateMon.getQty(), EBookingDir.CREDIT, party, EFinStmtType.BAL_SHEET));

		// BOOKING
		return bookingSvc.createBooking(orderCreateMon, orderCreateMon.getPosRcv(), EBookingDir.CREDIT, balTrxList,
				orderCreateMon.getQty(), orderCreateMon.getAdvText());
	}

	// -----------------------------------------------------------------
	// LOAN - CREDIT LENDER
	// -----------------------------------------------------------------
	public ObjPosLoan creditPos(OrderLoan orderLoan) {
		ObjParty partyLender = orderLoan.getMaccLender().getParty();
		double qty = orderLoan.getQty();

		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();
		// ASSETS - LENDER
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.LOANS_ASSET, qty, EBookingDir.CREDIT, partyLender, EFinStmtType.BAL_SHEET));
		// EQUITY - LENDER
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.EQUITY, qty, EBookingDir.CREDIT, partyLender, EFinStmtType.BAL_SHEET));

		// BOOKING
		return (ObjPosLoan) bookingSvc.createBooking(orderLoan, orderLoan.getPosLoanLender(), EBookingDir.CREDIT,
				balTrxList, orderLoan.getQty(), orderLoan.getAdvText());
	}

	// -----------------------------------------------------------------
	// LOAN - DEBIT BORROWER
	// -----------------------------------------------------------------
	public ObjPosLoan debitPos(OrderLoan orderLoan) {
		ObjParty partyDebtor = orderLoan.getMaccDebtor().getParty();
		double qty = orderLoan.getQty();

		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();

		// LIABILITIES - BORROWER
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.LOANS_LIAB, qty, EBookingDir.CREDIT, partyDebtor, EFinStmtType.BAL_SHEET));
		// EQUITY - BORROWER
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.EQUITY, qty, EBookingDir.DEBIT, partyDebtor, EFinStmtType.BAL_SHEET));

		// BOOKING
		return (ObjPosLoan) bookingSvc.createBooking(orderLoan, orderLoan.getPosLoanBorrower(), EBookingDir.DEBIT,
				balTrxList, orderLoan.getQty(), orderLoan.getAdvText());
	}

	// -----------------------------------------------------------------
	// STEX - CREDIT POS STEX (BUY Order)
	// -----------------------------------------------------------------
	public ObjPosStex creditPosStex(OrderStex orderBuy, ExecStex execStex) {

		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();
		// CREDIT STOCKS - BUYER
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.STOCKS, execStex.getAmtExec(), EBookingDir.CREDIT,
				orderBuy.getParty(), EFinStmtType.BAL_SHEET));

		// CREDIT EQUITY - BUYER
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.EQUITY, execStex.getAmtExec(), EBookingDir.CREDIT,
				orderBuy.getParty(), EFinStmtType.BAL_SHEET));

		// BOOKING
		return (ObjPosStex) bookingSvc.createBooking(orderBuy, execStex.getPosRcv(), EBookingDir.CREDIT, balTrxList,
				execStex.getQtyExec(), execStex.getBookText());

	}

	// -----------------------------------------------------------------
	// STEX - DEBIT POS STEX (SELL Order)
	// -----------------------------------------------------------------
	public ObjPosStex debitPosStex(OrderStex orderSell, ExecStex execStex) {
		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();
		double assetDbAmt = execStex.getQtyExec() * execStex.getPosSend().getPriceAvg();
		// DEBIT STOCKS - BUYER
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.STOCKS, assetDbAmt, EBookingDir.DEBIT, orderSell.getParty(), EFinStmtType.BAL_SHEET));

		// DEBIT EQUITY
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.EQUITY, assetDbAmt, EBookingDir.DEBIT, orderSell.getParty(), EFinStmtType.BAL_SHEET));

		// BOOKING
		return (ObjPosStex) bookingSvc.createBooking(orderSell, execStex.getPosSend(), EBookingDir.DEBIT, balTrxList,
				execStex.getQtyExec(), execStex.getBookText());

	}

	// -----------------------------------------------------------------
	// STEX - CREDIT POS MACC (SELL Order)
	// -----------------------------------------------------------------
	public ObjPosMacc creditPosMacc(OrderStex orderSell, ExecStex execStex) {
		ObjPosMacc maccSeller = partySvc.getMacc(orderSell.getParty());
		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();
		// CREDIT CASH- SELLER
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.CASH, execStex.getAmtExec(), EBookingDir.CREDIT,
				orderSell.getParty(), EFinStmtType.BAL_SHEET));

		// CREDIT EQUITY
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.EQUITY, execStex.getAmtExec(), EBookingDir.CREDIT,
				orderSell.getParty(), EFinStmtType.BAL_SHEET));

		// BOOKING
		return (ObjPosMacc) bookingSvc.createBooking(orderSell, maccSeller, EBookingDir.CREDIT, balTrxList,
				execStex.getAmtExec(), execStex.getBookText());
	}

	// -----------------------------------------------------------------
	// STEX - DEBIT POS MACC (BUY Order)
	// -----------------------------------------------------------------
	public ObjPosMacc debitPosMacc(OrderStex orderBuy, ExecStex execStex) {

		ArrayList<FinStmtTrxTrans> balTrxList = new ArrayList<FinStmtTrxTrans>();
		// CREDIT CASH- SELLER
		balTrxList.add(
				new FinStmtTrxTrans(EFinStmtItemType.CASH, execStex.getAmtExec(), EBookingDir.DEBIT, orderBuy.getParty(), EFinStmtType.BAL_SHEET));

		// DEBIT EQUITY
		balTrxList.add(new FinStmtTrxTrans(EFinStmtItemType.EQUITY, execStex.getAmtExec(), EBookingDir.DEBIT,
				orderBuy.getParty(), EFinStmtType.BAL_SHEET));

		// BOOKING
		ObjPosMacc maccBuyer = partySvc.getMacc(orderBuy.getParty());
		return (ObjPosMacc) bookingSvc.createBooking(orderBuy, maccBuyer, EBookingDir.DEBIT, balTrxList,
				execStex.getAmtExec(), execStex.getBookText());
	}

	public List<ObjPos> getByParty(ObjParty jpaParty) {
		return posRepo.findByParty(jpaParty);
	}

	public List<ObjPosLoan> getFinancingByParty(ObjParty jpaParty) {
		return posLoanRepo.findByParty(jpaParty);
	}

	public ObjPos getById(Long posId) {
		return posRepo.getOne(posId);
	}

}
