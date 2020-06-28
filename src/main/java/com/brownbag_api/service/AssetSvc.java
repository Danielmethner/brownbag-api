package com.brownbag_api.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
		ObjAsset assetSaved = null;
		try {
			assetSaved = assetRepo.save(asset);
		} catch (Exception e) {
			logSvc.write(e.getMessage());
			logSvc.write(e.getCause().getMessage());
			logSvc.write(e.getCause().getCause().getMessage());			
			e.printStackTrace();
		}
		return assetSaved;
	}

	public ObjAsset createAssetLoan(String advText, EAssetGrp loan, @NotNull int totalShares, ObjParty partyLender,
			LocalDateTime matDate, double intrRate) {

		ObjAsset assetLoan = createAsset(advText, null, EAssetGrp.LOAN, totalShares, partyLender, 1, intrRate,
				matDate);
		return assetRepo.save(assetLoan);
	}

	public ObjAsset createAsset(String name, String isin, EAssetGrp assetGrp, @NotNull int totalShares,
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

	public ObjAsset createAssetBond(ObjParty party, ObjUser user, int qty, LocalDateTime matDate,
			@NotNull double nomVal, @NotNull double intrRate, @NotBlank @Size(max = 150) String name,
			@Size(max = 12) String isin) {

		System.err.println(party.getName());
		// Custom format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
		 
		// Format LocalDateTime
		String formattedMatDate = matDate.format(formatter);
		name = name == null ? party.getName() + ": " + intrRate + "% - " + formattedMatDate : name;
		isin = isin == null ? "XS" + party.getId() : isin;

		ObjAsset asset = new ObjAsset(name, isin, EAssetGrp.BOND, qty, party, nomVal, matDate, intrRate);
		return save(asset);
	}
}
