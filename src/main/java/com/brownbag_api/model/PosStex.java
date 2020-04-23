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
@Table(name = "POS_STEX")
public class PosStex extends Pos implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "QTY", columnDefinition = "Decimal(20,2)")
	private double priceAvg;

	public PosStex() {
	}

	public PosStex(@NotNull double qty, @NotNull double qtyBlocked, @NotNull Asset asset, @NotNull Party owner,
			@NotNull double priceAvg) {
		super(qty, qtyBlocked, asset, owner);
		this.priceAvg = priceAvg;
	}

	public double getPriceAvg() {
		return priceAvg;
	}

	public void setPriceAvg(double priceAvg) {
		this.priceAvg = priceAvg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
