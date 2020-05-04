package com.brownbag_api.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EBalSheetItemType;
import com.brownbag_api.model.enums.EBalSheetSectionType;
import com.brownbag_api.model.jpa.ObjBalSheet;
import com.brownbag_api.model.jpa.ObjBalSheetItem;
import com.brownbag_api.model.jpa.ObjBalSheetSection;
import com.brownbag_api.model.json.JsonObjBalSheetSection;
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

	public ObjBalSheetSection createBalSheetSection(ObjBalSheet balSheet, EBalSheetSectionType eBalSheetSection) {
		ObjBalSheetSection balSheetSection = new ObjBalSheetSection(balSheet, eBalSheetSection, 0);
		ObjBalSheetSection balSheetSectionDb = balSheetSectionRepo.save(balSheetSection);
		List<EBalSheetItemType> items = getItemsBySection(eBalSheetSection);
		items.forEach(eBalSheetItem -> {
			balSheetItemSvc.createItem(eBalSheetItem, balSheetSectionDb, balSheet.getFinYear(), balSheet.getParty());
		});
		return balSheetSection;
	}

	public ObjBalSheetSection save(ObjBalSheetSection bss) {
		return balSheetSectionRepo.save(bss);

	}

	public JsonObjBalSheetSection getByBalSheetAndSection(ObjBalSheet balSheet, EBalSheetSectionType sectionType) {
		ObjBalSheetSection section = balSheetSectionRepo.findByBalSheetAndSection(balSheet, sectionType);
		JsonObjBalSheetSection jsonObjBalSheetSection = new JsonObjBalSheetSection(section);
		List<ObjBalSheetItem> items = balSheetItemSvc.getByBalSheetSection(section);
		jsonObjBalSheetSection.setItems(items);

		return jsonObjBalSheetSection;
	}
}
