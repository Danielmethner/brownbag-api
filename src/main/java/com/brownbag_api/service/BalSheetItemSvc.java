package com.brownbag_api.service;

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
		ObjBalSheetItem balSheetItem = new ObjBalSheetItem(0, eBalSheetItemType, finYear, party, balSheetSectionType);
		balSheetItem = balSheetItemRepo.save(balSheetItem);
		return balSheetItem;
	}

	public void save(ObjBalSheetItem bsi) {
		balSheetItemRepo.save(bsi);

	}

	public ObjBalSheetItem getItem(ObjParty party, int finYear, EBalSheetItemType eBalSheetItem) {
		ObjBalSheetItem balSheetItem = balSheetItemRepo.findByPartyAndFinYearAndItemType(party, finYear, eBalSheetItem);
		return balSheetItem;
	}

	public ObjBalSheetItem getItem(ObjPos pos, int finYear, EBalSheetItemType eBalSheetItem) {
		ObjBalSheetItem balSheetItem = balSheetItemRepo.findByPartyAndFinYearAndItemType(pos.getParty(), finYear,
				eBalSheetItem);
		return balSheetItem;
	}
}
