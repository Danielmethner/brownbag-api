package com.brownbag_api.model.enums;

public enum ECtrlVar {
	FIN_DATE("Financial Date", EDataType.DATE), DEMO_DATA_CREATED("Demo Data Created", EDataType.BOOL);

	public final String name;
	public final EDataType dataType;

	private ECtrlVar(String name, EDataType dataType) {
		this.name = name;
		this.dataType = dataType;
	}

	public String getName() {
		return name;
	}

	public EDataType getDataType() {
		return dataType;
	}
	
	
}