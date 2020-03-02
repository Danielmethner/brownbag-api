package com.brownbag_api.security.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.security.model.ERole;
import com.brownbag_api.security.model.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);

	boolean existsByName(ERole eRole);
}
