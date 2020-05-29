package com.brownbag_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EFinStmtSectionType;
import com.brownbag_api.model.jpa.ObjFinStmt;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.repo.FinStmtRepo;

@Service
public class FinStmtSvc {

	@Autowired
	private FinStmtRepo balSheetRepo;

	@Autowired
	private FinStmtSectionSvc balSheetSectionSvc;

	public ObjFinStmt createBalSheet(ObjParty party, int finYear) {
		ObjFinStmt balSheet = new ObjFinStmt(party, finYear);
		balSheet = balSheetRepo.save(balSheet);
		balSheetSectionSvc.createBalSheetSection(balSheet, EFinStmtSectionType.ASSETS);
		balSheetSectionSvc.createBalSheetSection(balSheet, EFinStmtSectionType.LIABILITIES);
		balSheetSectionSvc.createBalSheetSection(balSheet, EFinStmtSectionType.EQUITY);
		return balSheet;
	}

	public ObjFinStmt getBalSheet(ObjParty party, int finYear) {
		ObjFinStmt balSheet = balSheetRepo.findByPartyAndFinYear(party, finYear);
		if (balSheet == null && party.getFoundingDate().getYear() <= finYear) {
			balSheet = createBalSheet(party, finYear);
		}
		return balSheet;
	}
}
