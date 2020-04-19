package com.brownbag_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.data.EAsset;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.util.UtilBA;

@Service
public class BAdminSvc {

	@Autowired
	private AssetRepo assetRepo;

	public Asset getByEnum(EAsset eAsset) {
		return assetRepo.findByName(eAsset.getName());	
	}

	public Asset save(Asset asset) {
		return assetRepo.save(asset);
	}

	public double getIntrRate() {
		return UtilBA.getIntrRate();
	}
}
