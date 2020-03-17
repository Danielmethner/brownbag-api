package com.brownbag_api.security.model.data;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brownbag_api.model.data.EIssuer;
import com.brownbag_api.model.data.EUser;
import com.brownbag_api.security.model.ERole;
import com.brownbag_api.security.model.Role;
import com.brownbag_api.security.repo.RoleRepo;
import com.brownbag_api.security.svc.UserSvc;

import sun.nio.cs.ext.EUC_CN;

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
		createRole(ERole.ROLE_ISSUER);
	}
	
	public void createUser(String userName, String name, String password, ERole eRole) {
		Set<String> roles = new HashSet<>();
		roles.add(eRole.toString());
		userSvc.registerUser(userName, name, password, roles);
	}
	
	public void createUsers() {
		createUser(EUser.U_BROKER.toString(), EUser.U_BROKER.name, EUser.U_BROKER.toString(), ERole.ROLE_BROKER);
		createUser(EUser.U_TRADER_1.toString(), EUser.U_TRADER_1.name, EUser.U_TRADER_1.toString(), ERole.ROLE_TRADER);
		
		// Issuer Users
		createUser(EIssuer.ISS_CENTRAL_BANK.toString(), EIssuer.ISS_CENTRAL_BANK.name, EIssuer.ISS_CENTRAL_BANK.toString(), ERole.ROLE_ISSUER);
		createUser(EIssuer.ISS_DEUTSCHE_BANK.toString(), EIssuer.ISS_DEUTSCHE_BANK.name, EIssuer.ISS_CENTRAL_BANK.toString(), ERole.ROLE_ISSUER);
		createUser(EIssuer.ISS_GOVERNMENT.toString(), EIssuer.ISS_GOVERNMENT.name, EIssuer.ISS_CENTRAL_BANK.toString(), ERole.ROLE_ISSUER);
	}
}
