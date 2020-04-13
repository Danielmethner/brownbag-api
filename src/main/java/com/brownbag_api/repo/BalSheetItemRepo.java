package com.brownbag_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.BalSheetItem;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.data.EBalSheetItemType;

@Repository
public interface BalSheetItemRepo extends JpaRepository<BalSheetItem, Long> {

	BalSheetItem findByPartyAndFinYearAndItemType(Party party, int finYear, EBalSheetItemType eBalSheetItem);
}