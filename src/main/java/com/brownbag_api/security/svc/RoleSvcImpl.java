package com.brownbag_api.security.svc;

import org.springframework.stereotype.Service;

import com.brownbag_api.security.model.Role;
import com.brownbag_api.security.repo.RoleRepo;

@Service
public class RoleSvcImpl implements RoleSvc {

	RoleRepo roleRepo;
	
	@Override
	public void save(Role role) {
		roleRepo.save(role);
	}

}
