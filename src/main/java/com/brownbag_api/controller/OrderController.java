package com.brownbag_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.jpa.Order;
import com.brownbag_api.model.jpa.OrderPay;
import com.brownbag_api.repo.OrderPayRepo;
import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.repo.UserRepo;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private OrderPayRepo orderPayRepo;

	@Autowired
	UserRepo userRepo;

	@GetMapping("/all")
	public List<Order> getAll() {
		return orderRepo.findAll();
	}

	@GetMapping("/pay/all")
	public List<OrderPay> getAllPayments() {
		return orderPayRepo.findAll();
	}

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