package com.brownbag_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.Asset;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.util.UtilDate;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/settings/")
public class SettingsController {

	@Autowired
	private AssetRepo assetRepo;

	@GetMapping("/finyear")
	public int getFinYear() {
		return UtilDate.finYear;
	}
	
	@GetMapping("/finyear/increment")
	public int incrementFinYear() {
		return UtilDate.finYear += 1;
	}
	
	@GetMapping("/finyear/set/{year}")
	public int setFinYear(@PathVariable int year) {
		return UtilDate.finYear = year;
	}


}