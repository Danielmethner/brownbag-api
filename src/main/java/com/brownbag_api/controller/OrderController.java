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

import com.brownbag_api.model.enums.EEntityType;
import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.model.jpa.Order;
import com.brownbag_api.model.jpa.OrderStex;
import com.brownbag_api.model.json.JsonFormOrder;
import com.brownbag_api.model.json.JsonOrder;
import com.brownbag_api.model.json.JsonOrderStex;
import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.repo.UserRepo;
import com.brownbag_api.security.svc.UserDetailsImpl;
import com.brownbag_api.service.GuiSvc;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	UserRepo userRepo;
	
	@Autowired
	GuiSvc guiSvc;

	/*
	 * GET OBJ_USER BY AUTH OBJ
	 */
	public ObjUser getByAuthentication(Authentication authentication) {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		ObjUser objUser = userRepo.findById(userDetailsImpl.getId()).orElseThrow(
				() -> new RuntimeException("ERROR API: User not found. USER.ID: " + userDetailsImpl.getId()));
		return objUser;
	}

	@GetMapping("/all")
	public List<?> getAll() {
		List<Order> jpaOrders = orderRepo.findAll();
		List<JsonOrder> jsonOrders = new ArrayList<JsonOrder>();
		for (Order order : jpaOrders) {
			JsonOrder jsonOrder = new JsonOrder(order);
			jsonOrders.add(jsonOrder);
		}
		return jsonOrders;
	}

	@GetMapping("/new/newStatus/{newStatus}/orderType/{orderType}")
	public ResponseEntity<?> createNewOrder(Authentication authentication, @PathVariable EOrderStatus newStatus,
			@PathVariable EOrderType orderType) {
		return null;
	}
}