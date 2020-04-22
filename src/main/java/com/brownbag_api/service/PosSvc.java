package com.brownbag_api.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.AssetLoan;
import com.brownbag_api.model.BalTrxTransient;
import com.brownbag_api.model.ExecStex;
import com.brownbag_api.model.OrderCreateMon;
import com.brownbag_api.model.OrderLoan;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.OrderStex;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.PosLoan;
import com.brownbag_api.model.PosMacc;
import com.brownbag_api.model.PosStex;
import com.brownbag_api.model.data.EAsset;
import com.brownbag_api.model.data.EBalSheetItemType;
import com.brownbag_api.model.data.EBookingDir;
import com.brownbag_api.model.data.EParty;
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

	public Pos save(Pos pos) {
		return posRepo.save(pos);
	}

	public PosLoan createPosLoan(double qty, AssetLoan assetLoan, Party partyLender, Pos maccLender, Pos maccDebtor) {
		PosLoan posLoan = new PosLoan(qty, assetLoan, partyLender, maccLender, maccDebtor);
		return posRepo.save(posLoan);
	}

	public PosMacc createPosMacc(double qty, Asset assetCurry, Party party) {
		PosMacc posMacc = new PosMacc(qty, assetCurry, party);
		return posRepo.save(posMacc);
	}

	public PosStex createPosStex(Asset asset, Party party) {
		PosStex posStex = new PosStex(0, 0, asset, party, 0);
		return posRepo.save(posStex);
	}

	public Pos createMacc(@NotNull double initialDeposit, @NotNull Party owner, double odLimit) {
		Asset assetCash = assetRepo.findByName(EAsset.EUR.getName());
		PosMacc newMacc = createPosMacc(0, assetCash, owner);

		// ADD INITIAL DEPOSIT FROM CENTRAL BANK
		if (initialDeposit > 0) {

			Party leSend = partyRepo.findByName(EParty.ECB.toString());
			orderCreateMonSvc.createMon(leSend, initialDeposit);

			Pos maccCentralBank = partySvc.getMacc(leSend);
			OrderLoan orderLoan = orderLoanSvc.createLoan(initialDeposit, owner.getUser(), maccCentralBank, newMacc,
					null, bAdminSvc.getIntrRate());
			orderLoanSvc.grantLoan(orderLoan);
		}

		return newMacc;
	}

	public PosMacc findByParty(Party party) {
		List<PosMacc> maccList = posMaccRepo.findByParty(party);
		return maccList.isEmpty() ? null : maccList.get(0);
	}

	public PosStex getByAssetAndParty(Asset asset, Party party) {
		PosStex posStex = posStexRepo.findByAssetAndParty(asset, party);

		// CREATE NEW POS IF NOT EXISTS
		if (posStex == null) {
			posStex = createPosStex(asset, party);
		}
		return posStex;
	}

	public double getQtyAvbl(Pos pos) {
		return pos.getQty() - pos.getQtyBlocked();
	}

	// -----------------------------------------------------------------
	// PAYMENT - DEBIT
	// -----------------------------------------------------------------
	public Pos debitPos(OrderPay orderPay) {
		Party partyPayer = orderPay.getPosSend().getParty();
		ArrayList<BalTrxTransient> balTrxList = new ArrayList<BalTrxTransient>();
		// ASSETS
		balTrxList.add(new BalTrxTransient(EBalSheetItemType.CASH, orderPay.getQty(), EBookingDir.DEBIT, partyPayer));
		// LIABILITIES
		balTrxList.add(new BalTrxTransient(EBalSheetItemType.EQUITY, orderPay.getQty(), EBookingDir.DEBIT, partyPayer));

		// BOOKING
		return bookingSvc.createBooking(orderPay, orderPay.getPosSend(), EBookingDir.DEBIT, balTrxList);

	}

	// -----------------------------------------------------------------
	// PAYMENT - CREDIT
	// -----------------------------------------------------------------
	public Pos creditPos(OrderPay orderPay) {
		Party partyRecipient = orderPay.getPosRcv().getParty();
		ArrayList<BalTrxTransient> balTrxList = new ArrayList<BalTrxTransient>();
		// ASSETS
		balTrxList.add(
				new BalTrxTransient(EBalSheetItemType.CASH, orderPay.getQty(), EBookingDir.CREDIT, partyRecipient));
		// EQUITY
		balTrxList.add(
				new BalTrxTransient(EBalSheetItemType.EQUITY, orderPay.getQty(), EBookingDir.CREDIT, partyRecipient));

		// BOOKING
		return bookingSvc.createBooking(orderPay, orderPay.getPosRcv(), EBookingDir.CREDIT, balTrxList);

	}

	// -----------------------------------------------------------------
	// MONEY CREATION - CREDIT
	// -----------------------------------------------------------------
	public Pos creditPos(OrderCreateMon orderCreateMon) {
		Party party = orderCreateMon.getPosRcv().getParty();
		ArrayList<BalTrxTransient> balTrxList = new ArrayList<BalTrxTransient>();
		// ASSETS
		balTrxList.add(new BalTrxTransient(EBalSheetItemType.CASH, orderCreateMon.getQty(), EBookingDir.CREDIT, party));
		// EQUITY
		balTrxList
				.add(new BalTrxTransient(EBalSheetItemType.EQUITY, orderCreateMon.getQty(), EBookingDir.CREDIT, party));

		// BOOKING
		return bookingSvc.createBooking(orderCreateMon, orderCreateMon.getPosRcv(), EBookingDir.CREDIT, balTrxList);
	}

	// -----------------------------------------------------------------
	// LOAN - CREDIT
	// -----------------------------------------------------------------
	public PosLoan creditPos(OrderLoan orderLoan) {
		Party partyLender = orderLoan.getMaccLender().getParty();
		Party partyDebtor = orderLoan.getMaccDebtor().getParty();
		double qty = orderLoan.getQty();

		ArrayList<BalTrxTransient> balTrxList = new ArrayList<BalTrxTransient>();
		// ASSETS - LENDER
		balTrxList.add(new BalTrxTransient(EBalSheetItemType.LOANS_ASSET, qty, EBookingDir.CREDIT, partyLender));
		// EQUITY - LENDER
		balTrxList.add(new BalTrxTransient(EBalSheetItemType.EQUITY, qty, EBookingDir.CREDIT, partyLender));

		// LIABILITIES - DEBTOR
		balTrxList.add(new BalTrxTransient(EBalSheetItemType.LOANS_LIAB, qty, EBookingDir.CREDIT, partyDebtor));
		// EQUITY - DEBTOR
		balTrxList.add(new BalTrxTransient(EBalSheetItemType.EQUITY, qty, EBookingDir.DEBIT, partyDebtor));

		// BOOKING
		return (PosLoan) bookingSvc.createBooking(orderLoan, orderLoan.getPosLoan(), EBookingDir.CREDIT, balTrxList);
	}

	// -----------------------------------------------------------------
	// STEX - CREDIT POS (BUY Order)
	// -----------------------------------------------------------------
	public PosStex creditPos(OrderStex orderStex, ExecStex execStex) {

		ArrayList<BalTrxTransient> balTrxList = new ArrayList<BalTrxTransient>();
		// CREDIT STOCKS - BUYER
		balTrxList.add(new BalTrxTransient(EBalSheetItemType.STOCKS, execStex.getAmtExec(), EBookingDir.CREDIT,
				orderStex.getParty()));

		// CREDIT EQUITY - BUYER
		balTrxList.add(new BalTrxTransient(EBalSheetItemType.EQUITY, execStex.getAmtExec(), EBookingDir.CREDIT,
				orderStex.getParty()));

		return (PosStex) bookingSvc.createBooking(orderStex, execStex.getPosRcv(), EBookingDir.CREDIT, balTrxList);

	}

	public PosStex debitPos(OrderStex orderSell, ExecStex execStex) {
		ArrayList<BalTrxTransient> balTrxList = new ArrayList<BalTrxTransient>();
		// DEBIT STOCKS - BUYER
		balTrxList.add(new BalTrxTransient(EBalSheetItemType.STOCKS, execStex.getAmtExec(), EBookingDir.DEBIT,
				orderSell.getParty()));

		// DEBIT EQUITY
		balTrxList.add(new BalTrxTransient(EBalSheetItemType.EQUITY, execStex.getAmtExec(), EBookingDir.DEBIT,
				orderSell.getParty()));

		// BOOKING
		return (PosStex) bookingSvc.createBooking(orderSell, execStex.getPosRcv(), EBookingDir.CREDIT, balTrxList);

	}

}
