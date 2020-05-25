package com.brownbag_api.model.jpa;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.brownbag_api.model.enums.EAssetGrp;

@Entity
@Table(name = "OBJ_ASSET_LOAN")
@Inheritance(strategy = InheritanceType.JOINED)
public class ObjAssetLoan extends ObjAsset implements Serializable {

	private static final long serialVersionUID = -8337101973240362473L;

	@Column(name = "MAT_DATE")
	private LocalDate matDate;

	@NotNull
	@Column(name = "INTR_RATE")
	private double intrRate;

	public ObjAssetLoan() {
	}

	public ObjAssetLoan(ObjAsset asset, int totalShares) {
		super(asset.getName(), null, asset.getAssetGrp(), totalShares, asset.getIssuer(), 1);
		this.intrRate = 0;
	}

	public ObjAssetLoan(@NotBlank @Size(max = 150) String name, @Size(max = 12) String isin, EAssetGrp assetGrp,
			@NotNull int totalShares, @NotNull ObjParty issuer, @NotNull double nomVal, LocalDate matDate,
			@NotNull double intrRate) {
		super(name, null, assetGrp, totalShares, issuer, nomVal);
		this.matDate = matDate;
		this.intrRate = intrRate;
	}

	public LocalDate getMatDate() {
		return matDate;
	}

	public double getIntrRate() {
		return intrRate;
	}

	public void setIntrRate(double intrRate) {
		this.intrRate = intrRate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setMatDate(LocalDate matDate) {
		this.matDate = matDate;
	}
	
	

}
