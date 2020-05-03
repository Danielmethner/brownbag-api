package com.brownbag_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.enums.EBalSheetItemType;
import com.brownbag_api.model.jpa.ObjBalSheetItem;
import com.brownbag_api.model.jpa.ObjParty;

@Repository
public interface BalSheetItemRepo extends JpaRepository<ObjBalSheetItem, Long> {

	ObjBalSheetItem findByPartyAndFinYearAndItemType(ObjParty party, int finYear, EBalSheetItemType eBalSheetItem);
}