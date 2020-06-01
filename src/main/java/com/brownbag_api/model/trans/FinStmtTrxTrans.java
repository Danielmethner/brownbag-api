package com.brownbag_api.model.trans;

import com.brownbag_api.model.enums.EBookingDir;
import com.brownbag_api.model.enums.EFinStmtItemType;
import com.brownbag_api.model.enums.EFinStmtType;
import com.brownbag_api.model.jpa.ObjParty;

public class FinStmtTrxTrans {

	private EFinStmtItemType itemType;
	private double qty;
	private EBookingDir bookingDir;
	private ObjParty party;

	public FinStmtTrxTrans(EFinStmtItemType itemType, double qty, EBookingDir bookingDir, ObjParty party) {
		this.itemType = itemType;
		this.qty = qty;
		this.bookingDir = bookingDir;
		this.party = party;
	}

	public EFinStmtItemType getItemType() {
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

	public void setItemType(EFinStmtItemType itemType) {
		this.itemType = itemType;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public ObjParty getParty() {
		return party;
	}

	public void setParty(ObjParty party) {
		this.party = party;
	}
}
