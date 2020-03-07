package com.brownbag_api.security.svc;

import java.util.Set;

import org.springframework.http.ResponseEntity;

import com.brownbag_api.security.model.ERole;
import com.brownbag_api.security.model.User;

public interface UserSvc {
	public void save(User user);

	ResponseEntity<?> registerUser(String userName, String password, Set<String> strRoles);
}
