package com.brownbag_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.ObjBalSheet;
import com.brownbag_api.model.ObjBalSheetSection;
import com.brownbag_api.model.enums.EBalSheetSectionType;

@Repository
public interface BalSheetSectionRepo extends JpaRepository<ObjBalSheetSection, Long> {

	ObjBalSheetSection findByBalSheetAndSection(ObjBalSheet balSheet, EBalSheetSectionType section);

}