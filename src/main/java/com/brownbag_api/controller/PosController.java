package com.brownbag_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.Position;
import com.brownbag_api.model.User;
import com.brownbag_api.repo.PosRepo;
import com.brownbag_api.security.repo.UserRepo;
import com.brownbag_api.security.svc.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/pos")
public class PosController {

	@Autowired
	private PosRepo posRepo;

	@Autowired
	UserRepo userRepo;

	@GetMapping("/all")
	public List<Position> getAll() {
		return posRepo.findAll();
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	@ResponseBody
	public List<Position> getByUser(Authentication authentication) {

		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		User user = userRepo.findById(userDetailsImpl.getId())
				.orElseThrow(() -> new RuntimeException("Error: User not found. USER.ID: " + userDetailsImpl.getId()));
		List<Position> positions = posRepo.findByPosUser(user);
		return positions ;
	}

}