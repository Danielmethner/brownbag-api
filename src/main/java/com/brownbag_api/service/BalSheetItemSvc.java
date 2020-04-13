package com.brownbag_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.BalSheetItem;
import com.brownbag_api.model.BalSheetSectionType;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.data.EBalSheetItemType;
import com.brownbag_api.repo.BalSheetItemRepo;
import com.brownbag_api.repo.BalSheetRepo;
import com.brownbag_api.repo.BalSheetSectionRepo;
import com.brownbag_api.repo.BookingRepo;
import com.brownbag_api.repo.PosRepo;

@Service
public class BalSheetItemSvc {

	@Autowired
	private BalSheetRepo balSheetRepo;

	@Autowired
	private BalSheetSvc balSheetSvc;

	@Autowired
	private BalSheetSectionRepo balSheetSectionRepo;

	@Autowired
	private BalSheetItemRepo balSheetItemRepo;

	@Autowired
	private BookingRepo bookingRepo;

	@Autowired
	private PosRepo posRepo;

	public BalSheetItem createItem(EBalSheetItemType eBalSheetItemType, BalSheetSectionType balSheetSectionType, int finYear, Party party) {
		BalSheetItem balSheetItem = new BalSheetItem(0, eBalSheetItemType, finYear, party, balSheetSectionType);
		balSheetItem = balSheetItemRepo.save(balSheetItem);
		return balSheetItem;
	}

	public void save(BalSheetItem bsi) {
		balSheetItemRepo.save(bsi);

	}

	public BalSheetItem getItem(Pos pos, int finYear, EBalSheetItemType eBalSheetItem) {
		BalSheetItem balSheetItem = balSheetItemRepo.findByPartyAndFinYearAndItemType(pos.getParty(), finYear, eBalSheetItem);
		return balSheetItem;
	}
}
