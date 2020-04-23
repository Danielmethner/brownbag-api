package com.brownbag_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.BalSheet;
import com.brownbag_api.model.BalSheetSection;
import com.brownbag_api.model.enums.EBalSheetSectionType;

@Repository
public interface BalSheetSectionRepo extends JpaRepository<BalSheetSection, Long> {

	BalSheetSection findByBalSheetAndSection(BalSheet balSheet, EBalSheetSectionType section);

}