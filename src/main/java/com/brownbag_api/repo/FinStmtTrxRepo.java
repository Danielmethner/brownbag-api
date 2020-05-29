package com.brownbag_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.jpa.FinStmtTrx;

@Repository
public interface FinStmtTrxRepo extends JpaRepository<FinStmtTrx, Long> {

}