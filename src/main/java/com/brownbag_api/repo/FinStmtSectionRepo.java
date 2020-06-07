package com.brownbag_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.enums.EFinStmtSectionType;
import com.brownbag_api.model.jpa.ObjFinStmt;
import com.brownbag_api.model.jpa.ObjFinStmtSection;

@Repository
public interface FinStmtSectionRepo extends JpaRepository<ObjFinStmtSection, Long> {

	ObjFinStmtSection findByFinStmtAndSection(ObjFinStmt finStmt, EFinStmtSectionType section);

	ObjFinStmtSection findByFinStmtAndSectionAndFinYear(ObjFinStmt finStmt, EFinStmtSectionType section, int finYear);

}