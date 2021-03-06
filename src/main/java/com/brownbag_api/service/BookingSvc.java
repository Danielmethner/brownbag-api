package com.brownbag_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EBookingDir;
import com.brownbag_api.model.enums.EFinStmtType;
import com.brownbag_api.model.jpa.Booking;
import com.brownbag_api.model.jpa.FinStmtTrx;
import com.brownbag_api.model.jpa.ObjFinStmtItem;
import com.brownbag_api.model.jpa.ObjPos;
import com.brownbag_api.model.jpa.Order;
import com.brownbag_api.model.trans.FinStmtTrxTrans;
import com.brownbag_api.repo.BookingRepo;
import com.brownbag_api.repo.FinStmtTrxRepo;
import com.brownbag_api.repo.PosRepo;

@Service
public class BookingSvc {

	@Autowired
	private FinStmtTrxRepo finStmtTrxRepo;

	@Autowired
	private BookingRepo bookingRepo;

	@Autowired
	private PosRepo posRepo;

	@Autowired
	private FinStmtSvc finStmtSvc;


	@Autowired
	private FinStmtItemSvc finStmtItemSvc;

	@Autowired
	private LogSvc logSvc;

	@Autowired
	private ControlSvc controlSvc;

	public ObjPos createBooking(Order order, ObjPos pos, EBookingDir eBookingDir,
			ArrayList<FinStmtTrxTrans> finStmtTrxTransientList, double bookQty, String bookText) {

		bookQty = bookQty > 0 ? bookQty : order.getQty();
		bookQty = eBookingDir == EBookingDir.CREDIT ? bookQty : -bookQty;

		double posQty = pos.getQty();
		// TODO: Fix fixYear
		int finYear = controlSvc.getFinYear();

		double balTrxAssets = 0;
		double balTrxLiabEquity = 0;

		// CREATE BOOKING
		Booking booking = new Booking(order, posQty, bookQty, posQty + bookQty, pos, bookText);
		booking = bookingRepo.save(booking);

		// ENSURE FINANICAL STATEMENTS EXIST (CREATE IF MISSING)
		finStmtSvc.getFinStmt(pos.getParty(), finYear, EFinStmtType.BAL_SHEET);
		finStmtSvc.getFinStmt(pos.getParty(), finYear, EFinStmtType.INCOME_STMT);

		// GENERATE FINANCIAL STATEMENT BOOKINGS
		for (FinStmtTrxTrans finStmtTrxTransient : finStmtTrxTransientList) {

			// deduct amount if debit
			double finStmtTrxQty = finStmtTrxTransient.getBookQty();

			// AGGREGATE ASSET TRX AMOUNTS TO ENSURE DEBIT = CREDIT
			balTrxAssets = finStmtTrxTransient.isAsset() ? balTrxAssets + finStmtTrxQty : balTrxAssets;

			balTrxLiabEquity = finStmtTrxTransient.isLiabOrEquity() ? balTrxLiabEquity + finStmtTrxQty
					: balTrxLiabEquity;

			// UPDATE BALANCE SHEET ITEMS
			ObjFinStmtItem finStmtItem = finStmtItemSvc.bookTrx(finStmtTrxTransient, finYear);

			// BALANCE SHEET TRANSACTION
			FinStmtTrx finStmtTrx = new FinStmtTrx(order, finStmtItem, booking, finStmtTrxQty);
			finStmtTrxRepo.save(finStmtTrx);
		}

		// ENSURE DB = CR
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
