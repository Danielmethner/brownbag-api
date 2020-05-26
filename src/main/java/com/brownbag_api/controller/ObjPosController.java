package com.brownbag_api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.jpa.Booking;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjPos;
import com.brownbag_api.model.jpa.ObjPosLoan;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.model.json.JsonBooking;
import com.brownbag_api.model.json.JsonObjPos;
import com.brownbag_api.model.json.JsonObjPosLoan;
import com.brownbag_api.repo.PartyRepo;
import com.brownbag_api.repo.PosRepo;
import com.brownbag_api.repo.UserRepo;
import com.brownbag_api.security.payload.response.MsgResponse;
import com.brownbag_api.security.svc.UserDetailsImpl;
import com.brownbag_api.service.AssetSvc;
import com.brownbag_api.service.BookingSvc;
import com.brownbag_api.service.PartySvc;
import com.brownbag_api.service.PosSvc;
import com.brownbag_api.service.UserSvc;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/pos")
public class ObjPosController {

	@Autowired
	private PosRepo posRepo;

	@Autowired
	private PosSvc posSvc;

	@Autowired
	private AssetSvc assetSvc;

	@Autowired
	private BookingSvc bookingSvc;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	PartyRepo lERepo;

	@Autowired
	UserSvc userSvc;

	@Autowired
	PartySvc partySvc;

	/*
	 * GET OBJ_USER BY AUTHENTICATION
	 */
	private ObjUser getByAuthentication(Authentication authentication) {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		ObjUser objUser = userRepo.findById(userDetailsImpl.getId()).orElseThrow(
				() -> new RuntimeException("ERROR API: User not found. USER.ID: " + userDetailsImpl.getId()));
		return objUser;
	}

	/*
	 * CONVERT JPA TO JSON
	 */
	private List<JsonObjPos> jpaToJson(List<ObjPos> jpaPosList, boolean addPriceData) {
		List<JsonObjPos> jsonPosList = new ArrayList<JsonObjPos>();
		for (ObjPos jpaPos : jpaPosList) {
			JsonObjPos jsonPos = new JsonObjPos(jpaPos);

			// DELIVER PRICE DATA
			if (addPriceData == true) {
				double lastPrice = assetSvc.getLastPrice(jpaPos.getAsset());
				jsonPos.setPriceLast(lastPrice);
				if ((jsonPos.getPriceLast() - jsonPos.getPriceAvg()) != 0) {
					double profitLoss = (jsonPos.getPriceLast() - jsonPos.getPriceAvg()) / jsonPos.getPriceAvg() * 100 ;
					jsonPos.setProfitLoss(profitLoss);

				}
			}
			jsonPosList.add(jsonPos);
		}
		return jsonPosList;
	}
	/*
	 * CONVERT JPA TO JSON - BOOKING
	 */
	private List<JsonBooking> jpaToJsonBooking(List<Booking> jpaBookingList) {
		List<JsonBooking> jsonBookingList = new ArrayList<JsonBooking>();
		for (Booking jpaBooking : jpaBookingList) {
			JsonBooking jsonBooking = new JsonBooking(jpaBooking);
			jsonBookingList.add(jsonBooking);
		}
		return jsonBookingList;
	}
	
	/*
	 * CONVERT JPA TO JSON - OBJ_POS_LOAN
	 */
	private List<JsonObjPosLoan> jpaToJsonPosLoan(List<ObjPosLoan> jpaPosList) {
		List<JsonObjPosLoan> jsonPosLoanList = new ArrayList<JsonObjPosLoan>();
		for (ObjPosLoan jpaPosLoan : jpaPosList) {
			JsonObjPosLoan jsonPosLoan = new JsonObjPosLoan(jpaPosLoan);
			jsonPosLoanList.add(jsonPosLoan);
		}
		return jsonPosLoanList;
	}

	@GetMapping("/all")
	public List<JsonObjPos> getAll() {
		List<ObjPos> jpaPosList = posRepo.findAll();
		return jpaToJson(jpaPosList, false);
	}

	@GetMapping("/user")
	public List<JsonObjPos> getByUser(Authentication authentication) {

		ObjUser user = getByAuthentication(authentication);
		ObjParty partyPerson = userSvc.getNaturalPerson(user);
		List<ObjPos> jpaPosList = posSvc.getByParty(partyPerson);

		return jpaToJson(jpaPosList, false);
	}

	@GetMapping("/private/user")
	public List<JsonObjPos> getPrivateByUser(Authentication authentication) {

		ObjUser user = getByAuthentication(authentication);
		ObjParty partyPerson = userSvc.getNaturalPerson(user);
		List<ObjPos> jpaPosList = posSvc.getByParty(partyPerson);

		return jpaToJson(jpaPosList, false);
	}

	@GetMapping("/party/{partyId}")
	public ResponseEntity<?> getByPartyId(@PathVariable Long partyId) {
		if (partyId == null)
			return ResponseEntity.badRequest().body(new MsgResponse("ERROR API: No Party ID specified!"));
		ObjParty jpaParty = partySvc.getById(partyId);
		List<ObjPos> jpaPosList = posSvc.getByParty(jpaParty);
		List<JsonObjPos> jsonPosList = jpaToJson(jpaPosList, true);
		return ResponseEntity.ok(jsonPosList);
	}
	
	@GetMapping("/financing/party/{partyId}")
	public ResponseEntity<?> getFinancingByPartyId(@PathVariable Long partyId) {
		if (partyId == null)
			return ResponseEntity.badRequest().body(new MsgResponse("ERROR API: No Party ID specified!"));
		ObjParty jpaParty = partySvc.getById(partyId);
		List<ObjPosLoan> jpaPosList = posSvc.getFinancingByParty(jpaParty);
		List<JsonObjPosLoan> jsonPosList = jpaToJsonPosLoan(jpaPosList);
		return ResponseEntity.ok(jsonPosList);
	}


	@GetMapping("/bookings/party/{partyId}/pos/{posId}")
	public ResponseEntity<?> getByPartyId(@PathVariable Long partyId, @PathVariable Long posId) {
		if (partyId == null)
			return ResponseEntity.badRequest().body(new MsgResponse("ERROR API: No Party ID specified!"));
		if (posId == null)
			return ResponseEntity.badRequest().body(new MsgResponse("ERROR API: No Position ID specified!"));
		ObjPos jpaPos = posSvc.getById(posId);
		List<Booking> jpaBookingList = bookingSvc.getByPos(jpaPos);
		return ResponseEntity.ok(jpaToJsonBooking(jpaBookingList));
	}

}