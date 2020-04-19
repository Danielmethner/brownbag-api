package com.brownbag_api.model.data;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.AssetLoan;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.OrderStex;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.Role;
import com.brownbag_api.model.User;
import com.brownbag_api.repo.RoleRepo;
import com.brownbag_api.service.AssetSvc;
import com.brownbag_api.service.OrderCreateMonSvc;
import com.brownbag_api.service.OrderPaySvc;
import com.brownbag_api.service.OrderSvc;
import com.brownbag_api.service.PartySvc;
import com.brownbag_api.service.PosSvc;
import com.brownbag_api.service.UserSvc;
import com.brownbag_api.util.UtilBA;

@Component
public class InitDataLoader {
	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private UserSvc userSvc;

	@Autowired
	private AssetSvc assetSvc;

	@Autowired
	private PartySvc partySvc;

	@Autowired
	private OrderSvc orderSvc;

	@Autowired
	private OrderPaySvc orderPaySvc;

	@Autowired
	private PosSvc posSvc;

	@Autowired
	OrderCreateMonSvc orderCreateMonSvc;

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
	private Asset createAsset(EAsset eAsset) {
		Party issuer = partySvc.findByEnum(eAsset.getParty());
		Asset asset = new Asset(eAsset.getName(), eAsset.getAssetGrp(), issuer);
		return assetSvc.save(asset);
	}

	// -----------------------------------------------------------
	// USER
	// -----------------------------------------------------------
	public void createUser(EUser eUser) {
		Set<String> roles = new HashSet<>();
		roles.add(eUser.getRole().getName());
		userSvc.registerUser(eUser.toString(), eUser.name(), eUser.toString(), roles);
	}

	// -----------------------------------------------------------
	// USERS
	// -----------------------------------------------------------
	private void createOrgUsers() {
		// ORGANISATIONS
		createUser(EUser.MGR_ECB);
		createUser(EUser.MGR_DEUTSCHE_BANK);
		createUser(EUser.MGR_GOVERNMENT);
		// BROKER
		createUser(EUser.U_BROKER);
	}
	

	// -----------------------------------------------------------
	// EUR CENTRAL BANK
	// -----------------------------------------------------------
	public void createCentralBank() {
		Party ecb = partySvc.createParty(EParty.ECB);
		// ECB is issuer of EUR, hence needs to be created beforehand
		Asset assetEUR = createAsset(EAsset.EUR);
		posSvc.createMacc(0, assetEUR.getIssuer(), 100000000);
		Asset assetLoanVanilla = new Asset(EAsset.LOAN_GENERIC.getName(), EAsset.LOAN_GENERIC.getAssetGrp(), ecb);
		AssetLoan assetLoan = new AssetLoan(assetLoanVanilla);
		assetSvc.save(assetLoan);
	}

	// -----------------------------------------------------------
	// LEGAL ENTITIES
	// -----------------------------------------------------------
	public void createLegalEntities() {
		partySvc.createParty(EParty.DEUTSCHE_BANK);
		partySvc.createParty(EParty.GOVERNMENT);
		partySvc.createParty(EParty.BROKER);
	}

	// -----------------------------------------------------------
	// ASSETS
	// -----------------------------------------------------------
	private void createAssets() {
		createAsset(EAsset.BOND_GOVERNMENT);
		createAsset(EAsset.DEUTSCHE_BANK);
	}

	// -----------------------------------------------------------
	// PERSONS - created after central bank since they receive a money account by default
	// -----------------------------------------------------------
	public void createUsers() {
		// MANAGERS
		createUser(EUser.U_TRADER_1);
//		createUser(EUser.U_TRADER_2);
	}

	// -----------------------------------------------------------
	// ORDERS
	// -----------------------------------------------------------
	private void createOrderPay(double qty, String bookText, Pos maccSend, Pos maccRcv) {

		User user = maccSend.getParty().getUser();

		OrderPay orderPay = orderPaySvc.createPay(qty, user, null, bookText, maccSend, maccRcv);
//		System.out.println(orderPay.getAdvText());
		orderSvc.execAction(orderPay, EOrderAction.HOLD);
		orderPaySvc.execPay(orderPay);

	}

	private void createOrdersPay() {

		double amount = 25;
		
		// GET MACC - SENDER
		User userSend = userSvc.getByEnum(EUser.U_TRADER_1);
		Party leSend = userSvc.getNaturalPerson(userSend);
		Pos maccSend = partySvc.getMacc(leSend);

		// GET MACC - RECIPIENT
		User userRcv = userSvc.getByEnum(EUser.U_TRADER_2);
		Party leRcv = userSvc.getNaturalPerson(userRcv);
		Pos maccRcv = partySvc.getMacc(leRcv);

		createOrderPay(amount, "Test Payment from Trader_1 to Trader_2", maccSend, maccRcv);
	}

	private void createOrderStex(EOrderDir orderDir, EOrderType orderType, Asset asset, int qty, double price,
			@NotNull User user, @NotNull EOrderStatus orderStatus) {
		OrderStex orderStex = new OrderStex(orderDir, qty, asset, orderType, orderStatus, user, price);
		orderSvc.execAction(orderStex, EOrderAction.HOLD);
	}

	private void createOrdersStex() {

		Asset deutscheBank = assetSvc.getByEnum(EAsset.DEUTSCHE_BANK);
		Asset govBond = assetSvc.getByEnum(EAsset.BOND_GOVERNMENT);

		User userTrader = userSvc.getByEnum(EUser.U_TRADER_1);
		createOrderStex(EOrderDir.BUY, EOrderType.STEX, deutscheBank, 10, 100.55, userTrader, EOrderStatus.NEW);
		createOrderStex(EOrderDir.SELL, EOrderType.STEX, govBond, 20, 49.33, userTrader, EOrderStatus.NEW);
	}

	private void createOrders() {
//		createOrdersPay();
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
