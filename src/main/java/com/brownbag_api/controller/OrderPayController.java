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
import com.brownbag_api.model.enums.EUser;
import com.brownbag_api.model.jpa.ObjPos;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.model.jpa.OrderLoan;
import com.brownbag_api.model.jpa.OrderPay;
import com.brownbag_api.model.json.JsonFormOrder;
import com.brownbag_api.model.json.JsonOrderLoan;
import com.brownbag_api.model.json.JsonOrderPay;
import com.brownbag_api.repo.OrderPayRepo;
import com.brownbag_api.repo.PosRepo;
import com.brownbag_api.repo.UserRepo;
import com.brownbag_api.service.OrderPaySvc;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order/ORDER_PAY")
public class OrderPayController extends OrderController {

	@Autowired
	private PosRepo posRepo;

	@Autowired
	private OrderPayRepo orderPayRepo;
	@Autowired
	private OrderPaySvc orderPaySvc;

	@Autowired
	UserRepo userRepo;
	
	public ResponseEntity<?> createNewOrder(Authentication authentication, @PathVariable EOrderStatus newStatus,
			@PathVariable EOrderType orderType) {
		System.out.println("New Loan");
		ObjUser objUser = getByAuthentication(authentication);
		OrderPay orderPay = orderPaySvc.createOrder(objUser, orderType);
		JsonOrderPay jsonOrderPay = new JsonOrderPay(orderPay);
		JsonFormOrder jsonOrderForm = guiSvc.getFormByEntityType(EEntityType.ORDER_PAY);
		class FormWithOrder {
			JsonOrderPay jsonOrderPay;
			JsonFormOrder jsonOrderForm;

			public JsonOrderPay getJsonOrderPay() {
				return jsonOrderPay;
			}

			public JsonFormOrder getOrderForm() {
				return jsonOrderForm;
			}

		}
		FormWithOrder formWithOrder = new FormWithOrder();
		formWithOrder.jsonOrderPay = jsonOrderPay;
		formWithOrder.jsonOrderForm = jsonOrderForm;
		return ResponseEntity.ok(formWithOrder);
	}

//	@GetMapping("/all")
//	public List<JsonOrderPay> getAllPay() {
//		List<OrderPay> jpaOrdersPay = orderPayRepo.findAll();
//		List<JsonOrderPay> jsonOrders = new ArrayList<JsonOrderPay>();
//		for (OrderPay orderPay : jpaOrdersPay) {
//			JsonOrderPay jsonOrder = new JsonOrderPay(orderPay);
//			jsonOrders.add(jsonOrder);
//		}
//		return jsonOrders;
//	}

	
	@GetMapping("/{id}")
	public ResponseEntity<?> getPaymentByIdTest(@PathVariable Long id) {

		OrderPay orderPay = orderPayRepo.findById(id).orElse(null);
		if (orderPay == null) {
			return ResponseEntity.noContent().build();
		} else {
			JsonOrderPay jsonOrderPay = new JsonOrderPay(orderPay);
			return ResponseEntity.ok(jsonOrderPay);
		}

	}

	@GetMapping("/exec/from/{from}/to/{to}/amount/{amount}")
	public ResponseEntity<?> execPayment(@PathVariable Long from, @PathVariable Long to, @PathVariable double amount) {
		ObjUser user = userRepo.findByUsername(EUser.MGR_ECB.toString());
		ObjPos posFrom = posRepo.getOne(from);
		ObjPos posTo = posRepo.getOne(to);
		OrderPay pay = orderPaySvc.createPay(amount, user, null, null, posFrom, posTo);
		pay = orderPaySvc.execPay(pay);

		if (pay == null) {
			return ResponseEntity.ok("ERROR API: Payment could not be executed. Please see log for more details!");
		} else {
			Long payId = pay.getId();
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