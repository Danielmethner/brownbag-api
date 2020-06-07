package com.brownbag_api.model.jpa;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;

@Entity
@Table(name = "order_intr")
public class OrderIntr extends Order implements Serializable {

	private static final long serialVersionUID = 4643589803146964779L;

	@NotNull
	@OneToOne(targetEntity = ObjPosMacc.class)
	@JoinColumn(name = "POS_LENDER_ID")
	private ObjPosMacc maccLender;

	@NotNull
	@OneToOne(targetEntity = ObjPosMacc.class)
	@JoinColumn(name = "POS_DEBTOR_ID")
	private ObjPosMacc maccDebtor;

	@OneToOne(targetEntity = ObjPosLoan.class)
	@JoinColumn(name = "POS_LOAN_LENDER_ID")
	private ObjPosLoan posLoanLender;

	@OneToOne(targetEntity = ObjPosLoan.class)
	@JoinColumn(name = "POS_LOAN_BORROWER_ID")
	private ObjPosLoan posLoanBorrower;

	public OrderIntr() {
	}

	public OrderIntr(@NotNull double qty, @NotNull ObjAsset asset, EOrderType orderType, EOrderStatus orderStatus,
			@NotNull ObjUser user, String advText, @NotNull ObjPosMacc maccLender, @NotNull ObjPosMacc maccDebtor,
			ObjPosLoan posLoanLender, ObjPosLoan posLoanBorrower) {
		super(qty, asset, EOrderType.INTR, EOrderStatus.NEW, user, advText);
		this.maccLender = maccLender;
		this.maccDebtor = maccDebtor;
		this.posLoanLender = posLoanLender;
		this.posLoanBorrower = posLoanBorrower;
	}

	public OrderIntr(ObjPosLoan posLoan, @NotNull ObjUser user, String advText, double amount) {
		super(amount, posLoan.getAsset(), EOrderType.INTR, EOrderStatus.NEW, user, advText);
		this.maccLender = posLoan.getMaccLender();
		this.maccDebtor = posLoan.getMaccDebtor();
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

}
