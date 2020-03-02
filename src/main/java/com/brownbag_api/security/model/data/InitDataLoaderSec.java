package com.brownbag_api.security.model.data;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brownbag_api.security.model.ERole;
import com.brownbag_api.security.model.Role;
import com.brownbag_api.security.repo.RoleRepo;

@Component
public class InitDataLoaderSec {

	@Autowired
	private RoleRepo roleRepo;
	

	private void createRole(ERole eRole) {
		
		if(!roleRepo.existsByName(eRole)) {
			Role role = new Role(eRole);
			roleRepo.save(role);
		}

	}

	public void createRoles() {
		createRole(ERole.ROLE_ADMIN);
		createRole(ERole.ROLE_MODERATOR);
		createRole(ERole.ROLE_USER);
	}
}
