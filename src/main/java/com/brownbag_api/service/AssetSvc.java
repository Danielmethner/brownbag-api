package com.brownbag_api.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EAsset;
import com.brownbag_api.model.enums.EAssetGrp;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjAssetLoan;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjPosStex;
import com.brownbag_api.repo.AssetRepo;

@Service
public class AssetSvc {

	@Autowired
	private AssetRepo assetRepo;

	@Autowired
	private PosSvc posSvc;

	@Autowired
	private LogSvc logSvc;

	public ObjAsset getByEnum(EAsset eAsset) {
		return assetRepo.findByName(eAsset.getName());
	}

	public ObjAsset getByIssuer(ObjParty party) {
		return assetRepo.findByIssuer(party);
	}

	public ObjAsset save(ObjAsset asset) {
		return assetRepo.save(asset);
	}

	public ObjAssetLoan createAssetLoan(String advText, EAssetGrp loan, ObjParty partyLender, Date matDate,
			double intrRate) {
		ObjAssetLoan assetLoan = new ObjAssetLoan(advText, EAssetGrp.LOAN, partyLender, matDate, intrRate);
		return assetRepo.save(assetLoan);
	}

	public ObjAsset createAssetStex(String name, String isin, EAssetGrp assetGrp, ObjParty issuer, double nomVal) {
		ObjAsset asset = new ObjAsset(name, isin, assetGrp, issuer, nomVal);
		return save(asset);
	}

	public void split(ObjAsset asset, int splitFactor) {

		if (asset.getTotalShares() > 1) {
			logSvc.write("Assets can currently only be split when total number of shares equals 1");
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
}
