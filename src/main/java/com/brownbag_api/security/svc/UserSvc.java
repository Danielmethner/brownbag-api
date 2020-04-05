package com.brownbag_api.security.svc;

import java.util.Set;

import org.springframework.http.ResponseEntity;

import com.brownbag_api.model.LegalEntity;
import com.brownbag_api.model.User;
import com.brownbag_api.model.data.ERole;

public interface UserSvc {
	public void save(User user);

	ResponseEntity<?> registerUser(String userName, String password, Set<String> strRoles);

	ResponseEntity<?> registerUser(String userName, String name, String password, Set<String> eRoles);

	boolean hasRole(User user, ERole eRole);

	LegalEntity getNaturalPerson(User user);

	LegalEntity getOrganisation(User user);
}
