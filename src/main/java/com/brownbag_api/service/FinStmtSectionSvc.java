package com.brownbag_api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.EFinStmtItemType;
import com.brownbag_api.model.enums.EFinStmtSectionType;
import com.brownbag_api.model.enums.EFinStmtType;
import com.brownbag_api.model.jpa.ObjFinStmt;
import com.brownbag_api.model.jpa.ObjFinStmtItem;
import com.brownbag_api.model.jpa.ObjFinStmtSection;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.json.JsonObjFinStmtItem;
import com.brownbag_api.model.json.JsonObjFinStmtSection;
import com.brownbag_api.repo.FinStmtSectionRepo;

@Service
public class FinStmtSectionSvc {

	@Autowired
	private FinStmtSectionRepo balSheetSectionRepo;

	@Autowired
	private FinStmtItemSvc balSheetItemSvc;

	@Autowired
	private FinStmtSvc balSheetSvc;

	@Autowired
	private ControlSvc controlSvc;

	public List<EFinStmtItemType> getItemsBySection(EFinStmtSectionType section) {
		List<EFinStmtItemType> items;
		Stream<EFinStmtItemType> sItems;
		sItems = EFinStmtItemType.stream().filter(item -> item.getSection().equals(section));
		items = sItems.collect(Collectors.toList());
		return items;
	}

	public ObjFinStmtSection createFinStmtSection(ObjFinStmt balSheet, EFinStmtSectionType eBalSheetSection,
			EFinStmtType finStmtType) {

		double qty = 0;

		// IF BALANCE SHEET: GET LAST YEARS BALANCE SHEET
		if (finStmtType == EFinStmtType.BAL_SHEET) {

			ObjFinStmt balSheetPrevYear = balSheetSvc.getFinStmt(balSheet.getParty(), controlSvc.getFinYear() - 1,
					finStmtType);
			if (balSheetPrevYear != null) {
				ObjFinStmtSection balSheetSectionPrevYear = getByBalSheetAndSectionType(balSheetPrevYear, eBalSheetSection);

				if (balSheetSectionPrevYear != null) {
					qty = balSheetSectionPrevYear.getQty();
				}
			}
		}

		ObjFinStmtSection balSheetSection = new ObjFinStmtSection(balSheet, eBalSheetSection, qty, finStmtType, balSheet.getParty());
		ObjFinStmtSection balSheetSectionDb = balSheetSectionRepo.save(balSheetSection);
		List<EFinStmtItemType> items = getItemsBySection(eBalSheetSection);
		items.forEach(eBalSheetItem -> {
			balSheetItemSvc.createItem(eBalSheetItem, balSheetSectionDb, balSheet.getFinYear(), balSheet.getParty(),
					balSheetSection.getFinStmtType());
		});

		return balSheetSection;
	}

	public ObjFinStmtSection save(ObjFinStmtSection bss) {
		return balSheetSectionRepo.save(bss);

	}

	public ObjFinStmtSection getByBalSheetAndSectionType(ObjFinStmt balSheet, EFinStmtSectionType sectionType) {
		ObjFinStmtSection section = balSheetSectionRepo.findByFinStmtAndSection(balSheet, sectionType);
		return section;
	}
	
	public ObjFinStmtSection getCurrentByPartyIdAndSectionType(ObjParty objParty, EFinStmtSectionType sectionType) {
		int finYear = controlSvc.getFinYear();
		ObjFinStmtSection section = getByPartyIdAndSectionTypeAndFinYear(objParty, sectionType, finYear);
		return section;
	}
	
	public ObjFinStmtSection getByPartyIdAndSectionTypeAndFinYear(ObjParty objParty, EFinStmtSectionType sectionType, int finYear) {
		ObjFinStmtSection section = balSheetSectionRepo.findByPartyAndSectionAndFinYear(objParty, sectionType, finYear);
		return section;
	}

	private List<JsonObjFinStmtItem> jpaToJson(List<ObjFinStmtItem> jpaFinStmtItemList) {
		List<JsonObjFinStmtItem> jsonFinStmtItemList = new ArrayList<JsonObjFinStmtItem>();
		for (ObjFinStmtItem jpaFinStmtItem : jpaFinStmtItemList) {
			JsonObjFinStmtItem jsonFinStmtItem = new JsonObjFinStmtItem(jpaFinStmtItem);
			jsonFinStmtItemList.add(jsonFinStmtItem);
		}
		return jsonFinStmtItemList;
	}

	public JsonObjFinStmtSection getByBalSheetAndSectionJson(ObjFinStmt balSheet, EFinStmtSectionType sectionType) {
		ObjFinStmtSection section = getByBalSheetAndSectionType(balSheet, sectionType);
		if (section == null) {
			return null;
		}
		JsonObjFinStmtSection jsonObjBalSheetSection = new JsonObjFinStmtSection(section);
		List<ObjFinStmtItem> jpaItemList = balSheetItemSvc.getByBalSheetSection(section);
		List<JsonObjFinStmtItem> jsonItemList = jpaToJson(jpaItemList);
		jsonObjBalSheetSection.setItems(jsonItemList);

		return jsonObjBalSheetSection;
	}

	public ObjFinStmtSection getByBalSheetAndSectionAndFinYear(ObjFinStmt balSheet, EFinStmtSectionType sectionType,
			int finYear) {
		ObjFinStmtSection section = balSheetSectionRepo.findByFinStmtAndSectionAndFinYear(balSheet, sectionType,
				finYear);
		return section;
	}
}
