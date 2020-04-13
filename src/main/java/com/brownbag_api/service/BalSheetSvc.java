package com.brownbag_api.service;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.BalSheet;
import com.brownbag_api.model.BalSheetSection;
import com.brownbag_api.model.Booking;
import com.brownbag_api.model.Order;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.User;
import com.brownbag_api.model.data.EAsset;
import com.brownbag_api.model.data.EBalSheetSection;
import com.brownbag_api.model.data.EBookingDir;
import com.brownbag_api.model.data.EOrderAction;
import com.brownbag_api.model.data.EOrderDir;
import com.brownbag_api.model.data.EOrderStatus;
import com.brownbag_api.model.data.EOrderType;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.BalSheetRepo;
import com.brownbag_api.repo.BalSheetSectionRepo;
import com.brownbag_api.repo.BookingRepo;
import com.brownbag_api.repo.OrderPayRepo;
import com.brownbag_api.repo.PosRepo;

@Service
public class BalSheetSvc {

	@Autowired
	private BalSheetRepo balSheetRepo;
	
	@Autowired
	private BalSheetSectionSvc balSheetSectionSvc;

	@Autowired
	private BookingRepo bookingRepo;

	@Autowired
	private PosRepo posRepo;

	public BalSheet createBalSheet(Party party, int finYear) {
		String balSheetName = "Balance Sheet for " + party.getName() + " as of Financial Year: " + finYear;
		BalSheet balSheet = new BalSheet(balSheetName, party, finYear);
		balSheet = balSheetRepo.save(balSheet);
		balSheetSectionSvc.createBalSheetSection(balSheet, EBalSheetSection.ASSETS);
		balSheetSectionSvc.createBalSheetSection(balSheet, EBalSheetSection.LIABILITIES);
		balSheetSectionSvc.createBalSheetSection(balSheet, EBalSheetSection.EQUITY);
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
