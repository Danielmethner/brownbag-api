package com.brownbag_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.enums.EBalSheetSectionType;
import com.brownbag_api.model.jpa.ObjBalSheet;
import com.brownbag_api.model.jpa.ObjBalSheetSection;

@Repository
public interface BalSheetSectionRepo extends JpaRepository<ObjBalSheetSection, Long> {

	ObjBalSheetSection findByBalSheetAndSection(ObjBalSheet balSheet, EBalSheetSectionType section);
	
	ObjBalSheetSection findByBalSheetAndSectionAndFinYear(ObjBalSheet balSheet, EBalSheetSectionType section, int finYear);

}