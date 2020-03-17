package com.brownbag_api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "ASSET")
public class Asset implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotBlank
	@Size(max = 50)
	private String name;

	@NotNull
	@Column(name = "IS_SHARE", columnDefinition = "tinyint default false")
	private boolean isShare = true;
	
	@NotNull
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "ISSUER_ID")
	@JsonBackReference
	private User issuer;

	// CONSTRUCTOR
	public Asset(String name, boolean isShare, User issuer) {
		this.name = name;
		this.isShare = isShare;
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

	public boolean isShare() {
		return isShare;
	}

	public void setShare(boolean isShare) {
		this.isShare = isShare;
	}

	public User getIssuer() {
		return issuer;
	}

	public void setIssuer(User issuer) {
		this.issuer = issuer;
	}
	
	
}
