package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.BalSheet;
import com.brownbag_api.model.BalSheetSectionType;
import com.brownbag_api.model.Booking;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.data.EBalSheetSectionType;

@Repository
public interface BalSheetSectionRepo extends JpaRepository<BalSheetSectionType, Long> {
	
	BalSheetSectionType findByBalSheetAndSection(BalSheet balSheet, EBalSheetSectionType section);
	
}