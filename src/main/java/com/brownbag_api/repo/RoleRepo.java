package com.brownbag_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.ObjRole;

@Repository
public interface RoleRepo extends JpaRepository<ObjRole, Long> {
	ObjRole findByName(String strRole);

	boolean existsByName(String strRole);
}
