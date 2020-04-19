package com.brownbag_api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "POS_LOAN")
public class PosLoan extends Pos implements Serializable {

	private static final long serialVersionUID = 5228815539510545768L;

	@NotNull
	@OneToOne(targetEntity = Pos.class)
	@JoinColumn(name = "MACC_LENDER_ID")
	private Pos maccLender;

	@NotNull
	@OneToOne(targetEntity = Pos.class)
	@JoinColumn(name = "MACC_DEBTOR_ID")
	private Pos maccDebtor;

	public PosLoan() {
	}

	public PosLoan(@NotNull double qty, @NotNull Asset asset, @NotNull Party owner, @NotNull Pos maccGrant,
			@NotNull Pos maccRcv) {
		super(1, qty, 0, 0, asset, owner, false);
		this.maccLender = maccGrant;
		this.maccDebtor = maccRcv;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
