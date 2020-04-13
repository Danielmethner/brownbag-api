package com.brownbag_api.service;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.BalSheet;
import com.brownbag_api.model.Booking;
import com.brownbag_api.model.Order;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.User;
import com.brownbag_api.model.data.EAsset;
import com.brownbag_api.model.data.EBookingDir;
import com.brownbag_api.model.data.EOrderAction;
import com.brownbag_api.model.data.EOrderDir;
import com.brownbag_api.model.data.EOrderStatus;
import com.brownbag_api.model.data.EOrderType;
import com.brownbag_api.repo.AssetRepo;
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

	public Pos createBooking(Order order, Pos pos, EBookingDir eBookingDir) {
		double orderQty = order.getQty();
		orderQty = eBookingDir == EBookingDir.CREDIT ? orderQty : -orderQty;
		double posQty = pos.getQty();

		// GET BALANCE SHEET
		BalSheet balSheet = balSheetSvc.getBalSheet(pos.getParty(), UtilDate.finYear);
		System.err.println("balSheetSvc: " + balSheet.getName());
		
		// CREATE BOOKING
		Booking booking = new Booking(order, posQty, orderQty, posQty + orderQty, pos);
		booking = bookingRepo.save(booking);
		pos.setQty(booking.getPosBalNew());
		return posRepo.save(pos);
	}
}
