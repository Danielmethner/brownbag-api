package com.brownbag_api.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.Booking;
import com.brownbag_api.model.Order;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.data.EAsset;
import com.brownbag_api.model.data.EBalSheetItemType;
import com.brownbag_api.model.data.EBookType;
import com.brownbag_api.model.data.EBookingDir;
import com.brownbag_api.model.data.EParty;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.BookingRepo;
import com.brownbag_api.repo.PartyRepo;
import com.brownbag_api.repo.PosRepo;

@Service
public class PosSvc {

	@Autowired
	private AssetRepo assetRepo;
	@Autowired
	private PosRepo posRepo;
	@Autowired
	private BookingRepo bookingRepo;

	@Autowired
	private BookingSvc bookingSvc;
	@Autowired
	private OrderPaySvc orderPaySvc;
	@Autowired
	private PartyRepo partyRepo;
	@Autowired
	private PartySvc lESvc;

	public Pos createPosition(@NotNull int qty, @NotNull Party owner, @NotNull Asset asset,
			@NotNull double priceAvg, double odLimit, boolean isMacc) {
		Pos position = new Pos(priceAvg, qty, 0, odLimit, asset, owner, isMacc);
		return posRepo.save(position);
	}

	public Pos createMacc(@NotNull int initialDeposit, @NotNull Party owner, double odLimit) {
		Asset assetCash = assetRepo.findByName(EAsset.EUR.getName());
		Pos newMacc = createPosition(0, owner, assetCash, 1, odLimit, true);

		// ADD INITIAL DEPOSIT FROM CENTRAL BANK
		if (initialDeposit > 0) {

			Party leSend = partyRepo.findByName(EParty.ECB.toString());
			Pos maccCentralBank = lESvc.getMacc(leSend);
			String bookText = "Initial Deposit from Central Bank for Entity: " + owner.getName();
			OrderPay orderPay = orderPaySvc.createPay(initialDeposit, owner.getUser(), null, bookText, maccCentralBank,
					newMacc, EBookType.REVENUE);
			orderPaySvc.execPay(orderPay);
		}

		return newMacc;
	}

	public Pos findByPartyAndIsMacc(Party party, boolean isMacc) {
		List<Pos> maccList = posRepo.findByPartyAndIsMacc(party, isMacc);
		return maccList.isEmpty() ? null : maccList.get(0);
	}

	public Pos debitPos(OrderPay orderPay) {
		return bookingSvc.createBooking(orderPay, orderPay.getPosSend(), EBookingDir.DEBIT, EBalSheetItemType.CASH, EBalSheetItemType.EQUITY);
		
	}

	public Pos crebitPos(OrderPay orderPay) {
		return bookingSvc.createBooking(orderPay, orderPay.getPosRcv(), EBookingDir.CREDIT, EBalSheetItemType.CASH, EBalSheetItemType.EQUITY);
	}

}
