package com.brownbag_api.service;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EAsset;
import com.brownbag_api.model.enums.EAssetGrp;
import com.brownbag_api.model.jpa.ExecStex;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjPosStex;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.ExecStexRepo;

@Service
public class AssetSvc {

	@Autowired
	private AssetRepo assetRepo;

	@Autowired
	private PosSvc posSvc;

	@Autowired
	private LogSvc logSvc;

	@Autowired
	private ExecStexRepo execStexRepo;

	public ObjAsset getByEnum(EAsset eAsset) {
		return assetRepo.findByName(eAsset.getName());
	}

	public ObjAsset getByIssuer(ObjParty party) {
		return assetRepo.findByIssuer(party);
	}

	public ObjAsset save(ObjAsset asset) {
		return assetRepo.save(asset);
	}

	public ObjAsset createAssetLoan(String advText, EAssetGrp loan, @NotNull int totalShares, ObjParty partyLender,
			LocalDateTime matDate, double intrRate) {
		ObjAsset assetLoan = createAssetStex(advText, null, EAssetGrp.LOAN, totalShares, partyLender, 1, intrRate,
				matDate);
		return assetRepo.save(assetLoan);
	}

	public ObjAsset createAssetStex(String name, String isin, EAssetGrp assetGrp, @NotNull int totalShares,
			ObjParty issuer, double nomVal, @NotNull double intrRate, LocalDateTime matDate) {

		ObjAsset asset = new ObjAsset(name, isin, assetGrp, totalShares, issuer, nomVal, matDate, intrRate);
		return save(asset);
	}

	public void split(ObjAsset asset, int splitFactor) {

		if (asset.getTotalShares() > 1) {
			logSvc.write("Assets can currently only be split when total number of shares equals 1. Currently: "
					+ asset.getTotalShares() + " Asset: " + asset.getName());
			return;
		}
		asset.setTotalShares(asset.getTotalShares() * splitFactor);
		asset = assetRepo.save(asset);
		for (ObjPosStex posStex : posSvc.getByAsset(asset)) {
			posStex.setQty(posStex.getQty() * splitFactor);
			// TODO: adjust asset prices
			posSvc.save(posStex);
		}
	}

	public ObjAsset getById(Long assetId) {
		return assetRepo.getOne(assetId);
	}

	public double getLastPrice(ObjAsset asset) {
		ExecStex lastExec = execStexRepo.findFirstByAssetOrderByTimestampCreateDesc(asset);
		if (lastExec != null) {
			return lastExec.getPrice();
		} else {
			return 1;
		}

	}

	public ObjAsset createAssetBond(ObjParty party, ObjUser user, double qty, LocalDateTime matDate) {
		// TODO Auto-generated method stub
		return null;
	}
}
