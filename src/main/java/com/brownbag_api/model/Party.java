package com.brownbag_api.model;

import java.io.Serializable;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.brownbag_api.model.data.EPartyType;

@Entity
@Table(name = "PARTY")
@Inheritance(strategy = InheritanceType.JOINED)
public class Party implements Serializable {

	private static final long serialVersionUID = -8337101973240362473L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotBlank
	@Size(max = 50)
	private String name;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private EPartyType partyType;

	@NotNull
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "USER_ID")
	private User user;

	// CONSTRUCTOR
	public Party(String name, User user, EPartyType legalEntityType) {
		this.name = name;
		this.user = user;
		this.partyType = legalEntityType;
	}

	public Party() {
	}

	public Party(Long id) {
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public EPartyType getLegalEntityType() {
		return partyType;
	}

	public void setLegalEntityType(EPartyType legalEntityType) {
		this.partyType = legalEntityType;
	}

}