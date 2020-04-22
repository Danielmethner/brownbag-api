package com.brownbag_api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.brownbag_api.model.data.EAssetGrp;

@Entity
@Table(name = "ASSET_LOAN")
@Inheritance(strategy = InheritanceType.JOINED)
public class AssetLoan extends Asset implements Serializable {

	private static final long serialVersionUID = -8337101973240362473L;

	@Column(name = "MAT_DATE")
	private Date matDate;

	@NotNull
	@Column(name = "INTR_RATE")
	private double intrRate;

	public AssetLoan() {
	}

	public AssetLoan(Asset asset) {
		super(asset.getName(), null, asset.getAssetGrp(), asset.getIssuer(), 1);
		this.intrRate = 0;
	}

	public AssetLoan(@NotBlank @Size(max = 50) String name, EAssetGrp eAssetGrp, @NotNull Party issuer, Date matDate,
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
