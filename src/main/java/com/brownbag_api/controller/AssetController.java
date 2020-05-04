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
import com.brownbag_api.model.json.JsonObjAsset;
import com.brownbag_api.repo.AssetRepo;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/asset")
public class AssetController {

	@Autowired
	private AssetRepo assetRepo;
	
	private List<JsonObjAsset> jpaToJson(List<ObjAsset> jpaAssets) {
		List<JsonObjAsset> jsonAssets = new ArrayList<JsonObjAsset>();
		for (ObjAsset jpaAsset : jpaAssets) {
			JsonObjAsset jsonAsset = new JsonObjAsset(jpaAsset);
			jsonAssets.add(jsonAsset);
		}
		return jsonAssets;
	}

	@GetMapping("/stock/all")
	public List<JsonObjAsset> allStocks() {
		
		List<ObjAsset> jpaAssets = assetRepo.findAllByAssetGrp(EAssetGrp.STOCK);
		return jpaToJson(jpaAssets);
	}

	@GetMapping("/curry/all")
	public List<JsonObjAsset> allMaccs() {
		List<ObjAsset> jpaAssets = assetRepo.findAllByAssetGrp(EAssetGrp.CURRY);
		return jpaToJson(jpaAssets);
	}

}