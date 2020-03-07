package com.brownbag_api.security.svc;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.brownbag_api.security.model.ERole;
import com.brownbag_api.security.model.Role;
import com.brownbag_api.security.model.User;
import com.brownbag_api.security.payload.request.SignupRequest;
import com.brownbag_api.security.payload.response.MsgResponse;
import com.brownbag_api.security.repo.RoleRepo;
import com.brownbag_api.security.repo.UserRepo;

@Service
public class UserSvcImpl implements UserSvc {
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	RoleRepo roleRepo;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Override
	public void save(User user) {
		userRepo.save(user);
	}
	
	@Override
	public ResponseEntity<?> registerUser(String userName, String password, Set<String> eRoles) {

		if (userRepo.existsByUsername(userName)) {
			return ResponseEntity.badRequest().body(new MsgResponse("Error: Username is already taken!"));
		}

		// Create new user's account
		User user = new User(userName, encoder.encode(password));

		Set<String> strRoles = eRoles;
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepo.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {

				switch (role) {
				case "admin":
					Role adminRole = roleRepo.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepo.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepo.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepo.save(user);
		return ResponseEntity.ok(new MsgResponse("User registered successfully!"));
	}
}
