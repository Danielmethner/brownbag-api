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

import com.brownbag_api.model.enums.EOrderAction;
import com.brownbag_api.model.enums.EOrderDir;
import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjPos;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.model.jpa.Order;
import com.brownbag_api.model.jpa.OrderStex;
import com.brownbag_api.model.json.JsonObjAsset;
import com.brownbag_api.model.json.JsonOrderStex;
import com.brownbag_api.repo.OrderStexRepo;
import com.brownbag_api.repo.PosRepo;
import com.brownbag_api.repo.UserRepo;
import com.brownbag_api.security.payload.response.MsgResponse;
import com.brownbag_api.security.svc.UserDetailsImpl;
import com.brownbag_api.service.AssetSvc;
import com.brownbag_api.service.LogSvc;
import com.brownbag_api.service.OrderStexSvc;
import com.brownbag_api.service.OrderSvc;
import com.brownbag_api.service.PartySvc;
import com.brownbag_api.service.PosSvc;
import com.brownbag_api.service.UserSvc;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order/stex")
public class OrderStexController {

	@Autowired
	PosRepo posRepo;

	@Autowired
	PosSvc posSvc;

	@Autowired
	OrderSvc orderSvc;

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

	@Autowired
	LogSvc logSvc;

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
		ObjUser objUser = userRepo.findById(userDetailsImpl.getId()).orElseThrow(
				() -> new RuntimeException("ERROR API: User not found. USER.ID: " + userDetailsImpl.getId()));
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
			return ResponseEntity.badRequest().body(new MsgResponse("ERROR API: No Party ID specified!"));

		ObjParty party = partySvc.getById(partyId);
		if (party == null)
			return ResponseEntity.ok("ERROR API: Party with ID: " + partyId + " could not be found!");

		List<OrderStex> jpaOrderStexList = orderStexSvc.getByParty(party);

		return ResponseEntity.ok(jpaToJson(jpaOrderStexList));
	}

	@GetMapping("/placed/asset/{assetId}")
	public ResponseEntity<?> getPlacedByAsset(Authentication authentication, @PathVariable Long assetId) {

		if (assetId == null) {
			logSvc.write("ERROR API: No Asset ID specified!");
			return ResponseEntity.badRequest().body(new MsgResponse("ERROR API: No Asset ID specified!"));
		}

		ObjAsset asset = assetSvc.getById(assetId);
		if (asset == null)
			return ResponseEntity.ok("ERROR API: Party with ID: " + assetId + " could not be found!");

		List<OrderStex> jpaOrderStexList = orderStexSvc.getByAssetAndStatus(asset, EOrderStatus.PLACED);

		return ResponseEntity.ok(jpaToJson(jpaOrderStexList));
	}

	@GetMapping("/{orderId}/disc")
	public ResponseEntity<?> discard(Authentication authentication, @PathVariable Long orderId) {
		OrderStex orderStex = orderStexSvc.getById(orderId);
		if (orderStex == null) {
			logSvc.write("Order with ID: " + orderId + " could not be found!");
			return ResponseEntity.ok("Order with ID: " + orderId + " could not be found!");
		}
		if (orderStex.getOrderStatus() == EOrderStatus.DISC) {
			String msg = "Order with ID: " + orderId + " has already been discarded!";
			logSvc.write(msg);
			return ResponseEntity.ok(msg);
		}
		orderStex = orderStexSvc.discardOrder(orderStex);
		if (orderStex == null) {
			String msg = "Order with ID: " + orderId + " could not be discarded! Check log for more details";
			logSvc.write(msg);
			return ResponseEntity.ok(msg);
		}
		JsonOrderStex jsonOrderStex = new JsonOrderStex(orderStex);
		return ResponseEntity.ok(jsonOrderStex);
	}

	@GetMapping("/match/buy/{orderBuyId}/sell/{orderSellId}")
	public ResponseEntity<?> match(Authentication authentication, @PathVariable Long orderBuyId,
			@PathVariable Long orderSellId) {
		OrderStex orderStexBuy = orderStexSvc.getById(orderBuyId);
		double qtyExecBuyPre = orderStexBuy.getQtyExec();
		OrderStex orderStexSell = orderStexSvc.getById(orderSellId);
		double qtyExecSellPre = orderStexSell.getQtyExec();
		orderStexSvc.matchOrders(orderStexBuy, orderStexSell);
		orderStexBuy = orderStexSvc.getById(orderBuyId);
		orderStexSell = orderStexSvc.getById(orderSellId);
		if (qtyExecBuyPre == orderStexBuy.getQtyExec()) {
			String msg = "ERROR_API: Buy Order execution Qty was not successfully updated.";
			logSvc.write(msg);
			return ResponseEntity.ok(msg);
		}
		if (qtyExecSellPre == orderStexSell.getQtyExec()) {
			String msg = "ERROR_API: Sell Order execution Qty was not successfully updated.";
			logSvc.write(msg);
			return ResponseEntity.ok(msg);
		}
		return ResponseEntity.ok("Orders were successfully executed!");
	}

	@PostMapping(value = "/place", consumes = "application/json")
	public ResponseEntity<?> placeOrder(@RequestBody JsonOrderStex jsonOrderStex, Authentication authentication) {
		ObjUser user = getByAuthentication(authentication);
		ObjParty party = partySvc.getById(jsonOrderStex.getPartyId());
		if (jsonOrderStex.getAssetId() == null)
			return ResponseEntity.ok("ERROR API: No Asset selected!");

		ObjAsset asset = assetSvc.getById(jsonOrderStex.getAssetId());

		if (jsonOrderStex.getOrderDir() == EOrderDir.SELL) {
			ObjPos assetPos = posSvc.getByAssetAndParty(asset, party);
			if (assetPos == null) {
				return ResponseEntity.ok("ERROR API: The user has no position with this asset!");
			}
			double avblShares = posSvc.getQtyAvbl(assetPos);
			if (avblShares < jsonOrderStex.getQty()) {
				return ResponseEntity.ok("ERROR API: The user does not own enough shares! Available Shares: "
						+ avblShares + " Order Amount: " + jsonOrderStex.getQty());
			}

		}

		OrderStex orderStex = orderStexSvc.placeNewOrder(jsonOrderStex.getOrderDir(), jsonOrderStex.getOrderType(),
				asset, (int) jsonOrderStex.getQty(), jsonOrderStex.getPriceLimit(), user, party);

		if (orderStex == null)
			return ResponseEntity.ok("ERROR API: Direction: " + jsonOrderStex.getOrderDir() + " Order Type: "
					+ jsonOrderStex.getOrderType() + " Asset: " + asset.getName() + " Quantity: "
					+ (int) jsonOrderStex.getQty() + " Price Limit: " + jsonOrderStex.getPriceLimit() + " User: "
					+ user.getName() + " Party: " + party.getName() + ". Please Check logs for more details.");

		orderStexSvc.placeOrder(orderStex);
		return ResponseEntity.ok("Order has been placed successfully!");
	}

}