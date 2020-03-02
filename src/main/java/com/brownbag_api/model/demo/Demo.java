package com.brownbag_api.model.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brownbag_api.security.controller.AuthController;
import com.brownbag_api.security.model.ERole;
import com.brownbag_api.security.model.Role;
import com.brownbag_api.security.repo.RoleRepo;
import com.brownbag_api.security.svc.RoleSvc;

@Component
public class Demo {

	@Autowired
	RoleSvc roleSvc;
	
	private void createDemoUser(String userName) {
//		User user = new User(userName);

	}

	private void createDemoUsers() {
		createDemoUser("First User");
		createDemoUser("Second User");
		createDemoUser("Third User");
	}

	private void createDemoRole(ERole name) {
		Role role = new Role(name);
		roleSvc.save(role);
	}

	private void createDemoRoles() {
		createDemoRole(ERole.ROLE_ADMIN);
		createDemoRole(ERole.ROLE_MODERATOR);
		createDemoRole(ERole.ROLE_USER);
	}

	public void createDemoData() {
		createDemoRoles();
		createDemoUsers();
	}

}
