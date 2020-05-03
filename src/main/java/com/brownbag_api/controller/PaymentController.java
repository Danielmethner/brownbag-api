package com.brownbag_api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.enums.EUser;
import com.brownbag_api.model.jpa.ObjPos;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.model.jpa.OrderPay;
import com.brownbag_api.repo.OrderPayRepo;
import com.brownbag_api.repo.PosRepo;
import com.brownbag_api.repo.UserRepo;
import com.brownbag_api.service.OrderPaySvc;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/pay")
public class PaymentController {

	@Autowired
	private PosRepo posRepo;

	@Autowired
	private OrderPayRepo orderPayRepo;
	@Autowired
	private OrderPaySvc orderPaySvc;

	@Autowired
	UserRepo userRepo;

	@GetMapping("/all")
	public List<OrderPay> getAllPayments() {
		return orderPayRepo.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getPaymentByIdTest(@PathVariable Long id) {
		Optional<OrderPay> orderPay = orderPayRepo.findById(id);
		return ResponseEntity.ok(orderPay);
	}

	@GetMapping("/exec/from/{from}/to/{to}/amount/{amount}")
	public ResponseEntity<?> execPayment(@PathVariable Long from, @PathVariable Long to, @PathVariable double amount) {
		ObjUser user = userRepo.findByUsername(EUser.MGR_ECB.toString());
		ObjPos posFrom = posRepo.getOne(from);
		ObjPos posTo = posRepo.getOne(to);
		OrderPay pay = orderPaySvc.createPay(amount, user, null, null, posFrom, posTo);
		pay = orderPaySvc.execPay(pay);

		if (pay == null) {
			return ResponseEntity.ok("Payment could not be executed. Please see log for more details!");
		} else {
			System.err.println("Payment executed: " + pay.getPosRcv().getParty().getName());
			Long payId = pay.getId();
			System.err.println("Payment executed: " + payId);
			orderPayRepo.findById(payId);
			return ResponseEntity.ok(pay);
		}

	}

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