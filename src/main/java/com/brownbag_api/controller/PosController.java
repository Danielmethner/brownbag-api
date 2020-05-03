package com.brownbag_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.jpa.ObjPos;
import com.brownbag_api.repo.PartyRepo;
import com.brownbag_api.repo.PosRepo;
import com.brownbag_api.service.UserSvc;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/pos")
public class PosController {

	@Autowired
	private PosRepo posRepo;

	@Autowired
	PartyRepo lERepo;

	@Autowired
	UserSvc userSvc;

	@GetMapping("/all")
	public List<ObjPos> getAll() {
		return posRepo.findAll();
	}

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