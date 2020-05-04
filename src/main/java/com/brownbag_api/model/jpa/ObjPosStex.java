package com.brownbag_api.model.jpa;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "OBJ_POS_STEX")
public class ObjPosStex extends ObjPos implements Serializable {

	private static final long serialVersionUID = 1L;

	public ObjPosStex() {
	}

	public ObjPosStex(@NotNull double qty, @NotNull double qtyBlocked, @NotNull ObjAsset asset, @NotNull ObjParty owner,
			@NotNull double priceAvg) {
		super(qty, qtyBlocked, asset, owner, priceAvg);

	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
