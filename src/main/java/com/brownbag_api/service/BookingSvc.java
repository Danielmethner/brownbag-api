package com.brownbag_api.service;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.BalSheet;
import com.brownbag_api.model.BalSheetItem;
import com.brownbag_api.model.Booking;
import com.brownbag_api.model.Order;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.User;
import com.brownbag_api.model.data.EAsset;
import com.brownbag_api.model.data.EBalSheetItemType;
import com.brownbag_api.model.data.EBalSheetSectionType;
import com.brownbag_api.model.data.EBookingDir;
import com.brownbag_api.model.data.EOrderAction;
import com.brownbag_api.model.data.EOrderDir;
import com.brownbag_api.model.data.EOrderStatus;
import com.brownbag_api.model.data.EOrderType;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.BalSheetItemRepo;
import com.brownbag_api.repo.BookingRepo;
import com.brownbag_api.repo.OrderPayRepo;
import com.brownbag_api.repo.PosRepo;
import com.brownbag_api.util.UtilDate;

@Service
public class BookingSvc {

	@Autowired
	private BookingRepo bookingRepo;

	@Autowired
	private PosRepo posRepo;

	@Autowired
	private BalSheetSvc balSheetSvc;

	@Autowired
	private BalSheetItemSvc balSheetItemSvc;
	
	public Pos createBooking(Order order, Pos pos, EBookingDir eBookingDir, EBalSheetItemType eBalSheetItemActive, EBalSheetItemType eBalSheetItemPassive) {
		double orderQty = order.getQty();
		orderQty = eBookingDir == EBookingDir.CREDIT ? orderQty : -orderQty;
		double posQty = pos.getQty();
		int finYear = UtilDate.finYear;
		
		// GET BALANCE SHEET (Instanciate if not exists)
		balSheetSvc.getBalSheet(pos.getParty(), finYear);
		
		// UPDATE BALANCE SHEET ITEM - ASSETS
		BalSheetItem bsiActive = balSheetItemSvc.getItem(pos, finYear, eBalSheetItemActive);
		bsiActive.setQty(bsiActive.getQty() + orderQty);
		balSheetItemSvc.save(bsiActive);
		
		// UPDATE BALANCE SHEET ITEM - Financing (LIABILITIES/ EQUITY)
		BalSheetItem bsiPassive = balSheetItemSvc.getItem(pos, finYear, eBalSheetItemPassive);
		bsiPassive.setQty(bsiPassive.getQty() + orderQty);
		balSheetItemSvc.save(bsiPassive);
		
		// CREATE BOOKING
		Booking booking = new Booking(order, posQty, orderQty, posQty + orderQty, pos, bsiPassive);
		booking = bookingRepo.save(booking);
		pos.setQty(booking.getPosBalNew());
		return posRepo.save(pos);
	}
}
