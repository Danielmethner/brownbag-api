package com.brownbag_api.model.json;

import java.time.LocalDateTime;

import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;
import com.brownbag_api.model.jpa.ObjPosLoan;
import com.brownbag_api.model.jpa.ObjPosMacc;

public class JsonOrderLoan extends JsonOrder {

//	public JsonOrderLoan(OrderLoan jpaOrderLoan) {
//		super(qty, orderType, orderStatus, assetId, userId);
//	}

	private ObjPosMacc maccLender;
	private ObjPosMacc maccDebtor;
	private ObjPosLoan posLoanLender;
	private ObjPosLoan posLoanBorrower;
	private LocalDateTime matDate;
	private double intrRate;
	private Long partyId;
	

	public JsonOrderLoan(double qty, EOrderType orderType, EOrderStatus orderStatus, Long assetId, Long userId,
			ObjPosMacc maccLender, ObjPosMacc maccDebtor, ObjPosLoan posLoanLender, ObjPosLoan posLoanBorrower,
			LocalDateTime matDate, double intrRate, Long partyId) {
		super(qty, orderType, orderStatus, assetId, userId);
		this.maccLender = maccLender;
		this.maccDebtor = maccDebtor;
		this.posLoanLender = posLoanLender;
		this.posLoanBorrower = posLoanBorrower;
		this.matDate = matDate;
		this.partyId = partyId;
		this.intrRate = intrRate;
	}

	public Long getPartyId() {
		return partyId;
	}

	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}

	public ObjPosMacc getMaccLender() {
		return maccLender;
	}

	public void setMaccLender(ObjPosMacc maccLender) {
		this.maccLender = maccLender;
	}

	public ObjPosMacc getMaccDebtor() {
		return maccDebtor;
	}

	public void setMaccDebtor(ObjPosMacc maccDebtor) {
		this.maccDebtor = maccDebtor;
	}

	public ObjPosLoan getPosLoanLender() {
		return posLoanLender;
	}

	public void setPosLoanLender(ObjPosLoan posLoanLender) {
		this.posLoanLender = posLoanLender;
	}

	public ObjPosLoan getPosLoanBorrower() {
		return posLoanBorrower;
	}

	public void setPosLoanBorrower(ObjPosLoan posLoanBorrower) {
		this.posLoanBorrower = posLoanBorrower;
	}

	public LocalDateTime getMatDate() {
		return matDate;
	}

	public void setMatDate(LocalDateTime matDate) {
		this.matDate = matDate;
	}

	public double getIntrRate() {
		return intrRate;
	}

	public void setIntrRate(double intrRate) {
		this.intrRate = intrRate;
	}

}
