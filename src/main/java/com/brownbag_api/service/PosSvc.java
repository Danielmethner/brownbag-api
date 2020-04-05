package com.brownbag_api.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.LegalEntity;
import com.brownbag_api.model.Position;
import com.brownbag_api.model.User;
import com.brownbag_api.model.data.EAsset;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.PosRepo;

@Service
public class PosSvc {

	@Autowired
	private AssetRepo assetRepo;
	@Autowired
	private PosRepo posRepo;

	public void createPosition(@NotNull int qty, @NotNull LegalEntity owner, @NotNull Asset asset,
			@NotNull double priceAvg, double odLimit, boolean isMacc) {
		Position position = new Position(priceAvg, qty, 0, odLimit, asset, owner, isMacc);
		posRepo.save(position);
	}

	public void createMacc(@NotNull int qty, @NotNull LegalEntity owner, double odLimit) {
		Asset assetCash = assetRepo.findByName(EAsset.CASH.getName());
		createPosition(qty, owner, assetCash, 1, odLimit, true);
	}

	public Position findByOwnerAndIsMacc(LegalEntity user, boolean isMacc) {
		List<Position> maccList = posRepo.findByOwnerAndIsMacc(user, isMacc);

		return maccList.isEmpty() ? null : maccList.get(0);
	}

}
