package com.brownbag_api.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.BalSheet;
import com.brownbag_api.model.BalSheetSectionType;
import com.brownbag_api.model.enums.EBalSheetItemType;
import com.brownbag_api.model.enums.EBalSheetSectionType;
import com.brownbag_api.repo.BalSheetSectionRepo;

@Service
public class BalSheetSectionSvc {

	@Autowired
	private BalSheetSectionRepo balSheetSectionRepo;

	@Autowired
	private BalSheetItemSvc balSheetItemSvc;

	public List<EBalSheetItemType> getItemsBySection(EBalSheetSectionType section) {
		List<EBalSheetItemType> items;
		Stream<EBalSheetItemType> sItems;
		sItems = EBalSheetItemType.stream().filter(item -> item.getSection().equals(section));
		items = sItems.collect(Collectors.toList());
		return items;
	}

	public BalSheetSectionType createBalSheetSection(BalSheet balSheet, EBalSheetSectionType eBalSheetSection) {
		BalSheetSectionType balSheetSection = new BalSheetSectionType(balSheet, eBalSheetSection);
		BalSheetSectionType balSheetSectionDb = balSheetSectionRepo.save(balSheetSection);
		List<EBalSheetItemType> items = getItemsBySection(eBalSheetSection);
		items.forEach(eBalSheetItem -> {
			balSheetItemSvc.createItem(eBalSheetItem, balSheetSectionDb, balSheet.getFinYear(), balSheet.getParty());
		});
		return balSheetSection;
	}
}
