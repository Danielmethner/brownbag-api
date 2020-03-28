package com.brownbag_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.Asset;
import com.brownbag_api.repo.AssetRepo;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/asset")
public class AssetController {

	@Autowired
	private AssetRepo assetRepo;

	@GetMapping("/sec/all")
	public List<Asset> allSecurities() {
		return assetRepo.findAllByIsMacc(false);
	}
	
	@GetMapping("/macc/all")
	public List<Asset> allMaccs() {
		return assetRepo.findAllByIsMacc(true);
	}

}