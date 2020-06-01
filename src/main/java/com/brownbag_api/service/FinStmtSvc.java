package com.brownbag_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EFinStmtSectionType;
import com.brownbag_api.model.enums.EFinStmtType;
import com.brownbag_api.model.jpa.ObjFinStmt;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.repo.FinStmtRepo;

@Service
public class FinStmtSvc {

	@Autowired
	private FinStmtRepo finStmtRepo;

	@Autowired
	private FinStmtSectionSvc finStmtSectionSvc;

	public ObjFinStmt createFinStmt(ObjParty party, int finYear, EFinStmtType finStmtType) {
		ObjFinStmt balSheet = new ObjFinStmt(party, finYear, finStmtType);
		balSheet = finStmtRepo.save(balSheet);
		
		finStmtSectionSvc.createFinStmtSection(balSheet, EFinStmtSectionType.ASSETS, finStmtType);
		finStmtSectionSvc.createFinStmtSection(balSheet, EFinStmtSectionType.LIABILITIES, finStmtType);
		finStmtSectionSvc.createFinStmtSection(balSheet, EFinStmtSectionType.EQUITY, finStmtType);
		return balSheet;
	}

	public ObjFinStmt getFinStmt(ObjParty party, int finYear, EFinStmtType finStmtType) {
		ObjFinStmt balSheet = finStmtRepo.findByPartyAndFinYearAndFinStmtType(party, finYear, EFinStmtType.BAL_SHEET);
		if (balSheet == null && party.getFoundingDate().getYear() <= finYear) {
			balSheet = createFinStmt(party, finYear, finStmtType);
		}
		return balSheet;
	}
}
