package com.brownbag_api.model.data;

public enum EAsset {
	EUR("Euro", EAssetGrp.CURRY, EParty.ECB), DEUTSCHE_BANK("Deutsche Bank", EAssetGrp.STOCK, EParty.DEUTSCHE_BANK),
	GOVERNMENT_BOND("Government Bond", EAssetGrp.BOND, EParty.GOVERNMENT);

	public final String name;
	public final EAssetGrp assetGrp;
	public final EParty party;

	EAsset(String name, EAssetGrp assetGrp, EParty party) {
		this.name = name;
		this.assetGrp = assetGrp;
		this.party = party;
	}

	public String getName() {
		return name;
	}

	public EAssetGrp getAssetGrp() {
		return assetGrp;
	}

	public EParty getParty() {
		return party;
	}

}