package com.brownbag_api.model.trans;

import com.brownbag_api.model.Party;
import com.brownbag_api.model.enums.EBalSheetItemType;
import com.brownbag_api.model.enums.EBookingDir;

public class BalTrxTrans {

	private EBalSheetItemType itemType;
	private double qty;
	private EBookingDir bookingDir;
	private Party party;

	public BalTrxTrans(EBalSheetItemType itemType, double qty, EBookingDir bookingDir, Party party) {
		this.itemType = itemType;
		this.qty = qty;
		this.bookingDir = bookingDir;
		this.party = party;
	}

	public EBalSheetItemType getItemType() {
		return itemType;
	}

	public double getQty() {
		return qty;
	}

	public EBookingDir getBookingDir() {
		return bookingDir;
	}

	public void setBookingDir(EBookingDir bookingDir) {
		this.bookingDir = bookingDir;
	}

	public void setItemType(EBalSheetItemType itemType) {
		this.itemType = itemType;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

}
