package com.brownbag_api.model.jpa;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;

@Entity
@Table(name = "order_loan")
public class OrderLoan extends Order implements Serializable {

	private static final long serialVersionUID = 4643589803146964779L;

	@NotNull
	@OneToOne(targetEntity = ObjPos.class)
	@JoinColumn(name = "POS_LENDER_ID")
	private ObjPos maccLender;

	@NotNull
	@OneToOne(targetEntity = ObjPos.class)
	@JoinColumn(name = "POS_DEBTOR_ID")
	private ObjPos maccDebtor;

	@OneToOne(targetEntity = ObjPosLoan.class)
	@JoinColumn(name = "POS_LOAN_LENDER_ID")
	private ObjPosLoan posLoanLender;

	@OneToOne(targetEntity = ObjPosLoan.class)
	@JoinColumn(name = "POS_LOAN_BORROWER_ID")
	private ObjPosLoan posLoanBorrower;

	@Column(name = "MAT_DATE")
	private LocalDateTime  matDate;

	@NotNull
	@Column(name = "INTR_RATE")
	private double intrRate;

	public OrderLoan() {
	}
	
	

	public OrderLoan(@NotNull double qty, @NotNull ObjAsset asset, EOrderType orderType, EOrderStatus orderStatus,
			@NotNull ObjUser user, String advText, @NotNull ObjPos maccLender, @NotNull ObjPos maccDebtor,
			ObjPosLoan posLoanLender, ObjPosLoan posLoanBorrower, LocalDateTime  matDate, @NotNull double intrRate) {
		super(qty, asset, orderType, orderStatus, user, advText);
		this.maccLender = maccLender;
		this.maccDebtor = maccDebtor;
		this.posLoanLender = posLoanLender;
		this.posLoanBorrower = posLoanBorrower;
		this.matDate = matDate;
		this.intrRate = intrRate;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public LocalDateTime  getMatDate() {
		return matDate;
	}

	public void setMatDate(LocalDateTime  matDate) {
		this.matDate = matDate;
	}

	public double getIntrRate() {
		return intrRate;
	}

	public void setIntrRate(double intrRate) {
		this.intrRate = intrRate;
	}

	public ObjPos getMaccLender() {
		return maccLender;
	}

	public void setMaccLender(ObjPos maccLender) {
		this.maccLender = maccLender;
	}

	public ObjPos getMaccDebtor() {
		return maccDebtor;
	}

	public void setMaccDebtor(ObjPos maccDebtor) {
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

}
