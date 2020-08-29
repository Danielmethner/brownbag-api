package com.brownbag_api.model.enums;

public enum EDataKind {
	MASTER_DATA("Master DAta"), TRX_DATA("Transaction Data");

	public final String name;

	private EDataKind(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
