package com.brownbag_api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.jpa.ObjPos;
import com.brownbag_api.model.json.JsonObjPos;
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

	private List<JsonObjPos> jpaToJson(List<ObjPos> jpaPosList) {
		List<JsonObjPos> jsonPosList = new ArrayList<JsonObjPos>();
		for (ObjPos jpaPos : jpaPosList) {
			JsonObjPos jsonPos = new JsonObjPos(jpaPos);
			jsonPosList.add(jsonPos);
		}
		return jsonPosList;
	}

	@GetMapping("/all")
	public List<JsonObjPos> getAll() {
		List<ObjPos> jpaPosList = posRepo.findAll();
		return jpaToJson(jpaPosList);
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