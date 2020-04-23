package com.brownbag_api.model;

import java.io.Serializable;
import java.util.Date;

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
	@OneToOne(targetEntity = Pos.class)
	@JoinColumn(name = "POS_LENDER_ID")
	private Pos maccLender;

	@NotNull
	@OneToOne(targetEntity = Pos.class)
	@JoinColumn(name = "POS_DEBTOR_ID")
	private Pos maccDebtor;

	@OneToOne(targetEntity = PosLoan.class)
	@JoinColumn(name = "POS_LOAN_ID")
	private PosLoan posLoan;

	@Column(name = "MAT_DATE")
	private Date matDate;

	@NotNull
	@Column(name = "INTR_RATE")
	private double intrRate;

	public OrderLoan() {
	}

	public OrderLoan(@NotNull double qty, @NotNull Asset asset, EOrderType orderType, EOrderStatus orderStatus,
			@NotNull User user, String advText, @NotNull Pos maccLender, @NotNull Pos maccDebtor, PosLoan posLoan,
			Date matDate, @NotNull double intrRate) {
		super(qty, asset, orderType, orderStatus, user, advText);
		this.maccLender = maccLender;
		this.maccDebtor = maccDebtor;
		this.posLoan = posLoan;
		this.matDate = matDate;
		this.intrRate = intrRate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getMatDate() {
		return matDate;
	}

	public void setMatDate(Date matDate) {
		this.matDate = matDate;
	}

	public double getIntrRate() {
		return intrRate;
	}

	public void setIntrRate(double intrRate) {
		this.intrRate = intrRate;
	}

	public Pos getMaccLender() {
		return maccLender;
	}

	public void setMaccLender(Pos maccLender) {
		this.maccLender = maccLender;
	}

	public Pos getMaccDebtor() {
		return maccDebtor;
	}

	public void setMaccDebtor(Pos maccDebtor) {
		this.maccDebtor = maccDebtor;
	}

	public PosLoan getPosLoan() {
		return posLoan;
	}

	public void setPosLoan(PosLoan posLoan) {
		this.posLoan = posLoan;
	}

}
