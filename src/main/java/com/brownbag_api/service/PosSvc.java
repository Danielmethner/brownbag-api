package com.brownbag_api.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.data.EAsset;
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
		Asset assetCash = assetRepo.findByName(EAsset.CASH.getName());
		Pos newMacc = createPosition(0, owner, assetCash, 1, odLimit, true);

		// ADD INITIAL DEPOSIT FROM CENTRAL BANK
		if (initialDeposit > 0) {

			Party leSend = partyRepo.findByName(EParty.CENTRAL_BANK.toString());
			Pos maccCentralBank = lESvc.getMacc(leSend);
			String bookText = "Initial Deposit from Central Bank for Entity: " + owner.getName();
			OrderPay orderPay = orderPaySvc.createPay(initialDeposit, owner.getUser(), null, bookText, maccCentralBank,
					newMacc);
			orderPaySvc.execPay(orderPay);
		}

		return newMacc;
	}

	public Pos findByPartyAndIsMacc(Party party, boolean isMacc) {
		List<Pos> maccList = posRepo.findByPartyAndIsMacc(party, isMacc);
		return maccList.isEmpty() ? null : maccList.get(0);
	}

	public Pos debitPos(Pos pos, double qty) {
		pos.setQty(pos.getQty() - qty);
		return posRepo.save(pos);
	}

	public Pos crebitPos(Pos pos, double qty) {
		pos.setQty(pos.getQty() + qty);
		return posRepo.save(pos);
	}

}
