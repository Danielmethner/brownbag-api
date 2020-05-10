package com.brownbag_api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjPos;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.model.json.JsonObjPos;
import com.brownbag_api.repo.PartyRepo;
import com.brownbag_api.repo.PosRepo;
import com.brownbag_api.repo.UserRepo;
import com.brownbag_api.security.svc.UserDetailsImpl;
import com.brownbag_api.service.UserSvc;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/pos")
public class ObjPosController {

	@Autowired
	private PosRepo posRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	PartyRepo lERepo;

	@Autowired
	UserSvc userSvc;
	
	private ObjUser getByAuthentication(Authentication authentication) {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		ObjUser objUser = userRepo.findById(userDetailsImpl.getId())
				.orElseThrow(() -> new RuntimeException("Error: User not found. USER.ID: " + userDetailsImpl.getId()));
		return objUser;
	}

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

	@GetMapping("/user")
	public List<JsonObjPos> getByUser(Authentication authentication) {

		ObjUser user = getByAuthentication(authentication);
		ObjParty partyPerson = userSvc.getNaturalPerson(user);
		List<ObjPos> jpaPosList = posRepo.findByParty(partyPerson);

		return jpaToJson(jpaPosList);
	}
	
	@GetMapping("/private/user")
	public List<JsonObjPos> getPrivateByUser(Authentication authentication) {

		ObjUser user = getByAuthentication(authentication);
		ObjParty partyPerson = userSvc.getNaturalPerson(user);
		List<ObjPos> jpaPosList = posRepo.findByParty(partyPerson);

		return jpaToJson(jpaPosList);
	}

}