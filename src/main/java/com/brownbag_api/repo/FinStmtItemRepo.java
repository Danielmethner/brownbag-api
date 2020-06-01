package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.enums.EFinStmtItemType;
import com.brownbag_api.model.jpa.ObjFinStmtItem;
import com.brownbag_api.model.jpa.ObjFinStmtSection;
import com.brownbag_api.model.jpa.ObjParty;

@Repository
public interface FinStmtItemRepo extends JpaRepository<ObjFinStmtItem, Long> {

	ObjFinStmtItem findByPartyAndFinYearAndItemType(ObjParty party, int finYear, EFinStmtItemType eBalSheetItem);

	List<ObjFinStmtItem> findByFinStmtSection(ObjFinStmtSection section);
}