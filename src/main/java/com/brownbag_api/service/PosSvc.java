package com.brownbag_api.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.AssetLoan;
import com.brownbag_api.model.BalTrx;
import com.brownbag_api.model.BalTrxTransient;
import com.brownbag_api.model.OrderCreateMon;
import com.brownbag_api.model.OrderLoan;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.PosLoan;
import com.brownbag_api.model.data.EAsset;
import com.brownbag_api.model.data.EBalSheetItemType;
import com.brownbag_api.model.data.EBookingDir;
import com.brownbag_api.model.data.EParty;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.PartyRepo;
import com.brownbag_api.repo.PosRepo;

@Service
public class PosSvc {

	@Autowired
	private AssetRepo assetRepo;
	@Autowired
	private PosRepo posRepo;
	@Autowired
	private BookingSvc bookingSvc;
	@Autowired
	private OrderPaySvc orderPaySvc;
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

	public Pos createPosition(@NotNull int qty, @NotNull Party owner, @NotNull Asset asset, @NotNull double priceAvg,
			double odLimit, boolean isMacc) {
		Pos position = new Pos(priceAvg, qty, 0, odLimit, asset, owner, isMacc);
		return posRepo.save(position);
	}

	public PosLoan createPosLoan(double qty, AssetLoan assetLoan, Party partyLender, Pos maccLender, Pos maccDebtor) {
		PosLoan posLoan = new PosLoan(qty, assetLoan, partyLender, maccLender, maccDebtor);
		return posRepo.save(posLoan);
	}

	public Pos createMacc(@NotNull double initialDeposit, @NotNull Party owner, double odLimit) {
		Asset assetCash = assetRepo.findByName(EAsset.EUR.getName());
		Pos newMacc = createPosition(0, owner, assetCash, 1, odLimit, true);

		// ADD INITIAL DEPOSIT FROM CENTRAL BANK
		if (initialDeposit > 0) {

			Party leSend = partyRepo.findByName(EParty.ECB.toString());
			// TODO: implement book_text
			orderCreateMonSvc.createMon(leSend, initialDeposit);

			Pos maccCentralBank = partySvc.getMacc(leSend);
			OrderLoan orderLoan = orderLoanSvc.createLoan(initialDeposit, owner.getUser(), maccCentralBank, newMacc,
					null, bAdminSvc.getIntrRate());
			orderLoanSvc.grantLoan(orderLoan);
		}

		return newMacc;
	}

	public Pos findByPartyAndIsMacc(Party party, boolean isMacc) {
		List<Pos> maccList = posRepo.findByPartyAndIsMacc(party, isMacc);
		return maccList.isEmpty() ? null : maccList.get(0);
	}

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

	public Pos crebitPos(OrderPay orderPay) {
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

	public Pos crebitPos(OrderCreateMon orderCreateMon) {
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

	public PosLoan crebitPos(OrderLoan orderLoan) {
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

}
