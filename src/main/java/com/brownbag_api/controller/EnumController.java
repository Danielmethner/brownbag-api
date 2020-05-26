package com.brownbag_api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.enums.EAssetGrp;
import com.brownbag_api.model.enums.ELegalForm;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.json.JsonELegalForm;
import com.brownbag_api.model.json.JsonObjAsset;
import com.brownbag_api.repo.AssetRepo;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/enum")
public class EnumController {

	@Autowired
	private AssetRepo assetRepo;
	
	@GetMapping("/legalform/all")
	public ResponseEntity<?> allLegalForms() {

		List<JsonELegalForm> legalFormJsonList = new ArrayList<JsonELegalForm>();
		ELegalForm[] legalFormArray = ELegalForm.values();
		
		for (ELegalForm legalForm : legalFormArray) {
			JsonELegalForm legalFormJson = new JsonELegalForm(legalForm);
			legalFormJsonList.add(legalFormJson);
		}
		return ResponseEntity.ok(legalFormJsonList);
	}
}