package com.brownbag_api.service;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.brownbag_api.model.enums.ERole;
import com.brownbag_api.model.enums.EUser;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjUser;

public interface UserSvc {
	public void save(ObjUser user);

	ResponseEntity<?> registerUser(String userName, String password, Set<String> strRoles);

	ResponseEntity<?> registerUser(String userName, String name, String password, Set<String> eRoles);

	boolean hasRole(ObjUser user, ERole eRole);

	ObjParty getNaturalPerson(ObjUser user);

	ObjParty getOrganisation(ObjUser user);

	ObjUser getByEnum(EUser eUser);

	public ObjUser getByAuthentication(Authentication authentication);
}
