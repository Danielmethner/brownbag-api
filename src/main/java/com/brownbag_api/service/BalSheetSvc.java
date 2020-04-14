package com.brownbag_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.BalSheet;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.data.EBalSheetSectionType;
import com.brownbag_api.repo.BalSheetRepo;

@Service
public class BalSheetSvc {

	@Autowired
	private BalSheetRepo balSheetRepo;

	@Autowired
	private BalSheetSectionSvc balSheetSectionSvc;

	public BalSheet createBalSheet(Party party, int finYear) {
		BalSheet balSheet = new BalSheet(party, finYear);
		balSheet = balSheetRepo.save(balSheet);
		balSheetSectionSvc.createBalSheetSection(balSheet, EBalSheetSectionType.ASSETS);
		balSheetSectionSvc.createBalSheetSection(balSheet, EBalSheetSectionType.LIABILITIES);
		balSheetSectionSvc.createBalSheetSection(balSheet, EBalSheetSectionType.EQUITY);
		return balSheet;
	}

	public BalSheet getBalSheet(Party party, int finYear) {
		BalSheet balSheet = balSheetRepo.findByPartyAndFinYear(party, finYear);
		if (balSheet == null) {
			balSheet = createBalSheet(party, finYear);
		}
		return balSheet;
	}
}
