package com.brownbag_api.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.AssetLoan;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.enums.EAsset;
import com.brownbag_api.model.enums.EAssetGrp;
import com.brownbag_api.repo.AssetRepo;

@Service
public class AssetSvc {

	@Autowired
	private AssetRepo assetRepo;

	public Asset getByEnum(EAsset eAsset) {
		return assetRepo.findByName(eAsset.getName());
	}
	
	public Asset getByIssuer(Party party) {
		return assetRepo.findByIssuer(party);
	}

	public Asset save(Asset asset) {
		return assetRepo.save(asset);
	}

	public AssetLoan createAssetLoan(String advText, EAssetGrp loan, Party partyLender, Date matDate, double intrRate) {
		AssetLoan assetLoan = new AssetLoan(advText, EAssetGrp.LOAN, partyLender, matDate, intrRate);
		return assetRepo.save(assetLoan);
	}

	public Asset createAssetStex(String name, String isin, EAssetGrp assetGrp, Party issuer, double nomVal) {
		Asset asset = new Asset(name, isin, assetGrp, issuer, nomVal);
		return save(asset);
	}
}
