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
	

	@Autowired
	private ControlSvc ctrlVarSvc;

	public ObjFinStmt createFinStmt(ObjParty party, int finYear, EFinStmtType finStmtType) {
		ObjFinStmt finStmt = new ObjFinStmt(party, finYear, finStmtType);
		finStmt = finStmtRepo.save(finStmt);

		for (EFinStmtSectionType sectionType : EFinStmtSectionType.values()) {
			if (sectionType.getFinStmtType() == finStmtType) {

				finStmtSectionSvc.createFinStmtSection(finStmt, sectionType, finStmtType);
			}
		}
		return finStmt;
	}

	public ObjFinStmt getFinStmt(ObjParty party, int finYear, EFinStmtType finStmtType) {
		ObjFinStmt balSheet = finStmtRepo.findByPartyAndFinYearAndFinStmtType(party, finYear, finStmtType);
		if (balSheet == null && party.getFoundingDate().getYear() <= finYear) {
			balSheet = createFinStmt(party, finYear, finStmtType);
		}
		return balSheet;
	}
	
	public ObjFinStmt getFinStmtCurrent(ObjParty party, EFinStmtType finStmtType) {
		int finYear = ctrlVarSvc.getFinYear();
		ObjFinStmt balSheet = finStmtRepo.findByPartyAndFinYearAndFinStmtType(party, finYear, finStmtType);
		if (balSheet == null && party.getFoundingDate().getYear() <= finYear) {
			balSheet = createFinStmt(party, finYear, finStmtType);
		}
		return balSheet;
	}
}
