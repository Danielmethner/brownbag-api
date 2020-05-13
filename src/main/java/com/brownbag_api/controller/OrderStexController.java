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

import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.model.jpa.OrderStex;
import com.brownbag_api.model.json.JsonObjAsset;
import com.brownbag_api.model.json.JsonOrderStex;
import com.brownbag_api.repo.OrderStexRepo;
import com.brownbag_api.repo.PosRepo;
import com.brownbag_api.repo.UserRepo;
import com.brownbag_api.security.payload.response.MsgResponse;
import com.brownbag_api.security.svc.UserDetailsImpl;
import com.brownbag_api.service.AssetSvc;
import com.brownbag_api.service.OrderStexSvc;
import com.brownbag_api.service.PartySvc;
import com.brownbag_api.service.UserSvc;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order/stex")
public class OrderStexController {

	@Autowired
	PosRepo posRepo;

	@Autowired
	OrderStexRepo orderStexRepo;
	@Autowired
	OrderStexSvc orderStexSvc;

	@Autowired
	UserRepo userRepo;

	@Autowired
	AssetSvc assetSvc;

	@Autowired
	UserSvc userSvc;

	@Autowired
	PartySvc partySvc;

	/*
	 * CONVERT JPA TO JSON
	 */
	private List<JsonOrderStex> jpaToJson(List<OrderStex> jpaOrderStexList) {
		List<JsonOrderStex> jsonOrderStexList = new ArrayList<JsonOrderStex>();
		for (OrderStex jpaOrderStex : jpaOrderStexList) {
			JsonOrderStex jsonOrderStex = new JsonOrderStex(jpaOrderStex);
			jsonOrderStexList.add(jsonOrderStex);
		}
		return jsonOrderStexList;
	}

	/*
	 * GET OBJ_USER BY AUTH OBJ
	 */
	private ObjUser getByAuthentication(Authentication authentication) {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		ObjUser objUser = userRepo.findById(userDetailsImpl.getId())
				.orElseThrow(() -> new RuntimeException("Error: User not found. USER.ID: " + userDetailsImpl.getId()));
		return objUser;
	}

	@GetMapping("/all")
	public List<JsonOrderStex> getAllStex() {
		List<OrderStex> jpaOrderStexList = orderStexRepo.findAll();
		return jpaToJson(jpaOrderStexList);
	}

	@GetMapping("/user")
	public List<JsonOrderStex> getByUser(Authentication authentication) {
		ObjUser user = getByAuthentication(authentication);
		List<OrderStex> jpaOrderStexList = orderStexRepo.findByUser(user);
		return jpaToJson(jpaOrderStexList);
	}

	@GetMapping("/party/{partyId}")
	public ResponseEntity<?> getByParty(Authentication authentication, @PathVariable Long partyId) {

		if (partyId == null)
			return ResponseEntity.badRequest().body(new MsgResponse("Error: No Party ID specified!"));

		ObjParty party = partySvc.getById(partyId);
		if (party == null)
			return ResponseEntity.badRequest()
					.body(new MsgResponse("Error: Party with ID: " + partyId + " could not be found!"));

		List<OrderStex> jpaOrderStexList = orderStexSvc.getByParty(party);

		return ResponseEntity.ok(jpaToJson(jpaOrderStexList));
	}

	@PostMapping(value = "/place", consumes = "application/json")
	public ResponseEntity<?> placeOrder(@RequestBody JsonOrderStex jsonOrderStex, Authentication authentication) {
		ObjUser user = getByAuthentication(authentication);
		ObjParty party = partySvc.getById(jsonOrderStex.getPartyId());
		if (jsonOrderStex.getAssetId() == null)
			return ResponseEntity.ok("Order could not be placed: No Asset selected!");

		ObjAsset asset = assetSvc.getById(jsonOrderStex.getAssetId());
		OrderStex orderStex = orderStexSvc.placeNewOrder(jsonOrderStex.getOrderDir(), jsonOrderStex.getOrderType(),
				asset, (int) jsonOrderStex.getQty(), jsonOrderStex.getPriceLimit(), user, party);
		if (orderStex == null)
			return ResponseEntity.badRequest()
					.body(new MsgResponse("Error: Order could not be created: Direction: " + jsonOrderStex.getOrderDir()
							+ " Order Type: " + jsonOrderStex.getOrderType() + " Asset: " + asset + " Quantity: "
							+ (int) jsonOrderStex.getQty() + " Price Limit: " + jsonOrderStex.getPriceLimit()
							+ " User: " + user + " Party: " + party + ". Please Check logs for more details."));
		orderStexSvc.placeOrder(orderStex);
		return ResponseEntity.ok("Order has been placed successfully!");
	}

}