package com.brownbag_api.model.enums;

public enum ECtrlVar {
	FIN_DATE("Financial Date", EDataType.DATE), DEMO_DATA_CREATED("Demo Data Created", EDataType.BOOL),
	NATP_INIT_DEPOSIT_AMT("Natural Person Initial Deposit Amount", EDataType.DOUBLE),
	NATP_INIT_DEPOSIT_DURATION("Natural Person Initial Deposit Duration", EDataType.DOUBLE),
	NATP_INIT_DEPOSIT_INTR_RATE("Base Interest Rate", EDataType.DOUBLE);

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