package com.brownbag_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.ObjBalSheet;
import com.brownbag_api.model.ObjParty;

@Repository
public interface BalSheetRepo extends JpaRepository<ObjBalSheet, Long> {
	ObjBalSheet findByPartyAndFinYear(ObjParty party, int finYear);
}