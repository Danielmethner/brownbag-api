package com.brownbag_api.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.BalSheetItem;
import com.brownbag_api.model.BalTrx;
import com.brownbag_api.model.BalTrxTransient;
import com.brownbag_api.model.Booking;
import com.brownbag_api.model.Order;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.data.EBalSheetSectionType;
import com.brownbag_api.model.data.EBookingDir;
import com.brownbag_api.repo.BalTrxRepo;
import com.brownbag_api.repo.BookingRepo;
import com.brownbag_api.repo.PosRepo;
import com.brownbag_api.util.UtilDate;

@Service
public class BookingSvc {

	@Autowired
	private BalTrxRepo balTrxRepo;

	@Autowired
	private BookingRepo bookingRepo;

	@Autowired
	private PosRepo posRepo;

	@Autowired
	private BalSheetSvc balSheetSvc;

	@Autowired
	private BalSheetItemSvc balSheetItemSvc;

	@Autowired
	private LogSvc logSvc;

	public Pos createBooking(Order order, Pos pos, EBookingDir eBookingDir,
			ArrayList<BalTrxTransient> balTrxTransientList) {

		double orderQty = order.getQty();
		orderQty = eBookingDir == EBookingDir.CREDIT ? orderQty : -orderQty;

		double posQty = pos.getQty();
		int finYear = UtilDate.getFinYear();

		double balTrxAssets = 0;
		double balTrxLiabEquity = 0;

		// CREATE BOOKING
		Booking booking = new Booking(order, posQty, orderQty, posQty + orderQty, pos, order.getAdvText());
		booking = bookingRepo.save(booking);

		// GET BALANCE SHEET (Instanciate if not exists)
		balSheetSvc.getBalSheet(pos.getParty(), finYear);

		// GENERATE BALANCE SHEET BOOKINGS
		for (BalTrxTransient balTrxTransient : balTrxTransientList) {

			double balTrxQty = balTrxTransient.getBookingDir() == EBookingDir.CREDIT ? balTrxTransient.getQty()
					: (-1) * balTrxTransient.getQty();

			balTrxAssets = balTrxTransient.getItemType().getSection() == EBalSheetSectionType.ASSETS
					? balTrxAssets + balTrxQty
					: balTrxAssets;
			balTrxLiabEquity = balTrxTransient.getItemType().getSection() != EBalSheetSectionType.ASSETS
					? balTrxLiabEquity + balTrxQty
					: balTrxLiabEquity;

			// UPDATE BALANCE SHEET ITEM
			BalSheetItem bsi = balSheetItemSvc.getItem(balTrxTransient.getParty(), finYear,
					balTrxTransient.getItemType());
			bsi.setQty(bsi.getQty() + balTrxQty);
			balSheetItemSvc.save(bsi);

			// BALANCE SHEET TRANSACTION
			BalTrx balTrx = new BalTrx(order, bsi, booking, balTrxQty);
			balTrxRepo.save(balTrx);
		}

		if (balTrxAssets != balTrxLiabEquity) {
			logSvc.write(
					"BookingSvc.createBooking(): Asset and Financing bookings of Balance sheet must be equal. Booking cannot proceed. Order ID: "
							+ order.getId());
			return null;
		}

		pos.setQty(booking.getPosBalNew());
		return posRepo.save(pos);
	}
}
