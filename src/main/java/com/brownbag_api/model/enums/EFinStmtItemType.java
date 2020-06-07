package com.brownbag_api.model.enums;

import java.util.stream.Stream;

public enum EFinStmtItemType {
	// ----------------------------------------------------------
	// BALANCE SHEET
	// ----------------------------------------------------------

	// ASSETS
	BAL_A_CASH("Cash", EFinStmtSectionType.ASSETS, EFinStmtType.BAL_SHEET),
	BAL_A_STOCKS("Stocks", EFinStmtSectionType.ASSETS, EFinStmtType.BAL_SHEET),
	BAL_A_LOANS("Loans granted", EFinStmtSectionType.ASSETS, EFinStmtType.BAL_SHEET),

	// FINANCING:
	// - LIABILITIES
	BAL_L_LOANS("Loans raised", EFinStmtSectionType.LIABILITIES, EFinStmtType.BAL_SHEET),
	BAL_L_BONDS("Bonds issued", EFinStmtSectionType.LIABILITIES, EFinStmtType.BAL_SHEET),
	// - EQUITY
	BAL_E_RET_EARN("Retained Earnings", EFinStmtSectionType.EQUITY, EFinStmtType.BAL_SHEET),
	BAL_E_CAPITAL_STOCK("Paid in Capital", EFinStmtSectionType.EQUITY, EFinStmtType.BAL_SHEET),

	// ----------------------------------------------------------
	// INCOME STATEMENT
	// ----------------------------------------------------------

	// OPERATIONS
	PL_REV("Revenue", EFinStmtSectionType.OPERATIONS, EFinStmtType.INCOME_STMT),
	PL_COGS("Cost of goods sold", EFinStmtSectionType.OPERATIONS, EFinStmtType.INCOME_STMT),

	// FINANCIAL ACTIVITIES
	PL_INVEST("Investments", EFinStmtSectionType.FIN_ACTIVITIES, EFinStmtType.INCOME_STMT),
	PL_INTR("Interest", EFinStmtSectionType.FIN_ACTIVITIES, EFinStmtType.INCOME_STMT);

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
