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

	@NotNull
	@Column(name = "IS_CURRY", columnDefinition = "tinyint default false")
	private boolean isCurry = false;

	@NotNull
	@ManyToOne(targetEntity = Party.class)
	@JoinColumn(name = "ISSUER_ID")
	private Party issuer;

	// CONSTRUCTOR
	public Asset(String name, boolean isShare, Party issuer) {
		this.name = name;
		this.isCurry = isShare;
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

	public boolean isCurry() {
		return isCurry;
	}

	public void setCurry(boolean isCurry) {
		this.isCurry = isCurry;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
