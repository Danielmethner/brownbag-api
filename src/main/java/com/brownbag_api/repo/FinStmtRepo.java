package com.brownbag_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.enums.EFinStmtType;
import com.brownbag_api.model.jpa.ObjFinStmt;
import com.brownbag_api.model.jpa.ObjParty;

@Repository
public interface FinStmtRepo extends JpaRepository<ObjFinStmt, Long> {
	ObjFinStmt findByPartyAndFinYearAndFinStmtType(ObjParty party, int finYear, EFinStmtType balSheet);
}