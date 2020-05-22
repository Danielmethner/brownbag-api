package com.brownbag_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EBalSheetItemType;
import com.brownbag_api.model.jpa.ObjBalSheetItem;
import com.brownbag_api.model.jpa.ObjBalSheetSection;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjPos;
import com.brownbag_api.repo.BalSheetItemRepo;

@Service
public class BalSheetItemSvc {

	@Autowired
	private BalSheetItemRepo balSheetItemRepo;

	public ObjBalSheetItem createItem(EBalSheetItemType eBalSheetItemType, ObjBalSheetSection balSheetSectionType,
			int finYear, ObjParty party) {
		double qty = 0;
		// GET PREVIOUS YEARS ITEM AND TRANSFER ITS AMOUNT
		ObjBalSheetItem balSheetItemPrevYear = getByPartyAndFinYearAndItemType(party, finYear - 1, eBalSheetItemType);

		if(balSheetItemPrevYear != null) {
			qty = balSheetItemPrevYear.getQty();
		}
		ObjBalSheetItem balSheetItem = new ObjBalSheetItem(qty, eBalSheetItemType, finYear, party, balSheetSectionType);
		balSheetItem = balSheetItemRepo.save(balSheetItem);
		return balSheetItem;
	}

	public void save(ObjBalSheetItem bsi) {
		balSheetItemRepo.save(bsi);

	}

	public ObjBalSheetItem getByPartyAndFinYearAndItemType(ObjParty party, int finYear,
			EBalSheetItemType eBalSheetItem) {
		ObjBalSheetItem balSheetItem = balSheetItemRepo.findByPartyAndFinYearAndItemType(party, finYear, eBalSheetItem);
		return balSheetItem;
	}

	public ObjBalSheetItem getByPosAndFinYearAndItemType(ObjPos pos, int finYear, EBalSheetItemType eBalSheetItem) {
		ObjBalSheetItem balSheetItem = balSheetItemRepo.findByPartyAndFinYearAndItemType(pos.getParty(), finYear,
				eBalSheetItem);
		return balSheetItem;
	}

	public List<ObjBalSheetItem> getByBalSheetSection(ObjBalSheetSection section) {
		return balSheetItemRepo.findByBalSheetSection(section);

	}
}
