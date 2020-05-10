package com.brownbag_api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

	private List<JsonOrderStex> jpaToJson(List<OrderStex> jpaOrderStexList) {
		List<JsonOrderStex> jsonOrderStexList = new ArrayList<JsonOrderStex>();
		for (OrderStex jpaOrderStex : jpaOrderStexList) {
			JsonOrderStex jsonOrderStex = new JsonOrderStex(jpaOrderStex);
			jsonOrderStexList.add(jsonOrderStex);
		}
		return jsonOrderStexList;
	}

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

	@PostMapping(value = "/place", consumes = "application/json")
	public ResponseEntity<?> placeOrder(@RequestBody JsonOrderStex jsonOrderStex, Authentication authentication) {
		ObjUser user = getByAuthentication(authentication);
		ObjParty party = userSvc.getNaturalPerson(user);
		ObjAsset asset = assetSvc.getById(jsonOrderStex.getAssetId());

		System.out.println("user" + user.getName());
		System.out.println("party: " + party.getName());
		System.out.println("asset: " + asset.getName());
		System.out.println("getOrderDir: " + jsonOrderStex.getOrderDir());
		System.out.println("getOrderType: " + jsonOrderStex.getOrderType());
		System.out.println("getQty: " + jsonOrderStex.getQty());
		System.out.println("getPriceLimit: " + jsonOrderStex.getPriceLimit());
		OrderStex orderStex = orderStexSvc.placeNewOrder(jsonOrderStex.getOrderDir(), jsonOrderStex.getOrderType(),
				asset, (int) jsonOrderStex.getQty(), jsonOrderStex.getPriceLimit(), user, party);
		orderStexSvc.placeOrder(orderStex);
		return ResponseEntity.ok("Order has been placed successfully!");
	}

}