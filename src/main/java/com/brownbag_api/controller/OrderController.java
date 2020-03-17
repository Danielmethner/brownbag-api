package com.brownbag_api.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.Order;
import com.brownbag_api.model.User;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.security.payload.request.LoginRequest;
import com.brownbag_api.security.payload.response.JwtResponse;
import com.brownbag_api.security.repo.UserRepo;
import com.brownbag_api.security.svc.UserDetailsImpl;
import com.brownbag_api.service.OrderSvc;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private AssetRepo assetRepo;
	
	@Autowired
	private OrderRepo orderRepo;
	
	@Autowired
	private OrderSvc orderSvc;
	
	@Autowired
	UserRepo userRepo;

	@GetMapping("/all")
	public List<Order> getAll() {
		return orderRepo.findAll();
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	@ResponseBody
	public List<Order> getByUser(Authentication authentication) {

		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		User user = userRepo.findById(userDetailsImpl.getId())
				.orElseThrow(() -> new RuntimeException("Error: User not found. USER.ID: " + userDetailsImpl.getId()));
		List<Order> orders = orderRepo.findByUser(user);
		return orders;
	}
	
	@PostMapping(value = "/place", consumes="application/json")
	public ResponseEntity<?> authenticateUser(@RequestBody Order order, Authentication authentication) {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		User user = userRepo.findById(userDetailsImpl.getId())
				.orElseThrow(() -> new RuntimeException("Error: User not found. USER.ID: " + userDetailsImpl.getId()));
		order.setUser(user);
		orderSvc.place(order);
		return ResponseEntity.ok("Order has been placed successfully!");
	}


}