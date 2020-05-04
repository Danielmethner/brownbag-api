package com.brownbag_api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.enums.EBalSheetSectionType;
import com.brownbag_api.model.jpa.ObjBalSheet;
import com.brownbag_api.model.json.JsonObjBalSheet;
import com.brownbag_api.repo.BalSheetRepo;
import com.brownbag_api.service.BalSheetSectionSvc;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/balsheet")
public class BalanceController {

	@Autowired
	private BalSheetRepo balSheetRepo;

	@Autowired
	private BalSheetSectionSvc balSheetSectionSvc;

	private List<JsonObjBalSheet> jpaToJson(List<ObjBalSheet> jpaBalSheets) {
		List<JsonObjBalSheet> jsonBalSheets = new ArrayList<JsonObjBalSheet>();
		for (ObjBalSheet jpaBalSheet : jpaBalSheets) {
			JsonObjBalSheet jsonBalSheet = new JsonObjBalSheet(jpaBalSheet);
			jsonBalSheet.setSectionAssets(
					balSheetSectionSvc.getByBalSheetAndSection(jpaBalSheet, EBalSheetSectionType.ASSETS));
			jsonBalSheet.setSectionLiablities(
					balSheetSectionSvc.getByBalSheetAndSection(jpaBalSheet, EBalSheetSectionType.LIABILITIES));
			jsonBalSheet.setSectionEquity(
					balSheetSectionSvc.getByBalSheetAndSection(jpaBalSheet, EBalSheetSectionType.EQUITY));
			jsonBalSheets.add(jsonBalSheet);
		}
		return jsonBalSheets;
	}

	@GetMapping("/all")
	public List<JsonObjBalSheet> allBalSheets() {

		List<ObjBalSheet> jpaBalSheets = balSheetRepo.findAll();
		return jpaToJson(jpaBalSheets);
	}

}