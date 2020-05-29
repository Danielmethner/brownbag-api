package com.brownbag_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EFinStmtSectionType;
import com.brownbag_api.model.enums.EBookingDir;
import com.brownbag_api.model.jpa.FinStmtTrx;
import com.brownbag_api.model.jpa.Booking;
import com.brownbag_api.model.jpa.ObjFinStmtItem;
import com.brownbag_api.model.jpa.ObjFinStmtSection;
import com.brownbag_api.model.jpa.ObjPos;
import com.brownbag_api.model.jpa.Order;
import com.brownbag_api.model.trans.BalTrxTrans;
import com.brownbag_api.repo.FinStmtTrxRepo;
import com.brownbag_api.repo.BookingRepo;
import com.brownbag_api.repo.PosRepo;
import com.brownbag_api.util.UtilDate;

@Service
public class BookingSvc {

	@Autowired
	private FinStmtTrxRepo balTrxRepo;

	@Autowired
	private BookingRepo bookingRepo;

	@Autowired
	private PosRepo posRepo;

	@Autowired
	private FinStmtSvc balSheetSvc;

	@Autowired
	private FinStmtSectionSvc balSheetSectionSvc;

	@Autowired
	private FinStmtItemSvc balSheetItemSvc;

	@Autowired
	private LogSvc logSvc;
	
	@Autowired
	private ControlSvc controlSvc;

	public ObjPos createBooking(Order order, ObjPos pos, EBookingDir eBookingDir,
			ArrayList<BalTrxTrans> balTrxTransientList, double bookQty, String bookText) {

		bookQty = bookQty > 0 ? bookQty : order.getQty();
		bookQty = eBookingDir == EBookingDir.CREDIT ? bookQty : -bookQty;

		double posQty = pos.getQty();
		//TODO: Fix fixYear
		int finYear = controlSvc.getFinYear();

		double balTrxAssets = 0;
		double balTrxLiabEquity = 0;

		// CREATE BOOKING
		Booking booking = new Booking(order, posQty, bookQty, posQty + bookQty, pos, bookText);
		booking = bookingRepo.save(booking);

		// GET BALANCE SHEET (Instanciate if not exists)
		balSheetSvc.getBalSheet(pos.getParty(), finYear);

		// GENERATE BALANCE SHEET BOOKINGS
		for (BalTrxTrans balTrxTransient : balTrxTransientList) {

			double balTrxQty = balTrxTransient.getBookingDir() == EBookingDir.CREDIT ? balTrxTransient.getQty()
					: (-1) * balTrxTransient.getQty();

			balTrxAssets = balTrxTransient.getItemType().getSection() == EFinStmtSectionType.ASSETS
					? balTrxAssets + balTrxQty
					: balTrxAssets;
			balTrxLiabEquity = balTrxTransient.getItemType().getSection() != EFinStmtSectionType.ASSETS
					? balTrxLiabEquity + balTrxQty
					: balTrxLiabEquity;

			// UPDATE BALANCE SHEET ITEM
			ObjFinStmtItem bsi = balSheetItemSvc.getByPartyAndFinYearAndItemType(balTrxTransient.getParty(), finYear,
					balTrxTransient.getItemType());
			bsi.setQty(bsi.getQty() + balTrxQty);
			balSheetItemSvc.save(bsi);

			// UPDATE BALANCE SHEET SECTION
			ObjFinStmtSection bss = bsi.getBalSheetSection();
			bss.increaseQty(balTrxQty);
			balSheetSectionSvc.save(bss);

			// BALANCE SHEET TRANSACTION
			FinStmtTrx balTrx = new FinStmtTrx(order, bsi, booking, balTrxQty);
			balTrxRepo.save(balTrx);
		}

		if (balTrxAssets != balTrxLiabEquity) {
			logSvc.write("BookingSvc.createBooking(): balTrxAssets: " + balTrxAssets);
			logSvc.write("BookingSvc.createBooking(): balTrxLiabEquity: " + balTrxLiabEquity);
			logSvc.write(
					"BookingSvc.createBooking(): Asset and Financing bookings of Balance sheet must be equal. Booking cannot proceed. Order ID: "
							+ order.getId());
			return null;
		}

		pos.setQty(booking.getPosBalNew());
		return posRepo.save(pos);
	}

	public List<Booking> getByPos(ObjPos pos) {
		return bookingRepo.findByPos(pos);
	}
}
