package com.brownbag_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.repo.AssetRepo;
//import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.security.repo.UserRepo;
import com.brownbag_api.service.OrderSvc;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private AssetRepo assetRepo;
	
//	@Autowired
//	private OrderRepo orderRepo;
		
	@Autowired
	private OrderSvc orderSvc;
	
	@Autowired
	UserRepo userRepo;

//	@GetMapping("/all")
//	public List<OrderBase> getAll() {
//		return orderRepo.findAll();
//	}

//	@RequestMapping(value = "/user", method = RequestMethod.GET)
//	@ResponseBody
//	public List<Order> getByUser(Authentication authentication) {
//
//		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
//		User user = userRepo.findById(userDetailsImpl.getId())
//				.orElseThrow(() -> new RuntimeException("Error: User not found. USER.ID: " + userDetailsImpl.getId()));
//		List<Order> orders = orderRepo.findByUser(user);
//		return orders;
//	}
	
//	@PostMapping(value = "/place", consumes="application/json")
//	public ResponseEntity<?> authenticateUser(@RequestBody Order order, Authentication authentication) {
//		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
//		User user = userRepo.findById(userDetailsImpl.getId())
//				.orElseThrow(() -> new RuntimeException("Error: User not found. USER.ID: " + userDetailsImpl.getId()));
//		order.setUser(user);
//		order.setOrderStatus(EOrderStatus.NEW);
//		orderSvc.place(order);
//		return ResponseEntity.ok("Order has been placed successfully!");
//	}


}