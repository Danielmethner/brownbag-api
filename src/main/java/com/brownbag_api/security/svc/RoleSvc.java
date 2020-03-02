package com.brownbag_api.security.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.security.model.Role;
import com.brownbag_api.security.repo.RoleRepo;

public interface RoleSvc {

	public abstract void save(Role role);
}
