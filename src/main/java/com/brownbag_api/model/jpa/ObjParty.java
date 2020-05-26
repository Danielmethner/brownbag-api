package com.brownbag_api.model.jpa;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.brownbag_api.model.enums.ELegalForm;
import com.brownbag_api.model.enums.EPartyType;

@Entity
@Table(name = "OBJ_PARTY")
@Inheritance(strategy = InheritanceType.JOINED)
public class ObjParty implements Serializable {

	private static final long serialVersionUID = -8337101973240362473L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotBlank
	@Size(max = 50)
	@Column(name = "name", unique = true)
	private String name;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private EPartyType partyType;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private ELegalForm legalForm;

	@NotNull
	@ManyToOne(targetEntity = ObjUser.class)
	@JoinColumn(name = "USER_ID")
	private ObjUser user;

	@OneToOne(targetEntity = ObjAsset.class)
	@JoinColumn(name = "ASSET_ID")
	private ObjAsset asset;
	
	@Column(name = "FOUNDING_DATE")
	private LocalDateTime  foundingDate;

	// CONSTRUCTOR
	public ObjParty() {
	}

	public ObjParty(@NotBlank @Size(max = 50) String name, @NotNull EPartyType partyType, @NotNull ELegalForm legalForm,
			@NotNull ObjUser user, LocalDateTime  foundingDate) {
		super();
		this.name = name;
		this.partyType = partyType;
		this.legalForm = legalForm;
		this.user = user;
		this.foundingDate = foundingDate;
	}

	public ObjParty(Long id) {
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

	public ObjUser getUser() {
		return user;
	}

	public void setUser(ObjUser user) {
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

	public EPartyType getPartyType() {
		return partyType;
	}

	public void setPartyType(EPartyType partyType) {
		this.partyType = partyType;
	}

	public ELegalForm getLegalForm() {
		return legalForm;
	}

	public void setLegalForm(ELegalForm legalForm) {
		this.legalForm = legalForm;
	}

	public ObjAsset getAsset() {
		return asset;
	}

	public void setAsset(ObjAsset asset) {
		this.asset = asset;
	}

	public LocalDateTime  getFoundingDate() {
		return foundingDate;
	}

	public void setFoundingDate(LocalDateTime  foundingDate) {
		this.foundingDate = foundingDate;
	}

	
}
