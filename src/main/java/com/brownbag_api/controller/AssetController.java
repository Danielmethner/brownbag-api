package com.brownbag_api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.enums.EAssetGrp;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.json.JsonAsset;
import com.brownbag_api.repo.AssetRepo;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/asset")
public class AssetController {

	@Autowired
	private AssetRepo assetRepo;
	
	private List<JsonAsset> jpaToJson(List<ObjAsset> jpaAssets) {
		List<JsonAsset> jsonAssets = new ArrayList<JsonAsset>();
		for (ObjAsset jpaAsset : jpaAssets) {
			JsonAsset jsonAsset = new JsonAsset(jpaAsset);
			jsonAssets.add(jsonAsset);
		}
		return jsonAssets;
	}

	@GetMapping("/stock/all")
	public List<JsonAsset> allStocks() {
		
		List<ObjAsset> jpaAssets = assetRepo.findAllByAssetGrp(EAssetGrp.STOCK);
		return jpaToJson(jpaAssets);
	}

	@GetMapping("/curry/all")
	public List<JsonAsset> allMaccs() {
		List<ObjAsset> jpaAssets = assetRepo.findAllByAssetGrp(EAssetGrp.CURRY);
		return jpaToJson(jpaAssets);
	}

}