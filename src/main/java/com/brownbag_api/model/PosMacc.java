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
@Table(name = "POS_MACC")
public class PosMacc extends Pos implements Serializable {

	private static final long serialVersionUID = 1530699992135610397L;

	public PosMacc() {
	}

	public PosMacc(@NotNull double qty, @NotNull Asset assetCurry, @NotNull Party owner) {
		super(qty, 0, assetCurry, owner);
	}

}
