package com.brownbag_api.model.data;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.OrderStex;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.Role;
import com.brownbag_api.model.User;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.OrderPayRepo;
import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.repo.PartyRepo;
import com.brownbag_api.repo.RoleRepo;
import com.brownbag_api.repo.UserRepo;
import com.brownbag_api.security.svc.UserSvc;
import com.brownbag_api.service.OrderPaySvc;
import com.brownbag_api.service.OrderSvc;
import com.brownbag_api.service.PartySvc;
import com.brownbag_api.service.PosSvc;

@Component
public class InitDataLoader {
	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private UserSvc userSvc;

	@Autowired
	private AssetRepo assetRepo;

	@Autowired
	private PartySvc lESvc;

	@Autowired
	private PartyRepo lERepo;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private OrderPayRepo orderPayRepo;

	@Autowired
	private OrderSvc orderSvc;

	@Autowired
	private OrderPaySvc orderPaySvc;

	@Autowired
	private PosSvc posSvc;

	@Autowired
	private UserRepo userRepo;

	// ---------------------------------------------------------------------
	// ---------------------------------------------------------------------
	// CREATE PARAM DATA
	// ---------------------------------------------------------------------
	// ---------------------------------------------------------------------

	// -----------------------------------------------------------
	// ROLES
	// -----------------------------------------------------------
	private void createRole(ERole eRole) {

		if (!roleRepo.existsByName(eRole.getName())) {
			Role role = new Role(eRole.getName());
			roleRepo.save(role);
		}
	}

	public void createRoles() {
		createRole(ERole.ROLE_BROKER);
		createRole(ERole.ROLE_MGR);
		createRole(ERole.ROLE_ORG);
	}

	public void createParamData() {
		createRoles();
	}
	// ---------------------------------------------------------------------
	// ---------------------------------------------------------------------
	// CREATE DEMO DATA
	// ---------------------------------------------------------------------
	// ---------------------------------------------------------------------

	// -----------------------------------------------------------
	// ASSET
	// -----------------------------------------------------------
	private void createAsset(String name, boolean isShare, Party issuer) {
		Asset asset = new Asset(name, isShare, issuer);
		assetRepo.save(asset);
	}

	// -----------------------------------------------------------
	// USER
	// -----------------------------------------------------------
	public void createUser(EUser eUser, ERole eRole) {
		Set<String> roles = new HashSet<>();
		roles.add(eRole.getName());
		userSvc.registerUser(eUser.toString(), eUser.name(), eUser.toString(), roles);
	}

	// -----------------------------------------------------------
	// USERS
	// -----------------------------------------------------------
	private void createOrgUsers() {
		// ORGANISATIONS
		createUser(EUser.MGR_CENTRAL_BANK, ERole.ROLE_ORG);
		createUser(EUser.MGR_DEUTSCHE_BANK, ERole.ROLE_ORG);
		createUser(EUser.MGR_GOVERNMENT, ERole.ROLE_ORG);
		// BROKER
		createUser(EUser.U_BROKER, ERole.ROLE_BROKER);
	}

	public void createCentralBank() {
		User mgr;
		mgr = userRepo.findByUsername(EUser.MGR_CENTRAL_BANK.toString());
		Party orgCentralBank = lESvc.createParty(EParty.CENTRAL_BANK, mgr, EPartyType.ORG_GOVT, false);
		createAsset(EAsset.CASH.getName(), true, orgCentralBank);
		posSvc.createMacc(0, orgCentralBank, 100000000);
	}

	// -----------------------------------------------------------
	// LEGAL ENTITIES
	// -----------------------------------------------------------
	public void createLegalEntities() {
		User mgr;
		mgr = userRepo.findByUsername(EUser.MGR_DEUTSCHE_BANK.toString());
		lESvc.createParty(EParty.DEUTSCHE_BANK, mgr, EPartyType.PERSON_LEGAL, true);
		mgr = userRepo.findByUsername(EUser.MGR_GOVERNMENT.toString());
		lESvc.createParty(EParty.GOVERNMENT, mgr, EPartyType.ORG_GOVT, true);
		mgr = userRepo.findByUsername(EUser.U_BROKER.toString());
		lESvc.createParty(EParty.BROKER, mgr, EPartyType.PERSON_LEGAL, true);
	}

	// -----------------------------------------------------------
	// ASSETS
	// -----------------------------------------------------------
	private void createAssets() {
		Party orgGovernment = lERepo.findByName(EParty.GOVERNMENT.toString());
		createAsset(EAsset.GOVERNMENT_BOND.getName(), false, orgGovernment);
		Party orgDeutscheBank = lERepo.findByName(EParty.DEUTSCHE_BANK.toString());
		createAsset(EAsset.DEUTSCHE_BANK.getName(), false, orgDeutscheBank);
	}

	// -----------------------------------------------------------
	// PERSONS
	// -----------------------------------------------------------
	public void createUsers() {
		// MANAGERS
		createUser(EUser.U_TRADER_1, ERole.ROLE_MGR);
		createUser(EUser.U_TRADER_2, ERole.ROLE_MGR);
	}

	// -----------------------------------------------------------
	// ORDERS
	// -----------------------------------------------------------

	private void createOrderPay(int qty, @NotNull EUser eUser,
			String bookText, Pos maccSend, Pos maccRcv) {
		
		User user = userRepo.findByUsername(maccSend.getParty().getUser().getUsername());
		
		OrderPay orderPay = orderPaySvc.createPay(qty, user, null, bookText, maccSend, maccRcv);
		orderSvc.execAction(orderPay, EOrderAction.HOLD);
		orderPaySvc.execPay(orderPay);

	}

	private void createOrdersPay() {

		// GET MACC - SENDER
		Party leSend = lERepo.findByName(EParty.CENTRAL_BANK.toString());
		Pos maccSend = lESvc.getMacc(leSend);

		// GET MACC - RECIPIENT
		User userRcv = userRepo.findByUsername(EUser.U_TRADER_1.toString());
		Party leRcv = userSvc.getNaturalPerson(userRcv);
		Pos maccRcv = lESvc.getMacc(leRcv);

		createOrderPay(10, EUser.U_TRADER_1, null, maccSend, maccRcv);
	}

	private void createOrderStex(EOrderDir orderDir, EOrderType orderType, Asset asset, int qty, double price,
			@NotNull User user, @NotNull EOrderStatus orderStatus) {
//		System.out.println("Price: " + price);
		OrderStex orderStex = new OrderStex(qty, asset, orderType, orderStatus, user, price);
//		System.out.println("Price: " + orderStex.getPrice());
		orderSvc.execAction(orderStex, EOrderAction.HOLD);
	}

	private void createOrdersStex() {

		Asset deutscheBank = assetRepo.findByName(EAsset.DEUTSCHE_BANK.getName());
		Asset govBond = assetRepo.findByName(EAsset.GOVERNMENT_BOND.getName());

		User userTrader = userRepo.findByUsername(EUser.U_TRADER_1.toString());
		createOrderStex(EOrderDir.BUY, EOrderType.STEX, deutscheBank, 10, 100.55, userTrader, EOrderStatus.NEW);
		createOrderStex(EOrderDir.SELL, EOrderType.STEX, govBond, 20, 49.33, userTrader, EOrderStatus.NEW);
	}

	private void createOrders() {
		createOrdersPay();
		createOrdersStex();
	}

	public void createDemoData() {
		createOrgUsers();
		createCentralBank();
		createLegalEntities();
		createAssets();
		createUsers();
		createOrders();
	}

}
