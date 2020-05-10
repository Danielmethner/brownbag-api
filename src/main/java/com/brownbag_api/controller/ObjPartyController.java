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
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.model.json.JsonObjParty;
import com.brownbag_api.repo.PartyRepo;
import com.brownbag_api.repo.PartyRepo;
import com.brownbag_api.repo.UserRepo;
import com.brownbag_api.security.svc.UserDetailsImpl;
import com.brownbag_api.service.UserSvc;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/party")
public class ObjPartyController {

	@Autowired
	private PartyRepo partyRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	PartyRepo lERepo;

	@Autowired
	UserSvc userSvc;

	private List<JsonObjParty> jpaToJson(List<ObjParty> jpaPartyList) {
		List<JsonObjParty> jsonPartyList = new ArrayList<JsonObjParty>();
		for (ObjParty jpaParty : jpaPartyList) {
			JsonObjParty jsonParty = new JsonObjParty(jpaParty);
			jsonPartyList.add(jsonParty);
		}
		return jsonPartyList;
	}

	@GetMapping("/all")
	public List<JsonObjParty> getAll() {
		List<ObjParty> jpaPartyList = partyRepo.findAll();
		return jpaToJson(jpaPartyList);
	}

//	@RequestMapping(value = "/user", method = RequestMethod.GET)
//	@ResponseBody
//	public List<JsonObjParty> getByUser(Authentication authentication) {
//
//		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
//		ObjUser user = userRepo.findById(userDetailsImpl.getId())
//				.orElseThrow(() -> new RuntimeException("Error: User not found. USER.ID: " + userDetailsImpl.getId()));
//		ObjParty partyPerson = userSvc.getNaturalPerson(user);
//		List<ObjParty> jpaPartyList = posRepo.findByParty(partyPerson);
//
//		return jpaToJson(jpaPartyList);
//	}

}