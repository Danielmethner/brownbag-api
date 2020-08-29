package com.brownbag_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.enums.EParty;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjPosMacc;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.model.jpa.OrderLoan;
import com.brownbag_api.model.json.JsonOrderLoan;
import com.brownbag_api.repo.PosRepo;
import com.brownbag_api.repo.UserRepo;
import com.brownbag_api.security.svc.UserDetailsImpl;
import com.brownbag_api.service.AssetSvc;
import com.brownbag_api.service.ControlSvc;
import com.brownbag_api.service.LogSvc;
import com.brownbag_api.service.OrderLoanSvc;
import com.brownbag_api.service.OrderSvc;
import com.brownbag_api.service.PartySvc;
import com.brownbag_api.service.PosSvc;
import com.brownbag_api.service.UserSvc;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order/loan")
public class OrderLoanController {

	@Autowired
	PosRepo posRepo;

	@Autowired
	PosSvc posSvc;

	@Autowired
	OrderSvc orderSvc;

	@Autowired
	ControlSvc controlSvc;

	@Autowired
	OrderLoanSvc orderLoanSvc;

	@Autowired
	UserRepo userRepo;

	@Autowired
	AssetSvc assetSvc;

	@Autowired
	UserSvc userSvc;

	@Autowired
	PartySvc partySvc;

	@Autowired
	LogSvc logSvc;

	/*
	 * CONVERT JPA TO JSON
	 */
//	private List<JsonOrderLoan> jpaToJson(List<OrderLoan> jpaOrderLoanList) {
//		List<JsonOrderLoan> jsonOrderLoanList = new ArrayList<JsonOrderLoan>();
//		for (OrderLoan jpaOrderLoan : jpaOrderLoanList) {
//			JsonOrderLoan jsonOrderLoan = new JsonOrderLoan(jpaOrderLoan);
//			jsonOrderLoanList.add(jsonOrderLoan);
//		}
//		return jsonOrderLoanList;
//	}

	/*
	 * GET OBJ_USER BY AUTH OBJ
	 */
	private ObjUser getByAuthentication(Authentication authentication) {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		ObjUser objUser = userRepo.findById(userDetailsImpl.getId()).orElseThrow(
				() -> new RuntimeException("ERROR API: User not found. USER.ID: " + userDetailsImpl.getId()));
		return objUser;
	}

	// ----------------------------------------------------------------------
	// PLACE NEW ORDER
	// ----------------------------------------------------------------------
	@PostMapping(value = "/place", consumes = "application/json")
	public ResponseEntity<?> placeOrder(@RequestBody JsonOrderLoan jsonOrderLoan, Authentication authentication) {
		ObjUser user = getByAuthentication(authentication);
		ObjParty partyDebtor = partySvc.getById(jsonOrderLoan.getPartyId());

		if (partyDebtor.getId() == null) {
			return ResponseEntity.ok("ERROR API: Party could not be found. Party ID: " + jsonOrderLoan.getId());
		}

		ObjPosMacc maccGrant = jsonOrderLoan.getMaccLender();
		if (maccGrant == null) {
			ObjParty partyLender = partySvc.getByEnum(EParty.ECB);
			maccGrant = (ObjPosMacc) posSvc.getByParty(partyLender).get(0);
		}

		ObjPosMacc maccRcv = jsonOrderLoan.getMaccDebtor();

		if (maccRcv == null) {
			maccRcv = (ObjPosMacc) posSvc.getByParty(partyDebtor).get(0);
		}

		double intrRate = jsonOrderLoan.getIntrRate() > 0 ?  jsonOrderLoan.getIntrRate() : controlSvc.getIntrRate();
		OrderLoan orderLoan = orderLoanSvc.createLoan(jsonOrderLoan.getQty(), user, maccGrant, maccRcv,
				jsonOrderLoan.getMatDate(), intrRate);

		if (orderLoan == null) {
			return ResponseEntity.ok("ERROR API: Order Creation failed. Order Type: " + jsonOrderLoan.getOrderType()
					+ " Amount: " + (int) jsonOrderLoan.getQty() + " User: " + user.getName() + " Party: "
					+ partyDebtor.getName() + ". Please Check logs for more details.");
		}

		orderLoanSvc.grantLoan(orderLoan);
		return ResponseEntity.ok("Order has been placed successfully!");
	}

}