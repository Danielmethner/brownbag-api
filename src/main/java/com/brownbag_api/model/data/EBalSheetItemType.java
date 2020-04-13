package com.brownbag_api.model.data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum EBalSheetItemType {
	CASH("Cash", EBalSheetSectionType.ASSETS), STOCKS("Stocks", EBalSheetSectionType.ASSETS),
	LOANS("Loans", EBalSheetSectionType.LIABILITIES), EQUITY("Equity", EBalSheetSectionType.EQUITY);

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
