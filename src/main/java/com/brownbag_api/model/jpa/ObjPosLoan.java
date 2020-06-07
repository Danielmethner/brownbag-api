package com.brownbag_api.model.jpa;

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
	private @NotNull ObjPosMacc maccLender;

	@NotNull
	@OneToOne(targetEntity = ObjPos.class)
	@JoinColumn(name = "MACC_DEBTOR_ID")
	private ObjPosMacc maccDebtor;

	public ObjPosLoan() {
	}

	public ObjPosLoan(@NotNull double qty, @NotNull ObjAsset asset, @NotNull ObjParty owner,
			@NotNull ObjPosMacc maccGrant, @NotNull ObjPosMacc maccRcv) {
		super(qty, 0, asset, owner, 1);
		this.maccLender = maccGrant;
		this.maccDebtor = maccRcv;
	}

	public @NotNull ObjPosMacc getMaccLender() {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
