package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjPosLoan;
import com.brownbag_api.model.jpa.ObjPosMacc;

@Repository
public interface PosLoanRepo extends JpaRepository<ObjPosLoan, Long> {

	List<ObjPosLoan> findByParty(ObjParty party);

}