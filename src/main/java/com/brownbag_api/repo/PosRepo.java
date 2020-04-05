package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.LegalEntity;
import com.brownbag_api.model.Position;
import com.brownbag_api.model.User;

@Repository
public interface PosRepo extends JpaRepository<Position, Long> {

	List<Position> findByOwner(LegalEntity owner);
	
	List<Position> findByOwnerAndIsMacc(LegalEntity owner, boolean isMacc);
	
}