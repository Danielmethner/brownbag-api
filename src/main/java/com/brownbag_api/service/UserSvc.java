package com.brownbag_api.service;

import java.util.Set;

import org.springframework.http.ResponseEntity;

import com.brownbag_api.model.Party;
import com.brownbag_api.model.User;
import com.brownbag_api.model.data.ERole;
import com.brownbag_api.model.data.EUser;

public interface UserSvc {
	public void save(User user);

	ResponseEntity<?> registerUser(String userName, String password, Set<String> strRoles);

	ResponseEntity<?> registerUser(String userName, String name, String password, Set<String> eRoles);

	boolean hasRole(User user, ERole eRole);

	Party getNaturalPerson(User user);

	Party getOrganisation(User user);

	User getByEnum(EUser eUser);
}
