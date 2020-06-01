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

import com.brownbag_api.model.enums.EFinStmtSectionType;
import com.brownbag_api.model.enums.EFinStmtType;
import com.brownbag_api.model.jpa.ObjFinStmt;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.json.JsonObjFinStmt;
import com.brownbag_api.model.json.JsonObjFinStmtSection;
import com.brownbag_api.repo.FinStmtRepo;
import com.brownbag_api.service.AssetSvc;
import com.brownbag_api.service.ControlSvc;
import com.brownbag_api.service.FinStmtSectionSvc;
import com.brownbag_api.service.FinStmtSvc;
import com.brownbag_api.service.PartySvc;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/fin-stmt")
public class ObjFinStmtController {

	@Autowired
	private FinStmtRepo finStmtRepo;
	
	@Autowired
	private FinStmtSvc finStmtSvc;
	
	@Autowired
	private PartySvc partySvc;
	
	@Autowired
	private AssetSvc assetSvc;
	@Autowired
	private ControlSvc controlSvc;
	@Autowired
	private FinStmtSectionSvc balSheetSectionSvc;

	private JsonObjFinStmt jpaToJson(ObjFinStmt jpaBalSheet) {

		JsonObjFinStmt jsonBalSheet = new JsonObjFinStmt(jpaBalSheet);
		List<JsonObjFinStmtSection> balSheetSections = new ArrayList<JsonObjFinStmtSection>();
		JsonObjFinStmtSection assets = balSheetSectionSvc.getByBalSheetAndSectionJson(jpaBalSheet,
				EFinStmtSectionType.ASSETS);
		assets.setStyle(EFinStmtSectionType.ASSETS.getStyle());
		balSheetSections.add(assets);

		JsonObjFinStmtSection liablities = balSheetSectionSvc.getByBalSheetAndSectionJson(jpaBalSheet,
				EFinStmtSectionType.LIABILITIES);
		liablities.setStyle(EFinStmtSectionType.LIABILITIES.getStyle());
		balSheetSections.add(liablities);

		JsonObjFinStmtSection equity = balSheetSectionSvc.getByBalSheetAndSectionJson(jpaBalSheet,
				EFinStmtSectionType.EQUITY);
		equity.setStyle(EFinStmtSectionType.EQUITY.getStyle());
		balSheetSections.add(equity);

		jsonBalSheet.setSections(balSheetSections);
		return jsonBalSheet;
	}

	private List<JsonObjFinStmt> jpaToJson(List<ObjFinStmt> jpaBalSheets) {
		List<JsonObjFinStmt> jsonBalSheets = new ArrayList<JsonObjFinStmt>();
		for (ObjFinStmt jpaBalSheet : jpaBalSheets) {
			jsonBalSheets.add(jpaToJson(jpaBalSheet));
		}
		return jsonBalSheets;
	}

	@GetMapping("/balsheet/all")
	public List<JsonObjFinStmt> allBalSheets() {

		List<ObjFinStmt> jpaBalSheets = finStmtRepo.findAll();
		return jpaToJson(jpaBalSheets);
	}

	@GetMapping("/balsheet/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		ObjFinStmt jpaBalSheet = finStmtRepo.findById(id).orElse(null);

		if (jpaBalSheet == null) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(jpaToJson(jpaBalSheet));
		}

	}
	
	@GetMapping("/type/balsheet/finyear/{finYear}/party/{partyId}")
	public ResponseEntity<?> getBalSheetByPartyId(@PathVariable int finYear, @PathVariable Long partyId) {
		ObjParty party = partySvc.getById(partyId);
		EFinStmtType finStmtType = EFinStmtType.BAL_SHEET;
		ObjFinStmt jpaBalSheet = finStmtSvc.getFinStmt(party, finYear, finStmtType);
		
		if (jpaBalSheet == null) {
			return ResponseEntity.ok("Could not find Balance Sheet for year: '" + finYear + "' and Party ID: ' "+ partyId + "'");
		} else {
			return ResponseEntity.ok(jpaToJson(jpaBalSheet));
		}
	}

	@GetMapping("/type/incomestmt/finyear/{finYear}/party/{partyId}")
	public ResponseEntity<?> getIncomeStmtByPartyId(@PathVariable int finYear, @PathVariable Long partyId) {
		ObjParty party = partySvc.getById(partyId);
		EFinStmtType finStmtType = EFinStmtType.INCOME_STMT;
		ObjFinStmt jpaBalSheet = finStmtSvc.getFinStmt(party, finYear, finStmtType);
		
		if (jpaBalSheet == null) {
			return ResponseEntity.ok("Could not find Income Statement for year: '" + finYear + "' and Party ID: ' "+ partyId + "'");
		} else {
			return ResponseEntity.ok(jpaToJson(jpaBalSheet));
		}
	}
}