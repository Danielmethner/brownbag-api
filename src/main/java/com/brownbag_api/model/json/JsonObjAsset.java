package com.brownbag_api.model.json;

import java.util.Date;

import com.brownbag_api.model.enums.EAssetGrp;
import com.brownbag_api.model.jpa.ObjAsset;

public class JsonObjAsset {

	private double id;

	private String name;
	private String isin;
	private EAssetGrp assetGrp;
	private int totalShares;
	private Date timestampCreate;
	private double nomVal;

	public JsonObjAsset(ObjAsset jpaAsset) {
		this.id = jpaAsset.getId();
		this.name = jpaAsset.getName();
		this.isin = jpaAsset.getIsin();
		this.assetGrp = jpaAsset.getAssetGrp();
		this.totalShares = jpaAsset.getTotalShares();
		this.timestampCreate = jpaAsset.getTimestampCreate();
		this.nomVal = jpaAsset.getNomVal();
	}

	public double getId() {
		return id;
	}

	public void setId(double id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public EAssetGrp getAssetGrp() {
		return assetGrp;
	}

	public void setAssetGrp(EAssetGrp assetGrp) {
		this.assetGrp = assetGrp;
	}

	public int getTotalShares() {
		return totalShares;
	}

	public void setTotalShares(int totalShares) {
		this.totalShares = totalShares;
	}

	public Date getTimestampCreate() {
		return timestampCreate;
	}

	public void setTimestampCreate(Date timestampCreate) {
		this.timestampCreate = timestampCreate;
	}

	public double getNomVal() {
		return nomVal;
	}

	public void setNomVal(double nomVal) {
		this.nomVal = nomVal;
	}

}
