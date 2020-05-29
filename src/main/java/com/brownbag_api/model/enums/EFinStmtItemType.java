package com.brownbag_api.model.enums;

import java.util.stream.Stream;

public enum EFinStmtItemType {
	// ----------------------------------------------------------
	// BALANCE SHEET
	// ----------------------------------------------------------

	// ASSETS
	CASH("Cash", EFinStmtSectionType.ASSETS, EFinStmtType.BAL_SHEET),
	STOCKS("Stocks", EFinStmtSectionType.ASSETS, EFinStmtType.BAL_SHEET),
	LOANS_ASSET("Loans granted", EFinStmtSectionType.ASSETS, EFinStmtType.BAL_SHEET),

	// FINANCING: 
	// - LIABILITIES
	LOANS_LIAB("Loans raised", EFinStmtSectionType.LIABILITIES, EFinStmtType.BAL_SHEET),
	// - EQUITY
	EQUITY("Equity", EFinStmtSectionType.EQUITY, EFinStmtType.BAL_SHEET),

	// ----------------------------------------------------------
	// INCOME STATEMENT
	// ----------------------------------------------------------
	
	// OPERATIONS
	REV("Revenue", EFinStmtSectionType.OPERATIONS, EFinStmtType.INCOME_STMT),
	COGS("Cost of goods sold", EFinStmtSectionType.OPERATIONS, EFinStmtType.INCOME_STMT),
	
	// FINANCIAL ACTIVITIES
	INVEST("Investments", EFinStmtSectionType.FIN_ACTIVITIES, EFinStmtType.INCOME_STMT),
	INTR("Interest", EFinStmtSectionType.FIN_ACTIVITIES, EFinStmtType.INCOME_STMT);
	
	public final String name;
	public final EFinStmtSectionType section;
	private final EFinStmtType finStmtType;

	private EFinStmtItemType(String name, EFinStmtSectionType section, EFinStmtType finStmtType) {
		this.name = name;
		this.section = section;
		this.finStmtType = finStmtType;
	}

	public static Stream<EFinStmtItemType> stream() {
		return Stream.of(EFinStmtItemType.values());
	}

	public String getName() {
		return name;
	}

	public EFinStmtSectionType getSection() {
		return section;
	}

	public EFinStmtType getFinStmtType() {
		return finStmtType;
	}

}
