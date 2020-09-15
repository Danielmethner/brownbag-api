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
import com.brownbag_api.model.jpa.OrderCreateMon;
import com.brownbag_api.model.jpa.OrderIntr;
import com.brownbag_api.model.jpa.OrderLoan;
import com.brownbag_api.model.jpa.OrderPay;
import com.brownbag_api.model.json.JsonFormOrder;
import com.brownbag_api.model.json.JsonOrderCreateMon;
import com.brownbag_api.model.json.JsonOrderIntr;
import com.brownbag_api.model.json.JsonOrderLoan;
import com.brownbag_api.model.json.JsonOrderPay;
import com.brownbag_api.repo.OrderCreateMonRepo;
import com.brownbag_api.repo.OrderIntrRepo;
import com.brownbag_api.repo.OrderPayRepo;
import com.brownbag_api.repo.PosRepo;
import com.brownbag_api.repo.UserRepo;
import com.brownbag_api.service.OrderCreateMonSvc;
import com.brownbag_api.service.OrderIntrSvc;
import com.brownbag_api.service.OrderPaySvc;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order/ORDER_CREATE_MON")
public class OrderCreateMonController extends OrderController {

	@Autowired
	private PosRepo posRepo;

	@Autowired
	private OrderCreateMonRepo orderCreateMonRepo;
	@Autowired
	private OrderCreateMonSvc orderCreateMonSvc;

	@Autowired
	UserRepo userRepo;
	
	public ResponseEntity<?> createNewOrder(Authentication authentication, @PathVariable EOrderStatus newStatus,
			@PathVariable EOrderType orderType) {
		System.out.println("New Loan");
		ObjUser objUser = getByAuthentication(authentication);
		OrderCreateMon orderCreateMon = orderCreateMonSvc.createOrder(objUser, orderType);
		JsonOrderCreateMon jsonOrderCreateMon = new JsonOrderCreateMon(orderCreateMon);
		JsonFormOrder jsonOrderForm = guiSvc.getFormByEntityType(EEntityType.ORDER_CREATE_MON);
		class FormWithOrder {
			JsonOrderCreateMon jsonOrderCreateMon;
			JsonFormOrder jsonOrderForm;

			public JsonOrderCreateMon getJsonOrderCreateMon() {
				return jsonOrderCreateMon;
			}

			public JsonFormOrder getOrderForm() {
				return jsonOrderForm;
			}

		}
		FormWithOrder formWithOrder = new FormWithOrder();
		formWithOrder.jsonOrderCreateMon = jsonOrderCreateMon;
		formWithOrder.jsonOrderForm = jsonOrderForm;
		return ResponseEntity.ok(formWithOrder);
	}


}