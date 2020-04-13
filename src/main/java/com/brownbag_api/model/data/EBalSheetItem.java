package com.brownbag_api.model.data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum EBalSheetItem {
	CASH("Cash", EBalSheetSection.ASSETS), STOCKS("Stocks", EBalSheetSection.ASSETS),
	LOANS("Loans", EBalSheetSection.LIABILITIES), EQUITY("Equity", EBalSheetSection.EQUITY);

	public final String name;
	public final EBalSheetSection section;

	private EBalSheetItem(String name, EBalSheetSection section) {
		this.name = name;
		this.section = section;
	}

	public static Stream<EBalSheetItem> stream() {
		return Stream.of(EBalSheetItem.values());
	}



	public String getName() {
		return name;
	}

	public EBalSheetSection getSection() {
		return section;
	}

}
