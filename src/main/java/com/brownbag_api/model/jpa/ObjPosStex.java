package com.brownbag_api.model.jpa;

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
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
