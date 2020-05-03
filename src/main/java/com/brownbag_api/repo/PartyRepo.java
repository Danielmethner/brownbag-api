package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.enums.EPartyType;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjUser;

@Repository
public interface PartyRepo extends JpaRepository<ObjParty, Long> {

	ObjParty findByName(String name);

	List<ObjParty> findByUserAndPartyType(ObjUser user, EPartyType ePartyType);

}