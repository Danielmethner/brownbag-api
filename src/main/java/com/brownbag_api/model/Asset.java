package com.brownbag_api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import com.brownbag_api.model.data.EAssetGrp;

@Entity
@Table(name = "ASSET")
@Inheritance(strategy = InheritanceType.JOINED)
public class Asset implements Serializable {

	private static final long serialVersionUID = -8337101973240362473L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotBlank
	@Size(max = 50)
	private String name;

	@Column(name = "MAT_DATE")
	private Date matDate;

	@NotNull
	@Column(name = "INTR_RATE")
	private double intrRate;

	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	private EAssetGrp assetGrp;

	@NotNull
	@ManyToOne(targetEntity = Party.class)
	@JoinColumn(name = "ISSUER_ID")
	private Party issuer;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP", updatable = false)
	private Date timestampCreate;

	public Asset(@NotBlank @Size(max = 50) String name, EAssetGrp eAssetGrp, @NotNull Party issuer) {
		super();
		this.name = name;
		this.assetGrp = eAssetGrp;
		this.issuer = issuer;
	}

	public Asset() {
	}

	public Asset(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Party getIssuer() {
		return issuer;
	}

	public void setIssuer(Party issuer) {
		this.issuer = issuer;
	}

	public EAssetGrp geteAssetGrp() {
		return assetGrp;
	}

	public void seteAssetGrp(EAssetGrp eAssetGrp) {
		this.assetGrp = eAssetGrp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public EAssetGrp getAssetGrp() {
		return assetGrp;
	}

	public void setAssetGrp(EAssetGrp assetGrp) {
		this.assetGrp = assetGrp;
	}

	public Date getTimestampCreate() {
		return timestampCreate;
	}

	public void setTimestampCreate(Date timestampCreate) {
		this.timestampCreate = timestampCreate;
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

}
