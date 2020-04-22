package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.Party;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.PosMacc;

@Repository
public interface PosMaccRepo extends JpaRepository<PosMacc, Long> {

	List<PosMacc> findByParty(Party party);

}