package com.brownbag_api.model.data;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.Order;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.OrderStex;
import com.brownbag_api.model.LegalEntity;
import com.brownbag_api.model.Role;
import com.brownbag_api.model.User;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.OrderPayRepo;
import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.repo.OrgRepo;
import com.brownbag_api.repo.RoleRepo;
import com.brownbag_api.repo.UserRepo;
import com.brownbag_api.security.svc.UserSvc;
import com.brownbag_api.service.OrderSvc;
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
	private OrderRepo orderRepo;

	@Autowired
	private OrderPayRepo orderPayRepo;

	@Autowired
	private OrderSvc orderSvc;
	
	@Autowired
	private OrgRepo orgRepo;

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
	// USERS
	// -----------------------------------------------------------
	public void createUser(EUser eUser, ERole eRole) {
		Set<String> roles = new HashSet<>();
		roles.add(eRole.getName());
		userSvc.registerUser(eUser.toString(), eUser.name(), eUser.toString(), roles);
	}
	
	// -----------------------------------------------------------
	// LEGAL ENTITIES
	// -----------------------------------------------------------
	private void createOrgUsers() {
		createUser(EUser.MGR_CENTRAL_BANK, ERole.ROLE_ORG);
		createUser(EUser.MGR_DEUTSCHE_BANK, ERole.ROLE_ORG);
		createUser(EUser.MGR_GOVERNMENT, ERole.ROLE_ORG);
	}
	
	public void createLegalEntity(ELE eOrg, User manager, ELEType legalEntityType) {
		LegalEntity org = new LegalEntity(eOrg.toString(), manager, legalEntityType);
		orgRepo.save(org);
	}
	
	public void createLegalEntities() {
		User mgr;
		mgr = userRepo.findByUsername(EUser.MGR_CENTRAL_BANK.toString());
		createLegalEntity(ELE.CENTRAL_BANK, mgr, ELEType.ORG_GOVT);
		mgr = userRepo.findByUsername(EUser.MGR_DEUTSCHE_BANK.toString());
		createLegalEntity(ELE.DEUTSCHE_BANK, mgr, ELEType.PERSON_LEGAL);
		mgr = userRepo.findByUsername(EUser.MGR_GOVERNMENT.toString());
		createLegalEntity(ELE.GOVERNMENT, mgr, ELEType.ORG_GOVT);
	}
	
	// -----------------------------------------------------------
	// ASSETS
	// -----------------------------------------------------------
	private void createAsset(String name, boolean isShare, LegalEntity issuer) {
		Asset asset = new Asset(name, isShare, issuer);
		assetRepo.save(asset);
	}

	private void createAssets() {
		LegalEntity orgCentralBank = orgRepo.findByName(ELE.CENTRAL_BANK.toString());
		createAsset(EAsset.CASH.getName(), true, orgCentralBank);
		LegalEntity orgGovernment = orgRepo.findByName(ELE.GOVERNMENT.toString());
		createAsset(EAsset.GOVERNMENT_BOND.getName(), false, orgGovernment);
		LegalEntity orgDeutscheBank = orgRepo.findByName(ELE.DEUTSCHE_BANK.toString());
		createAsset(EAsset.DEUTSCHE_BANK.getName(), false, orgDeutscheBank);
	}

	// -----------------------------------------------------------
	// PERSONS
	// -----------------------------------------------------------
	public void createUsers() {
		// BROKER
		createUser(EUser.U_BROKER, ERole.ROLE_BROKER);
		// MANAGERS
		createUser(EUser.U_TRADER_1, ERole.ROLE_MGR);
		createUser(EUser.U_TRADER_2, ERole.ROLE_MGR);
	}
	
	public void createPerson() {
		
	}

	// -----------------------------------------------------------
	// ORDERS
	// -----------------------------------------------------------

	private void createOrderPay(EOrderType orderType, Asset asset, int qty, @NotNull User user,
			@NotNull EOrderStatus orderStatus, @NotNull String bookText) {
		OrderPay orderPay = new OrderPay(qty, asset, orderType, orderStatus, user, bookText);
		orderPayRepo.save(orderPay);
		orderSvc.execAction(orderPay, EOrderAction.HOLD);
	}

	private void createOrdersPay() {
		Asset cash = assetRepo.findByName(EAsset.CASH.getName());
		User userTrader = userRepo.findByUsername(EUser.U_TRADER_1.toString());
		createOrderPay(EOrderType.PAY, cash, 10, userTrader, EOrderStatus.NEW, "first payment");
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

	// -----------------------------------------------------------
	// POSITIONS TODO (to be generated by Orders)
	// -----------------------------------------------------------
	private void createPosition(int qty, double priceAvg, @NotNull Asset cash, @NotNull LegalEntity owner) {
		posSvc.createPosition(qty, owner, cash, priceAvg);
	}

	private void createPositions() {
		Asset deutscheBank = assetRepo.findByName(EAsset.DEUTSCHE_BANK.getName());
		
//		createPosition(20000, 10, deutscheBank, userTrader1);
//		createPosition(34000, 10, deutscheBank, userTrader2);

	}

	public void createDemoData() {
		createOrgUsers();
		createLegalEntities();
		createAssets();
		createUsers();
		createOrders();
		createPositions();
	}

}
