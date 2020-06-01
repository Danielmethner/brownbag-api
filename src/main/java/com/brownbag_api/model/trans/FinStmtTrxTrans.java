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
	private EFinStmtType finStmtType;

	public FinStmtTrxTrans(EFinStmtItemType itemType, double qty, EBookingDir bookingDir, ObjParty party, EFinStmtType finStmtType) {
		this.itemType = itemType;
		this.qty = qty;
		this.bookingDir = bookingDir;
		this.party = party;
		this.finStmtType = finStmtType;
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

	public EFinStmtType getFinStmtType() {
		return finStmtType;
	}

	public void setFinStmtType(EFinStmtType finStmtType) {
		this.finStmtType = finStmtType;
	}

}
