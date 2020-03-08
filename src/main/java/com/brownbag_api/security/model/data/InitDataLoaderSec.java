package com.brownbag_api.security.model.data;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brownbag_api.security.model.ERole;
import com.brownbag_api.security.model.Role;
import com.brownbag_api.security.repo.RoleRepo;
import com.brownbag_api.security.repo.UserRepo;
import com.brownbag_api.security.svc.UserSvc;

@Component
public class InitDataLoaderSec {

	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private UserSvc userSvc;

	private void createRole(ERole eRole) {
		
		if(!roleRepo.existsByName(eRole)) {
			Role role = new Role(eRole);
			roleRepo.save(role);
		}

	}

	public void createRoles() {
		createRole(ERole.ROLE_BROKER);
		createRole(ERole.ROLE_TRADER);
	}
	
	public void createUser(String userName, String password, ERole eRole) {
		Set<String> roles = new HashSet<>();
		roles.add(eRole.toString());
		userSvc.registerUser(userName, password, roles);
	}
	
	public void createUsers() {
		createUser("broker", "broker", ERole.ROLE_BROKER);
		createUser("trader1", "trader1", ERole.ROLE_TRADER);
	}
}