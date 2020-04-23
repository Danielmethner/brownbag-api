package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.Party;
import com.brownbag_api.model.User;
import com.brownbag_api.model.enums.EPartyType;

@Repository
public interface PartyRepo extends JpaRepository<Party, Long> {

	Party findByName(String name);

	List<Party> findByUserAndPartyType(User user, EPartyType ePartyType);

}