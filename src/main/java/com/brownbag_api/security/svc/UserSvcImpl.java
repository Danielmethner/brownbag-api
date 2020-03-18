package com.brownbag_api.security.svc;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.ERole;
import com.brownbag_api.model.Role;
import com.brownbag_api.model.User;
import com.brownbag_api.model.data.EAsset;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.security.payload.response.MsgResponse;
import com.brownbag_api.security.repo.RoleRepo;
import com.brownbag_api.security.repo.UserRepo;
import com.brownbag_api.service.PosSvc;

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
	PasswordEncoder encoder;

	@Autowired
	AssetRepo assetRepo;

	@Override
	public void save(User user) {
		userRepo.save(user);
	}

	@Override
	public boolean hasRole(User user, ERole eRole) {
		for (Role role : user.getRoles()) {
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
		User user = new User(userName, name, encoder.encode(password));

		// ---------------------------------------------------------------------
		// ADD ROLES
		// ---------------------------------------------------------------------
		Set<String> strRoles = eRoles;
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepo.findByName(ERole.ROLE_TRADER.toString());
			roles.add(userRole);
		} else {
			strRoles.forEach(strRole -> {
				Role role = roleRepo.findByName(strRole);
				if (role != null) {
					roles.add(role);
				}
			});
		}

		user.setRoles(roles);

		// ---------------------------------------------------------------------
		// PERSIST USER
		// ---------------------------------------------------------------------
		userRepo.save(user);
		
		// ---------------------------------------------------------------------
		// ADD CASH ACCOUNT
		// ---------------------------------------------------------------------
		if (userSvc.hasRole(user, ERole.ROLE_TRADER)) {
			Asset assetCash = assetRepo.findByName(EAsset.CASH.getName());
			posSvc.createPosition(1000000, user, assetCash, 1);
		}
		
		return ResponseEntity.ok(new MsgResponse("User registered successfully!"));
	}

	@Override
	public ResponseEntity<?> registerUser(String userName, String password, Set<String> eRoles) {
		return registerUser(userName, userName, password, eRoles);

	}
}
