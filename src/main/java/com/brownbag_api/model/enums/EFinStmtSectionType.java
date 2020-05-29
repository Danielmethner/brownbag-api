package com.brownbag_api.model.enums;

public enum EFinStmtSectionType {
	ASSETS("Assets", EFinStmtType.BAL_SHEET, "Total Assets"),
	LIABILITIES("Liabilities", EFinStmtType.BAL_SHEET, "Total Liabilities"),
	EQUITY("Equity", EFinStmtType.BAL_SHEET, "Total Equity"),
	OPERATIONS("Operations", EFinStmtType.BAL_SHEET, "Gross Profit"),
	FIN_ACTIVITIES("Financial Activities", EFinStmtType.BAL_SHEET, "Net Profit"),;

	private final String name;
	private final EFinStmtType finStmtType;
	private final String totalCaption;

	private EFinStmtSectionType(String name, EFinStmtType finStmtType, String totalCaption) {
		this.name = name;
		this.finStmtType = finStmtType;
		this.totalCaption = totalCaption;
	}

	public String getName() {
		return name;
	}

	public EFinStmtType getFinStmtType() {
		return finStmtType;
	}

	public String getTotalCaption() {
		return totalCaption;
	}

}
