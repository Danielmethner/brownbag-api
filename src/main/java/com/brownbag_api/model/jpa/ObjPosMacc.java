package com.brownbag_api.model.jpa;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "OBJ_POS_MACC")
public class ObjPosMacc extends ObjPos implements Serializable {

	private static final long serialVersionUID = 1530699992135610397L;

	public ObjPosMacc() {
	}

	public ObjPosMacc(@NotNull double qty, @NotNull ObjAsset assetCurry, @NotNull ObjParty owner, double priceAvg) {
		super(qty, 0, assetCurry, owner, priceAvg > 0 ? priceAvg : 1);
	}

}
