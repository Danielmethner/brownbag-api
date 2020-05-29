package com.brownbag_api.model.enums;

import java.util.stream.Stream;

public enum EFinStmtItemType {
	// ASSETS
	CASH("Cash", EFinStmtSectionType.ASSETS), STOCKS("Stocks", EFinStmtSectionType.ASSETS),
	LOANS_ASSET("Loans granted", EFinStmtSectionType.ASSETS),
	// FINANCING: LIABILITIES + EQUITY
	LOANS_LIAB("Loans raised", EFinStmtSectionType.LIABILITIES), EQUITY("Equity", EFinStmtSectionType.EQUITY);

	public final String name;
	public final EFinStmtSectionType section;

	private EFinStmtItemType(String name, EFinStmtSectionType section) {
		this.name = name;
		this.section = section;
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

}
