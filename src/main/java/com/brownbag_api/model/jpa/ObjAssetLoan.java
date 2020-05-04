package com.brownbag_api.model.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.brownbag_api.model.enums.EAssetGrp;

@Entity
@Table(name = "OBJ_ASSET_LOAN")
@Inheritance(strategy = InheritanceType.JOINED)
public class ObjAssetLoan extends ObjAsset implements Serializable {

	private static final long serialVersionUID = -8337101973240362473L;

	@Column(name = "MAT_DATE")
	private Date matDate;

	@NotNull
	@Column(name = "INTR_RATE")
	private double intrRate;

	public ObjAssetLoan() {
	}

	public ObjAssetLoan(ObjAsset asset) {
		super(asset.getName(), null, asset.getAssetGrp(), asset.getIssuer(), 1);
		this.intrRate = 0;
	}

	public ObjAssetLoan(@NotBlank String name, EAssetGrp eAssetGrp, @NotNull ObjParty issuer, Date matDate,
			@NotNull double intrRate) {
		super(name, null, eAssetGrp, issuer, 1);
		this.matDate = matDate;
		this.intrRate = intrRate;
	}

	public Date getMatDate() {
		return matDate;
	}

	public void setMatDate(Date matDate) {
		this.matDate = matDate;
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

}
