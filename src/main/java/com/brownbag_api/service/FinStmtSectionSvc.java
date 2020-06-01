package com.brownbag_api.service;

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

			ObjFinStmt balSheetPrevYear = balSheetSvc.getFinStmt(balSheet.getParty(), controlSvc.getFinYear() - 1, finStmtType);
			if (balSheetPrevYear != null) {
				ObjFinStmtSection balSheetSectionPrevYear = getByBalSheetAndSection(balSheetPrevYear, eBalSheetSection);

				if (balSheetSectionPrevYear != null) {
					qty = balSheetSectionPrevYear.getQty();
				}
			}
		}

		ObjFinStmtSection balSheetSection = new ObjFinStmtSection(balSheet, eBalSheetSection, qty, finStmtType);
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

	public ObjFinStmtSection getByBalSheetAndSection(ObjFinStmt balSheet, EFinStmtSectionType sectionType) {
		ObjFinStmtSection section = balSheetSectionRepo.findByFinStmtAndSection(balSheet, sectionType);
		return section;
	}

	public JsonObjFinStmtSection getByBalSheetAndSectionJson(ObjFinStmt balSheet, EFinStmtSectionType sectionType) {
		ObjFinStmtSection section = getByBalSheetAndSection(balSheet, sectionType);
		JsonObjFinStmtSection jsonObjBalSheetSection = new JsonObjFinStmtSection(section);
		List<ObjFinStmtItem> items = balSheetItemSvc.getByBalSheetSection(section);
		jsonObjBalSheetSection.setItems(items);

		return jsonObjBalSheetSection;
	}

	public ObjFinStmtSection getByBalSheetAndSectionAndFinYear(ObjFinStmt balSheet, EFinStmtSectionType sectionType,
			int finYear) {
		ObjFinStmtSection section = balSheetSectionRepo.findByFinStmtAndSectionAndFinYear(balSheet, sectionType,
				finYear);
		return section;
	}
}
