package com.brownbag_api.model.data;

public enum EIssuer {
	ISS_CENTRAL_BANK("Central Bank"), ISS_DEUTSCHE_BANK("Deutsche Bank"), ISS_GOVERNMENT("Government");
	
	public final String name;

	private EIssuer(String name) {
		this.name = name;
	}
}
