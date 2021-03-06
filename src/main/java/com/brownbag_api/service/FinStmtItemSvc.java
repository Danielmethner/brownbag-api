package com.brownbag_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EFinStmtItemType;
import com.brownbag_api.model.enums.EFinStmtType;
import com.brownbag_api.model.jpa.ObjFinStmtItem;
import com.brownbag_api.model.jpa.ObjFinStmtSection;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjPos;
import com.brownbag_api.model.trans.FinStmtTrxTrans;
import com.brownbag_api.repo.FinStmtItemRepo;

@Service
public class FinStmtItemSvc {

	@Autowired
	private FinStmtItemRepo balSheetItemRepo;

	@Autowired
	private FinStmtSectionSvc finStmtSectionSvc;

	public ObjFinStmtItem createItem(EFinStmtItemType eBalSheetItemType, ObjFinStmtSection balSheetSectionType,
			int finYear, ObjParty party, EFinStmtType finStmtType) {
		double qty = 0;

		// IF BALANCE SHEET: GET PREVIOUS YEARS ITEM AND TRANSFER ITS AMOUNT
		if (finStmtType == EFinStmtType.BAL_SHEET) {
			ObjFinStmtItem balSheetItemPrevYear = getByPartyAndFinYearAndItemType(party, finYear - 1,
					eBalSheetItemType);

			if (balSheetItemPrevYear != null) {
				qty = balSheetItemPrevYear.getQty();
			}
		}

		ObjFinStmtItem balSheetItem = new ObjFinStmtItem(qty, eBalSheetItemType, finYear, party, balSheetSectionType,
				finStmtType);
		balSheetItem = balSheetItemRepo.save(balSheetItem);
		return balSheetItem;
	}

	public ObjFinStmtItem save(ObjFinStmtItem bsi) {
		return balSheetItemRepo.save(bsi);

	}

	public ObjFinStmtItem getByPartyAndFinYearAndItemType(ObjParty party, int finYear, EFinStmtItemType eBalSheetItem) {
		ObjFinStmtItem balSheetItem = balSheetItemRepo.findByPartyAndFinYearAndItemType(party, finYear, eBalSheetItem);
		return balSheetItem;
	}

	public ObjFinStmtItem getByPosAndFinYearAndItemType(ObjPos pos, int finYear, EFinStmtItemType eBalSheetItem) {
		ObjFinStmtItem balSheetItem = balSheetItemRepo.findByPartyAndFinYearAndItemType(pos.getParty(), finYear,
				eBalSheetItem);
		return balSheetItem;
	}

	public List<ObjFinStmtItem> getByBalSheetSection(ObjFinStmtSection section) {
		return balSheetItemRepo.findByFinStmtSection(section);

	}

	public ObjFinStmtItem bookTrx(FinStmtTrxTrans finStmtTrxTransient, int finYear) {
		double finStmtTrxQty = finStmtTrxTransient.getBookQty();
		ObjFinStmtItem finStmtItem = getByPartyAndFinYearAndItemType(finStmtTrxTransient.getParty(), finYear,
				finStmtTrxTransient.getItemType());
		finStmtItem.setQty(finStmtItem.getQty() + finStmtTrxQty);

		// UPDATE BALANCE SHEET SECTION
		ObjFinStmtSection finStmtSection = finStmtItem.getFinStmtSection();
		finStmtSection.increaseQty(finStmtTrxQty);
		finStmtSectionSvc.save(finStmtSection);

		return save(finStmtItem);
	}
}
