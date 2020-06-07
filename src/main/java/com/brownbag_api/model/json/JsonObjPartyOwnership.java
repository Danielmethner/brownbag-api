package com.brownbag_api.model.json;

import com.brownbag_api.model.jpa.ObjPosStex;

public class JsonObjPartyOwnership {

	private Long id;
	private String name;
	private Long partyId;
	private String partyName;
	private int qty;
	private double ownershipPerc;

	public JsonObjPartyOwnership(ObjPosStex objPosStex) {
		this.id = objPosStex.getId();
		this.name = objPosStex.getName();
		this.partyId = objPosStex.getParty().getId();
		this.partyName = objPosStex.getParty().getName();
		this.qty = (int) objPosStex.getQty();
		this.ownershipPerc = Math.round((objPosStex.getQty() / objPosStex.getAsset().getTotalShares()) * 100d) / 100d;
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

	public Long getPartyId() {
		return partyId;
	}

	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getOwnershipPerc() {
		return ownershipPerc;
	}

	public void setOwnershipPerc(double ownershipPerc) {
		this.ownershipPerc = ownershipPerc;
	}

}
