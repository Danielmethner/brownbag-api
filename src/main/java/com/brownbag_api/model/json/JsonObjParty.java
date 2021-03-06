package com.brownbag_api.model.json;

import com.brownbag_api.model.enums.ELegalForm;
import com.brownbag_api.model.enums.EPartyType;
import com.brownbag_api.model.jpa.ObjParty;

public class JsonObjParty {

	private Long id;
	private String name;
	private String technicalName;
	private EPartyType partyType;
	private String partyTypeName;
	private ELegalForm legalForm;
	private String legalFormName;
	private Long userId;
	private String userName;
	private Long ownerPartyId;
	private Long ownerPartyName;
	private Long assetId;
	private String assetName;
	private int assetShareQty;
	private double shareCapital;

	// CREATE PARTY
	public JsonObjParty(String name, ELegalForm legalForm, int assetShareQty, double shareCapital) {
		super();
		this.name = name;
		this.legalForm = legalForm;
		this.assetShareQty = assetShareQty;
		this.shareCapital = shareCapital;
	}

	// SHOW PARTY
	public JsonObjParty(ObjParty jpaParty) {
		this.id = jpaParty.getId();
		this.name = jpaParty.getName();
		String legalFormSuffix = jpaParty.getLegalForm() == null ? "" : " " + jpaParty.getLegalForm().getAbbr();
		this.technicalName = jpaParty.getName() + legalFormSuffix;
		this.partyType = jpaParty.getPartyType();
		this.partyTypeName = this.partyType == null ? null : this.partyType.getName();
		this.legalForm = jpaParty.getLegalForm();
		this.legalFormName = this.legalForm == null ? null : this.legalForm.getName();
		this.userId = jpaParty.getUser().getId();
		this.userName = jpaParty.getUser().getName();
		if (jpaParty.getAsset() != null) {
			this.assetId = jpaParty.getAsset().getId();
			this.assetName = jpaParty.getAsset().getName();
			this.setAssetShareQty(jpaParty.getAsset().getTotalShares());
		}
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public int getAssetShareQty() {
		return assetShareQty;
	}

	public void setAssetShareQty(int assetShareQty) {
		this.assetShareQty = assetShareQty;
	}

	public Long getOwnerPartyId() {
		return ownerPartyId;
	}

	public void setOwnerPartyId(Long ownerPartyId) {
		this.ownerPartyId = ownerPartyId;
	}

	public Long getOwnerPartyName() {
		return ownerPartyName;
	}

	public void setOwnerPartyName(Long ownerPartyName) {
		this.ownerPartyName = ownerPartyName;
	}

	public double getShareCapital() {
		return shareCapital;
	}

	public void setShareCapital(double shareCapital) {
		this.shareCapital = shareCapital;
	}

	public String getTechnicalName() {
		return technicalName;
	}

	public void setTechnicalName(String technicalName) {
		this.technicalName = technicalName;
	}

	public String getPartyTypeName() {
		return partyTypeName;
	}

	public void setPartyTypeName(String partyTypeName) {
		this.partyTypeName = partyTypeName;
	}

	public String getLegalFormName() {
		return legalFormName;
	}

	public void setLegalFormName(String legalFormName) {
		this.legalFormName = legalFormName;
	}

}
