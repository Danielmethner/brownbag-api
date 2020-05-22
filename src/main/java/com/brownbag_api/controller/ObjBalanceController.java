package com.brownbag_api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.enums.EBalSheetSectionType;
import com.brownbag_api.model.jpa.ObjBalSheet;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.json.JsonObjBalSheet;
import com.brownbag_api.model.json.JsonObjBalSheetSection;
import com.brownbag_api.repo.BalSheetRepo;
import com.brownbag_api.service.AssetSvc;
import com.brownbag_api.service.BalSheetSectionSvc;
import com.brownbag_api.service.BalSheetSvc;
import com.brownbag_api.service.CtrlVarSvc;
import com.brownbag_api.service.PartySvc;
import com.brownbag_api.util.UtilDate;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/balsheet")
public class ObjBalanceController {

	@Autowired
	private BalSheetRepo balSheetRepo;
	
	@Autowired
	private BalSheetSvc balSheetSvc;
	
	@Autowired
	private PartySvc partySvc;
	
	@Autowired
	private AssetSvc assetSvc;
	@Autowired
	private CtrlVarSvc ctrlVarSvc;
	@Autowired
	private BalSheetSectionSvc balSheetSectionSvc;

	private JsonObjBalSheet jpaToJson(ObjBalSheet jpaBalSheet) {

		JsonObjBalSheet jsonBalSheet = new JsonObjBalSheet(jpaBalSheet);
		List<JsonObjBalSheetSection> balSheetSections = new ArrayList<JsonObjBalSheetSection>();
		JsonObjBalSheetSection assets = balSheetSectionSvc.getByBalSheetAndSectionJson(jpaBalSheet,
				EBalSheetSectionType.ASSETS);
		assets.setStyle("bg-success");
		balSheetSections.add(assets);

		JsonObjBalSheetSection liablities = balSheetSectionSvc.getByBalSheetAndSectionJson(jpaBalSheet,
				EBalSheetSectionType.LIABILITIES);
		liablities.setStyle("bg-danger");
		balSheetSections.add(liablities);

		JsonObjBalSheetSection equity = balSheetSectionSvc.getByBalSheetAndSectionJson(jpaBalSheet,
				EBalSheetSectionType.EQUITY);
		equity.setStyle("bg-primary");
		balSheetSections.add(equity);

		jsonBalSheet.setSections(balSheetSections);
		return jsonBalSheet;
	}

	private List<JsonObjBalSheet> jpaToJson(List<ObjBalSheet> jpaBalSheets) {
		List<JsonObjBalSheet> jsonBalSheets = new ArrayList<JsonObjBalSheet>();
		for (ObjBalSheet jpaBalSheet : jpaBalSheets) {
			jsonBalSheets.add(jpaToJson(jpaBalSheet));
		}
		return jsonBalSheets;
	}

	@GetMapping("/all")
	public List<JsonObjBalSheet> allBalSheets() {

		List<ObjBalSheet> jpaBalSheets = balSheetRepo.findAll();
		return jpaToJson(jpaBalSheets);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		ObjBalSheet jpaBalSheet = balSheetRepo.findById(id).orElse(null);

		if (jpaBalSheet == null) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(jpaToJson(jpaBalSheet));
		}

	}
	
	@GetMapping("/party/{partyId}")
	public ResponseEntity<?> getBalSheetByPartyId(@PathVariable Long partyId) {
		ObjParty party = partySvc.getById(partyId);
		ObjBalSheet jpaBalSheet = balSheetSvc.getBalSheet(party, ctrlVarSvc.getFinYear());
		
		if (jpaBalSheet == null) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(jpaToJson(jpaBalSheet));
		}

	}


}