package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.Party;
import com.brownbag_api.model.Pos;

@Repository
public interface PosRepo extends JpaRepository<Pos, Long> {

	List<Pos> findByParty(Party owner);

	List<Pos> findByPartyAndIsMacc(Party party, boolean isMacc);

}