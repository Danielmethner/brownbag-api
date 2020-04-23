package com.brownbag_api.model.enums;

import java.util.stream.Stream;

public enum EBalSheetItemType {
	// ASSETS
	CASH("Cash", EBalSheetSectionType.ASSETS), STOCKS("Stocks", EBalSheetSectionType.ASSETS),
	LOANS_ASSET("Loans granted", EBalSheetSectionType.ASSETS),
	// FINANCING: LIABILITIES + EQUITY
	LOANS_LIAB("Loans raised", EBalSheetSectionType.LIABILITIES), EQUITY("Equity", EBalSheetSectionType.EQUITY);

	public final String name;
	public final EBalSheetSectionType section;

	private EBalSheetItemType(String name, EBalSheetSectionType section) {
		this.name = name;
		this.section = section;
	}

	public static Stream<EBalSheetItemType> stream() {
		return Stream.of(EBalSheetItemType.values());
	}

	public String getName() {
		return name;
	}

	public EBalSheetSectionType getSection() {
		return section;
	}

}
