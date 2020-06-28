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

	private ObjFinStmt createFinStmt(ObjParty party, int finYear, EFinStmtType finStmtType) {
		ObjFinStmt finStmt = new ObjFinStmt(party, finYear, finStmtType);
		finStmt = finStmtRepo.save(finStmt);

		for (EFinStmtSectionType sectionType : EFinStmtSectionType.values()) {
			if (sectionType.getFinStmtType() == finStmtType) {

				finStmtSectionSvc.createFinStmtSection(finStmt, sectionType, finStmtType, finYear, party);
			}
		}
		return finStmt;
	}

	public ObjFinStmt getFinStmt(ObjParty party, int finYear, EFinStmtType finStmtType) {
		ObjFinStmt balSheet = finStmtRepo.findByPartyAndFinYearAndFinStmtType(party, finYear, finStmtType);
		int foundingYear = party.getFoundingDate().getYear();
		System.err.println("founding year: " + foundingYear);
		int balSheetYear = finYear;
		System.err.println("finYear: " + finYear);

		if (balSheet == null) {
			// TODO: iterate over past balance sheets until either year of foundation or the
			// last time a balance sheet existed
			// FIND LAST BALANCE SHEET WITH DATA
			while (balSheetYear > foundingYear) {
				balSheetYear--;
				balSheet = finStmtRepo.findByPartyAndFinYearAndFinStmtType(party, balSheetYear, finStmtType);
				if (balSheet != null) {
					System.err.println("found bal sheet: " + balSheet.getFinYear());
					balSheetYear++;
					break;
				}
			}
			System.err.println("bal sheet to create: " + balSheetYear);
			while (balSheetYear <= finYear) {
				balSheet = createFinStmt(party, balSheetYear, finStmtType);
				balSheetYear = balSheetYear + 1;
			}
		}
		return balSheet;
	}

	public ObjFinStmt getFinStmtCurrent(ObjParty party, EFinStmtType finStmtType) {
		int finYear = ctrlVarSvc.getFinYear();
		ObjFinStmt balSheet = getFinStmt(party, finYear, finStmtType);
		return balSheet;
	}
}
