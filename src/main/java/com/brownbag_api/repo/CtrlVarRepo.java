package com.brownbag_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.jpa.CtrlVar;

@Repository
public interface CtrlVarRepo extends JpaRepository<CtrlVar, Long> {

	CtrlVar findByKey(String key);

}