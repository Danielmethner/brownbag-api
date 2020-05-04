package com.brownbag_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.repo.PartyRepo;
import com.brownbag_api.service.UserSvc;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	PartyRepo lERepo;

	@Autowired
	UserSvc userSvc;

//	@RequestMapping(value = "/user", method = RequestMethod.GET)
//	@ResponseBody
//	public List<Position> getByUser(Authentication authentication) {
//
//		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
//		User user = userRepo.findById(userDetailsImpl.getId())
//				.orElseThrow(() -> new RuntimeException("Error: User not found. USER.ID: " + userDetailsImpl.getId()));
//
//		List<Position> positions = posRepo.findByOwner(.getNaturalPerson());
//		return positions ;
//	}

}