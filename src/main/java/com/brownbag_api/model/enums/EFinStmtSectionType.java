package com.brownbag_api.model.enums;

public enum EFinStmtSectionType {
	ASSETS("Assets", EFinStmtType.BAL_SHEET, "Total Assets", "bg-success"),
	LIABILITIES("Liabilities", EFinStmtType.BAL_SHEET, "Total Liabilities", "bg-danger"),
	EQUITY("Equity", EFinStmtType.BAL_SHEET, "Total Equity", "bg-primary"),
	OPERATIONS("Operations", EFinStmtType.INCOME_STMT, "Gross Profit", "bg-success"),
	FIN_ACTIVITIES("Financial Activities", EFinStmtType.INCOME_STMT, "Net Profit", "bg-success"),;

	private final String name;
	private final EFinStmtType finStmtType;
	private final String totalCaption;
	private final String style;

	private EFinStmtSectionType(String name, EFinStmtType finStmtType, String totalCaption, String style) {
		this.name = name;
		this.finStmtType = finStmtType;
		this.totalCaption = totalCaption;
		this.style = style;
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

	public String getStyle() {
		return style;
	}

	
}
