package com.brownbag_api.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.LegalEntity;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.Position;
import com.brownbag_api.model.User;
import com.brownbag_api.model.data.EAsset;
import com.brownbag_api.model.data.ELE;
import com.brownbag_api.model.data.ELEType;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.LERepo;
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
	private LERepo lERepo;
	@Autowired
	private LESvc lESvc;

	public Position createPosition(@NotNull int qty, @NotNull LegalEntity owner, @NotNull Asset asset,
			@NotNull double priceAvg, double odLimit, boolean isMacc) {
		Position position = new Position(priceAvg, qty, 0, odLimit, asset, owner, isMacc);
		return posRepo.save(position);
	}

	public Position createMacc(@NotNull int initialDeposit, @NotNull LegalEntity owner, double odLimit) {
		Asset assetCash = assetRepo.findByName(EAsset.CASH.getName());
		Position newMacc = createPosition(0, owner, assetCash, 1, odLimit, true);

		// ADD INITIAL DEPOSIT FROM CENTRAL BANK
		if (initialDeposit > 0) {

			LegalEntity leSend = lERepo.findByName(ELE.CENTRAL_BANK.toString());
			Position maccCentralBank = lESvc.getMacc(leSend);
			String bookText = "Initial Deposit from Central Bank for Entity: " + owner.getName();
			OrderPay orderPay = orderPaySvc.createPay(initialDeposit, owner.getUser(), null, bookText, maccCentralBank,
					newMacc);
			orderPaySvc.execPay(orderPay);
		}

		return newMacc;
	}

	public Position findByOwnerAndIsMacc(LegalEntity user, boolean isMacc) {
		List<Position> maccList = posRepo.findByOwnerAndIsMacc(user, isMacc);
		return maccList.isEmpty() ? null : maccList.get(0);
	}

	public Position debitPos(Position pos, double qty) {
		pos.setQty(pos.getQty() - qty);
		return posRepo.save(pos);
	}

	public Position crebitPos(Position pos, double qty) {
		pos.setQty(pos.getQty() + qty);
		return posRepo.save(pos);
	}

}
