package com.brownbag_api.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.enums.ERole;
import com.brownbag_api.model.enums.EUser;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjRole;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.RoleRepo;
import com.brownbag_api.repo.UserRepo;
import com.brownbag_api.security.payload.response.MsgResponse;
import com.brownbag_api.security.svc.UserDetailsImpl;

@Service
public class UserSvcImpl implements UserSvc {

	@Autowired
	UserRepo userRepo;

	@Autowired
	UserSvc userSvc;

	@Autowired
	RoleRepo roleRepo;

	@Autowired
	PosSvc posSvc;

	@Autowired
	PartySvc partySvc;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	AssetRepo assetRepo;

	@Override
	public void save(ObjUser user) {
		userRepo.save(user);
	}

	/*
	 * GET OBJ_USER BY AUTH OBJ
	 */
	@Override
	public ObjUser getByAuthentication(Authentication authentication) {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		ObjUser objUser = userRepo.findById(userDetailsImpl.getId()).orElseThrow(
				() -> new RuntimeException("ERROR API: User not found. USER.ID: " + userDetailsImpl.getId()));
		return objUser;
	}

	@Override
	public boolean hasRole(ObjUser user, ERole eRole) {
		for (ObjRole role : user.getRoles()) {
			if (role.getName().equals(eRole.toString())) {
				return true;
			}
		}
		return false;
	};

	@Override
	public ResponseEntity<?> registerUser(String userName, String name, String password, Set<String> eRoles) {

		// ---------------------------------------------------------------------
		// ENSURE USER NAME IS UNIQUE
		// ---------------------------------------------------------------------
		if (userRepo.existsByUsername(userName)) {
			return ResponseEntity.badRequest().body(new MsgResponse("Error: Username is already taken!"));
		}

		// ---------------------------------------------------------------------
		// INSTANTIATE USER
		// ---------------------------------------------------------------------
		ObjUser user = new ObjUser(userName, name, encoder.encode(password));

		// ---------------------------------------------------------------------
		// ADD ROLES
		// ---------------------------------------------------------------------
		Set<String> strRoles = eRoles;
		Set<ObjRole> roles = new HashSet<>();

		if (strRoles == null) {
			ObjRole userRole = roleRepo.findByName(ERole.ROLE_MGR.getName());
			roles.add(userRole);
		} else {
			strRoles.forEach(strRole -> {
				ObjRole role = roleRepo.findByName(strRole);
				if (role != null) {
					roles.add(role);
				}
			});
		}

		user.setRoles(roles);

		// ---------------------------------------------------------------------
		// PERSIST USER
		// ---------------------------------------------------------------------
		user = userRepo.save(user);

		// ---------------------------------------------------------------------
		// ADD NATURAL PERSON WITH MACC
		// ---------------------------------------------------------------------
		for (ObjRole role : user.getRoles()) {
			String roleName = role.getName();
			if (roleName.equals(ERole.ROLE_MGR.getName())) {
				partySvc.createNaturalPerson(user);
				break;
			}
		}

		return ResponseEntity.ok(new MsgResponse("User registered successfully!"));
	}

	@Override
	public ResponseEntity<?> registerUser(String userName, String password, Set<String> eRoles) {
		return registerUser(userName, userName, password, eRoles);

	}

	@Override
	public ObjParty getOrganisation(ObjUser user) {

		return partySvc.getNaturalPerson(user);
	}

	@Override
	public ObjParty getNaturalPerson(ObjUser user) {
		return partySvc.getNaturalPerson(user);
	}

	@Override
	public ObjUser getByEnum(EUser eUser) {
		return userRepo.findByUsername(eUser.toString());
	}
}
