package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.BalSheet;
import com.brownbag_api.model.BalSheetItem;
import com.brownbag_api.model.BalSheetSectionType;
import com.brownbag_api.model.Booking;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.data.EBalSheetItemType;

@Repository
public interface BalSheetItemRepo extends JpaRepository<BalSheetItem, Long> {

	BalSheetItem findByPartyAndFinYearAndItemType(Party party, int finYear, EBalSheetItemType eBalSheetItem);
}