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
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "OBJ_POS_MACC")
public class ObjPosMacc extends ObjPos implements Serializable {

	private static final long serialVersionUID = 1530699992135610397L;

	public ObjPosMacc() {
	}

	public ObjPosMacc(@NotNull double qty, @NotNull ObjAsset assetCurry, @NotNull ObjParty owner) {
		super(qty, 0, assetCurry, owner);
	}


}
