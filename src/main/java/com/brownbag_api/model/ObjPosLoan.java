package com.brownbag_api.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "OBJ_POS_LOAN")
public class ObjPosLoan extends ObjPos implements Serializable {

	private static final long serialVersionUID = 5228815539510545768L;

	@NotNull
	@OneToOne(targetEntity = ObjPos.class)
	@JoinColumn(name = "MACC_LENDER_ID")
	private ObjPos maccLender;

	@NotNull
	@OneToOne(targetEntity = ObjPos.class)
	@JoinColumn(name = "MACC_DEBTOR_ID")
	private ObjPos maccDebtor;

	public ObjPosLoan() {
	}

	public ObjPosLoan(@NotNull double qty, @NotNull ObjAsset asset, @NotNull ObjParty owner, @NotNull ObjPos maccGrant,
			@NotNull ObjPos maccRcv) {
		super(qty, 0, asset, owner);
		this.maccLender = maccGrant;
		this.maccDebtor = maccRcv;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
