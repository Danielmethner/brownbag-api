package com.brownbag_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brownbag_api.model.LegalEntity;

public interface OrgRepo extends JpaRepository<LegalEntity, Long> {
	LegalEntity findByName(String name);
}
