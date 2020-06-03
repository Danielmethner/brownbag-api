package com.brownbag_api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.enums.ELegalForm;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjPos;
import com.brownbag_api.model.jpa.ObjPosStex;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.model.json.JsonObjParty;
import com.brownbag_api.model.json.JsonObjPartyOwnership;
import com.brownbag_api.repo.PartyRepo;
import com.brownbag_api.repo.UserRepo;
import com.brownbag_api.security.payload.response.MsgResponse;
import com.brownbag_api.security.svc.UserDetailsImpl;
import com.brownbag_api.service.AssetSvc;
import com.brownbag_api.service.ControlSvc;
import com.brownbag_api.service.LogSvc;
import com.brownbag_api.service.PartySvc;
import com.brownbag_api.service.PosSvc;
import com.brownbag_api.service.UserSvc;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/party")
public class ObjPartyController {

	@Autowired
	AssetSvc assetSvc;

	@Autowired
	ControlSvc ctrlVarSvc;

	@Autowired
	PartyRepo partyRepo;

	@Autowired
	PartySvc partySvc;

	@Autowired
	PosSvc posSvc;

	@Autowired
	UserRepo userRepo;

	@Autowired
	PartyRepo lERepo;

	@Autowired
	UserSvc userSvc;

	@Autowired
	LogSvc logSvc;

	/*
	 * GET OBJ_USER BY AUTH OBJ
	 */
	private ObjUser getByAuthentication(Authentication authentication) {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		ObjUser objUser = userRepo.findById(userDetailsImpl.getId()).orElseGet(() -> {
			logSvc.write("Could not find user: " + userDetailsImpl.getName());
			return null;
		});
		return objUser;
	}

	/*
	 * CONVERT JPA TO JSON
	 */
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

	@GetMapping("/user/legalpersonlist")
	public List<JsonObjParty> getAllBusiness(Authentication authentication) {
		ObjUser user = getByAuthentication(authentication);
		List<ObjParty> jpaPartyList = partySvc.getLegalPersonByOwnerUser(user);
		return jpaToJson(jpaPartyList);
	}

	@GetMapping("/priv")
	public JsonObjParty getPriv(Authentication authentication) {
		ObjUser user = getByAuthentication(authentication);
		ObjParty jpaParty = userSvc.getNaturalPerson(user);
		JsonObjParty jsonParty = new JsonObjParty(jpaParty);
		return jsonParty;
	}

	@GetMapping("/{partyId}")
	public ResponseEntity<?> getById(@PathVariable Long partyId) {
		if (partyId == null)
			return ResponseEntity.badRequest().body(new MsgResponse("ERROR API: No Party ID specified!"));
		ObjParty jpaParty = partySvc.getById(partyId);

		if (jpaParty == null)
			return null;
		JsonObjParty jsonParty = new JsonObjParty(jpaParty);
		return ResponseEntity.ok(jsonParty);
	}

	@GetMapping("/{partyId}/ownership")
	public ResponseEntity<?> getOwnershipListByPartyId(@PathVariable Long partyId) {
		if (partyId == null)
			return ResponseEntity.badRequest().body(new MsgResponse("ERROR API: No Party ID specified!"));
		ObjParty jpaParty = partySvc.getById(partyId);

		if (jpaParty == null)
			return null;
		// TODO: get ownership
		List<ObjPosStex> ownershipPosList = partySvc.getOwnershipList(jpaParty);
		List<JsonObjPartyOwnership> ownershipList = new ArrayList<JsonObjPartyOwnership>();

		for (ObjPosStex objPosStex : ownershipPosList) {
			if (objPosStex.getQty() > 0) {
				JsonObjPartyOwnership jsonOwnership = new JsonObjPartyOwnership(objPosStex);
				ownershipList.add(jsonOwnership);

			}
		}
		return ResponseEntity.ok(ownershipList);
	}

	@GetMapping("/{partyId}/asset/{assetId}/qty/avbl")
	public ResponseEntity<?> getAvblQty(@PathVariable Long partyId, @PathVariable Long assetId) {
		if (partyId == null)
			return ResponseEntity.badRequest().body(new MsgResponse("ERROR API: No Party ID specified!"));
		ObjParty jpaParty = partySvc.getById(partyId);
		ObjAsset jpaAsset = assetSvc.getById(assetId);
		ObjPos objPos = posSvc.getByAssetAndParty(jpaAsset, jpaParty);
		if (objPos == null)
			return ResponseEntity.ok(0);
		double avblQty = posSvc.getQtyAvbl(objPos);
		return ResponseEntity.ok(avblQty);
	}

	@PostMapping(value = "/legalperson/create", consumes = "application/json")
	public ResponseEntity<?> createLegalPerson(@RequestBody JsonObjParty jsonObjParty, Authentication authentication) {
		String partyName = jsonObjParty.getName();
		ELegalForm legalForm = jsonObjParty.getLegalForm();
		ObjUser objUser = userSvc.getByAuthentication(authentication);
		ObjParty objParty = userSvc.getNaturalPerson(objUser);
		int shareQty = jsonObjParty.getAssetShareQty();
		ObjParty jpaParty = partySvc.createLegalPerson(partyName, legalForm, objUser, objParty, shareQty,
				jsonObjParty.getShareCapital());

		if (jpaParty == null) {
			logSvc.write("Party could not be created.");
			return ResponseEntity.ok("Party with Name: '" + jsonObjParty.getName()
					+ "' could not be created. Please check logs for more details!");
		}
		if (jpaParty.getLegalForm() == ELegalForm.CORP) {
			ObjAsset asset = assetSvc.getByIssuer(jpaParty);
			assetSvc.split(asset, jsonObjParty.getAssetShareQty());
		}
		return ResponseEntity.ok("New Party created: " + jpaParty.getId());
	}
}