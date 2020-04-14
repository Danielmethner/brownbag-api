package com.brownbag_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.BalSheetItem;
import com.brownbag_api.model.Booking;
import com.brownbag_api.model.Order;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.data.EBalSheetItemType;
import com.brownbag_api.model.data.EBookingDir;
import com.brownbag_api.repo.BookingRepo;
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

	public Pos createBooking(Order order, Pos pos, EBookingDir eBookingDir, EBalSheetItemType eBalSheetItemActive,
			EBalSheetItemType eBalSheetItemPassive) {

		double orderQty = order.getQty();
		orderQty = eBookingDir == EBookingDir.CREDIT ? orderQty : -orderQty;

//		System.out.println("Pos Qty directly: " + pos.getQty());
//		pos = posRepo.findById(pos.getId()).orElse(null);
//		System.out.println("Pos Qty from DB: " + pos.getQty());
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
