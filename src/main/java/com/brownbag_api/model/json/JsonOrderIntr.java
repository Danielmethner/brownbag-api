package com.brownbag_api.model.json;

import java.time.LocalDateTime;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;
import com.brownbag_api.model.jpa.ObjPosLoan;
import com.brownbag_api.model.jpa.ObjPosMacc;
import com.brownbag_api.model.jpa.OrderIntr;
import com.brownbag_api.model.jpa.OrderLoan;

public class JsonOrderIntr extends JsonOrder {

	private Long maccLenderId;
	private String maccLenderName;

	private Long maccDebtorId;
	private String maccDebtorName;

	private Long posLoanLenderId;
	private String posLoanLenderName;

	private Long posLoanBorrowerId;
	private String posLoanBorrowerName;

	public JsonOrderIntr(OrderIntr orderIntr) {
		super(orderIntr);
	}

	public Long getMaccLenderId() {
		return maccLenderId;
	}

	public void setMaccLenderId(Long maccLenderId) {
		this.maccLenderId = maccLenderId;
	}

	public String getMaccLenderName() {
		return maccLenderName;
	}

	public void setMaccLenderName(String maccLenderName) {
		this.maccLenderName = maccLenderName;
	}

	public Long getMaccDebtorId() {
		return maccDebtorId;
	}

	public void setMaccDebtorId(Long maccDebtorId) {
		this.maccDebtorId = maccDebtorId;
	}

	public String getMaccDebtorName() {
		return maccDebtorName;
	}

	public void setMaccDebtorName(String maccDebtorName) {
		this.maccDebtorName = maccDebtorName;
	}

	public Long getPosLoanLenderId() {
		return posLoanLenderId;
	}

	public void setPosLoanLenderId(Long posLoanLenderId) {
		this.posLoanLenderId = posLoanLenderId;
	}

	public String getPosLoanLenderName() {
		return posLoanLenderName;
	}

	public void setPosLoanLenderName(String posLoanLenderName) {
		this.posLoanLenderName = posLoanLenderName;
	}

	public Long getPosLoanBorrowerId() {
		return posLoanBorrowerId;
	}

	public void setPosLoanBorrowerId(Long posLoanBorrowerId) {
		this.posLoanBorrowerId = posLoanBorrowerId;
	}

	public String getPosLoanBorrowerName() {
		return posLoanBorrowerName;
	}

	public void setPosLoanBorrowerName(String posLoanBorrowerName) {
		this.posLoanBorrowerName = posLoanBorrowerName;
	}


}
