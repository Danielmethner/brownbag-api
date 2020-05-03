package com.brownbag_api.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EAsset;
import com.brownbag_api.model.enums.EBalSheetItemType;
import com.brownbag_api.model.enums.EBookingDir;
import com.brownbag_api.model.enums.EOrderType;
import com.brownbag_api.model.enums.EParty;
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
import com.brownbag_api.model.trans.BalTrxTrans;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.PartyRepo;
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
	private BookingSvc bookingSvc;
	@Autowired
	private BAdminSvc bAdminSvc;
	@Autowired
	private OrderLoanSvc orderLoanSvc;
	@Autowired
	private PartyRepo partyRepo;
	@Autowired
	private PartySvc partySvc;
	@Autowired
	private OrderCreateMonSvc orderCreateMonSvc;

	public ObjPos save(ObjPos pos) {
		return posRepo.save(pos);
	}

	public ObjPosLoan createPosLoan(double qty, ObjAssetLoan assetLoan, ObjParty partyLender, ObjPos maccLender, ObjPos maccDebtor) {
		ObjPosLoan posLoan = new ObjPosLoan(qty, assetLoan, partyLender, maccLender, maccDebtor);
		return posRepo.save(posLoan);
	}

	public ObjPosMacc createPosMacc(double qty, ObjAsset assetCurry, ObjParty party) {
		ObjPosMacc posMacc = new ObjPosMacc(qty, assetCurry, party);
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

	public ObjPos createMacc(@NotNull double initialDeposit, @NotNull ObjParty owner, double odLimit) {
		ObjAsset assetCash = assetRepo.findByName(EAsset.EUR.getName());
		ObjPosMacc newMacc = createPosMacc(0, assetCash, owner);

		// ADD INITIAL DEPOSIT FROM CENTRAL BANK
		if (initialDeposit > 0) {

			ObjParty leSend = partyRepo.findByName(EParty.ECB.toString());
			orderCreateMonSvc.createMon(leSend, initialDeposit);

			ObjPos maccCentralBank = partySvc.getMacc(leSend);
			OrderLoan orderLoan = orderLoanSvc.createLoan(initialDeposit, owner.getUser(), maccCentralBank, newMacc,
					null, bAdminSvc.getIntrRate());
			orderLoanSvc.grantLoan(orderLoan);
		}

		return newMacc;
	}

	public ObjPosMacc findByParty(ObjParty party) {
		List<ObjPosMacc> maccList = posMaccRepo.findByParty(party);
		return maccList.isEmpty() ? null : maccList.get(0);
	}

	public List<ObjPosStex> getByAsset(ObjAsset asset) {
		return posStexRepo.findByAsset(asset);
	}

	public ObjPosStex getByAssetAndParty(ObjAsset asset, ObjParty party) {
		ObjPosStex posStex = posStexRepo.findByAssetAndParty(asset, party);

		// CREATE NEW POS IF NOT EXISTS
		if (posStex == null) {
			posStex = createPosStex(asset, party);
		}
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
		ArrayList<BalTrxTrans> balTrxList = new ArrayList<BalTrxTrans>();
		// ASSETS
		balTrxList.add(new BalTrxTrans(EBalSheetItemType.CASH, orderPay.getQty(), EBookingDir.DEBIT, partyPayer));
		// LIABILITIES
		balTrxList.add(new BalTrxTrans(EBalSheetItemType.EQUITY, orderPay.getQty(), EBookingDir.DEBIT, partyPayer));

		// BOOKING
		return bookingSvc.createBooking(orderPay, orderPay.getPosSend(), EBookingDir.DEBIT, balTrxList);

	}

	// -----------------------------------------------------------------
	// PAYMENT - CREDIT
	// -----------------------------------------------------------------
	public ObjPos creditPos(OrderPay orderPay) {
		ObjParty partyRecipient = orderPay.getPosRcv().getParty();
		ArrayList<BalTrxTrans> balTrxList = new ArrayList<BalTrxTrans>();
		// ASSETS
		balTrxList.add(new BalTrxTrans(EBalSheetItemType.CASH, orderPay.getQty(), EBookingDir.CREDIT, partyRecipient));
		// EQUITY
		balTrxList
				.add(new BalTrxTrans(EBalSheetItemType.EQUITY, orderPay.getQty(), EBookingDir.CREDIT, partyRecipient));

		// BOOKING
		return bookingSvc.createBooking(orderPay, orderPay.getPosRcv(), EBookingDir.CREDIT, balTrxList);

	}

	// -----------------------------------------------------------------
	// MONEY CREATION - CREDIT
	// -----------------------------------------------------------------
	public ObjPos creditPos(OrderCreateMon orderCreateMon) {
		ObjParty party = orderCreateMon.getPosRcv().getParty();
		ArrayList<BalTrxTrans> balTrxList = new ArrayList<BalTrxTrans>();
		// ASSETS
		balTrxList.add(new BalTrxTrans(EBalSheetItemType.CASH, orderCreateMon.getQty(), EBookingDir.CREDIT, party));
		// EQUITY
		balTrxList.add(new BalTrxTrans(EBalSheetItemType.EQUITY, orderCreateMon.getQty(), EBookingDir.CREDIT, party));

		// BOOKING
		return bookingSvc.createBooking(orderCreateMon, orderCreateMon.getPosRcv(), EBookingDir.CREDIT, balTrxList);
	}

	// -----------------------------------------------------------------
	// LOAN - CREDIT
	// -----------------------------------------------------------------
	public ObjPosLoan creditPos(OrderLoan orderLoan) {
		ObjParty partyLender = orderLoan.getMaccLender().getParty();
		ObjParty partyDebtor = orderLoan.getMaccDebtor().getParty();
		double qty = orderLoan.getQty();

		ArrayList<BalTrxTrans> balTrxList = new ArrayList<BalTrxTrans>();
		// ASSETS - LENDER
		balTrxList.add(new BalTrxTrans(EBalSheetItemType.LOANS_ASSET, qty, EBookingDir.CREDIT, partyLender));
		// EQUITY - LENDER
		balTrxList.add(new BalTrxTrans(EBalSheetItemType.EQUITY, qty, EBookingDir.CREDIT, partyLender));

		// LIABILITIES - DEBTOR
		balTrxList.add(new BalTrxTrans(EBalSheetItemType.LOANS_LIAB, qty, EBookingDir.CREDIT, partyDebtor));
		// EQUITY - DEBTOR
		balTrxList.add(new BalTrxTrans(EBalSheetItemType.EQUITY, qty, EBookingDir.DEBIT, partyDebtor));

		// BOOKING
		return (ObjPosLoan) bookingSvc.createBooking(orderLoan, orderLoan.getPosLoan(), EBookingDir.CREDIT, balTrxList);
	}

	// -----------------------------------------------------------------
	// STEX - CREDIT POS (BUY Order)
	// -----------------------------------------------------------------
	public ObjPosStex creditPos(OrderStex orderStex, ExecStex execStex) {

		ArrayList<BalTrxTrans> balTrxList = new ArrayList<BalTrxTrans>();
		// CREDIT STOCKS - BUYER
		balTrxList.add(new BalTrxTrans(EBalSheetItemType.STOCKS, execStex.getAmtExec(), EBookingDir.CREDIT,
				orderStex.getParty()));

		// CREDIT EQUITY - BUYER
		balTrxList.add(new BalTrxTrans(EBalSheetItemType.EQUITY, execStex.getAmtExec(), EBookingDir.CREDIT,
				orderStex.getParty()));

		// BOOKING
		return (ObjPosStex) bookingSvc.createBooking(orderStex, execStex.getPosRcv(), EBookingDir.CREDIT, balTrxList);

	}

	public ObjPosStex debitPos(OrderStex orderSell, ExecStex execStex) {
		ArrayList<BalTrxTrans> balTrxList = new ArrayList<BalTrxTrans>();
		// DEBIT STOCKS - BUYER
		if (orderSell.getOrderType() != EOrderType.STEX_IPO) {
			balTrxList.add(new BalTrxTrans(EBalSheetItemType.STOCKS, execStex.getAmtExec(), EBookingDir.DEBIT,
					orderSell.getParty()));
		}

		// DEBIT EQUITY
		balTrxList.add(new BalTrxTrans(EBalSheetItemType.EQUITY, execStex.getAmtExec(), EBookingDir.DEBIT,
				orderSell.getParty()));

		// BOOKING
		return (ObjPosStex) bookingSvc.createBooking(orderSell, execStex.getPosSend(), EBookingDir.CREDIT, balTrxList);

	}

}
