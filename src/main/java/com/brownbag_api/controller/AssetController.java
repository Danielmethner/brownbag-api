package com.brownbag_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.enums.EAssetGrp;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.repo.AssetRepo;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/asset")
public class AssetController {

	@Autowired
	private AssetRepo assetRepo;

	@GetMapping("/sec/all")
	public List<ObjAsset> allStocks() {
		return assetRepo.findAllByAssetGrp(EAssetGrp.STOCK);
	}

	@GetMapping("/macc/all")
	public List<ObjAsset> allMaccs() {
		return assetRepo.findAllByAssetGrp(EAssetGrp.CURRY);
	}

}