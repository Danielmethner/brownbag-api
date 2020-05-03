package com.brownbag_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.ObjUser;

@Repository
public interface UserRepo extends JpaRepository<ObjUser, Long> {
	ObjUser findByUsername(String username);

	Boolean existsByUsername(String username);

}