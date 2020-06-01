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
	FinStmtRepo finStmtRepo;

	@Autowired
	FinStmtSvc finStmtSvc;

	@Autowired
	PartySvc partySvc;

	@Autowired
	AssetSvc assetSvc;
	@Autowired
	ControlSvc controlSvc;
	@Autowired
	FinStmtSectionSvc balSheetSectionSvc;

	private JsonObjFinStmt jpaToJson(ObjFinStmt jpaFinStmt) {

		JsonObjFinStmt jsonBalSheet = new JsonObjFinStmt(jpaFinStmt);
		List<JsonObjFinStmtSection> finStmtSections = new ArrayList<JsonObjFinStmtSection>();

		// GET SECTIONS
		EFinStmtSectionType.stream().filter(eSection -> eSection.getFinStmtType().equals(jpaFinStmt.getFinStmtType()))
				.forEach(eSection -> {
					JsonObjFinStmtSection jsonSection = balSheetSectionSvc.getByBalSheetAndSectionJson(jpaFinStmt,
							eSection);
					if (jsonSection == null) {
						return;
					}
					jsonSection.setStyle(eSection.getStyle());
					finStmtSections.add(jsonSection);
				});
		jsonBalSheet.setSections(finStmtSections);
		return jsonBalSheet;
	}

	private List<JsonObjFinStmt> jpaToJson(List<ObjFinStmt> jpaFinStmtList) {
		List<JsonObjFinStmt> jsonFinStmtList = new ArrayList<JsonObjFinStmt>();
		for (ObjFinStmt jpaFinStmt : jpaFinStmtList) {
			jsonFinStmtList.add(jpaToJson(jpaFinStmt));
		}
		return jsonFinStmtList;
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

	@GetMapping("/type/{finStmtType}/finyear/{finYear}/party/{partyId}")
	public ResponseEntity<?> getIncomeStmtByPartyId(@PathVariable EFinStmtType finStmtType, @PathVariable int finYear,
			@PathVariable Long partyId) {
		ObjParty party = partySvc.getById(partyId);
		ObjFinStmt jpaBalSheet = finStmtSvc.getFinStmt(party, finYear, finStmtType);

		if (jpaBalSheet == null) {
			return ResponseEntity
					.ok("Could not find Income Statement for year: '" + finYear + "' and Party ID: ' " + partyId + "'");
		} else {
			return ResponseEntity.ok(jpaToJson(jpaBalSheet));
		}
	}
}