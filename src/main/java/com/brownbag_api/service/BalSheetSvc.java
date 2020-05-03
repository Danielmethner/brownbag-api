package com.brownbag_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EBalSheetSectionType;
import com.brownbag_api.model.jpa.ObjBalSheet;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.repo.BalSheetRepo;

@Service
public class BalSheetSvc {

	@Autowired
	private BalSheetRepo balSheetRepo;

	@Autowired
	private BalSheetSectionSvc balSheetSectionSvc;

	public ObjBalSheet createBalSheet(ObjParty party, int finYear) {
		ObjBalSheet balSheet = new ObjBalSheet(party, finYear);
		balSheet = balSheetRepo.save(balSheet);
		balSheetSectionSvc.createBalSheetSection(balSheet, EBalSheetSectionType.ASSETS);
		balSheetSectionSvc.createBalSheetSection(balSheet, EBalSheetSectionType.LIABILITIES);
		balSheetSectionSvc.createBalSheetSection(balSheet, EBalSheetSectionType.EQUITY);
		return balSheet;
	}

	public ObjBalSheet getBalSheet(ObjParty party, int finYear) {
		ObjBalSheet balSheet = balSheetRepo.findByPartyAndFinYear(party, finYear);
		if (balSheet == null) {
			balSheet = createBalSheet(party, finYear);
		}
		return balSheet;
	}
}
