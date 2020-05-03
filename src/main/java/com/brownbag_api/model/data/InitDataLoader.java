package com.brownbag_api.model.data;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brownbag_api.model.ObjAsset;
import com.brownbag_api.model.ObjAssetLoan;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.OrderStex;
import com.brownbag_api.model.ObjParty;
import com.brownbag_api.model.ObjPos;
import com.brownbag_api.model.ObjRole;
import com.brownbag_api.model.ObjUser;
import com.brownbag_api.model.enums.EAsset;
import com.brownbag_api.model.enums.EOrderDir;
import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;
import com.brownbag_api.model.enums.EParty;
import com.brownbag_api.model.enums.ERole;
import com.brownbag_api.model.enums.EUser;
import com.brownbag_api.repo.PartyRepo;
import com.brownbag_api.repo.RoleRepo;
import com.brownbag_api.service.AssetSvc;
import com.brownbag_api.service.OrderCreateMonSvc;
import com.brownbag_api.service.OrderPaySvc;
import com.brownbag_api.service.OrderStexSvc;
import com.brownbag_api.service.OrderSvc;
import com.brownbag_api.service.PartySvc;
import com.brownbag_api.service.PosSvc;
import com.brownbag_api.service.UserSvc;

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
	private OrderStexSvc orderStexSvc;

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
			ObjRole role = new ObjRole(eRole.getName());
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
	private ObjAsset createAsset(EAsset eAsset) {
		ObjParty issuer = partySvc.getByEnum(eAsset.getParty());
		return assetSvc.createAssetStex(eAsset.getName(), null, eAsset.getAssetGrp(), issuer, 1);
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
		createUser(EUser.MGR_BROKER);
	}

	// -----------------------------------------------------------
	// EUR CENTRAL BANK
	// -----------------------------------------------------------
	public void createCentralBank() {
		ObjParty ecb = partySvc.createParty(EParty.ECB);
		// ECB is issuer of EUR, hence needs to be created beforehand
		ObjAsset assetEUR = createAsset(EAsset.EUR);
		posSvc.createMacc(0, assetEUR.getIssuer(), 100000000);
		ObjAsset assetLoanVanilla = new ObjAsset(EAsset.LOAN_GENERIC.getName(), null, EAsset.LOAN_GENERIC.getAssetGrp(), ecb, 1);
		ObjAssetLoan assetLoan = new ObjAssetLoan(assetLoanVanilla);
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
		ObjParty deutscheBank = partySvc.getByEnum(EParty.DEUTSCHE_BANK);
		deutscheBank = partySvc.goPublic(deutscheBank, 10000);
		partySvc.issueShares(deutscheBank, 5000, 15);
	}

	// -----------------------------------------------------------
	// PERSONS - created after central bank since they receive a money account by
	// default
	// -----------------------------------------------------------
	public void createUsers() {
		// MANAGERS
		createUser(EUser.U_TRADER_1);
		createUser(EUser.U_TRADER_2);
	}
	
	// -----------------------------------------------------------
	// ORDERS
	// -----------------------------------------------------------
	private void createOrdersStex() {

		ObjParty pDeutscheBank = partySvc.getByEnum(EParty.DEUTSCHE_BANK);
		ObjAsset deutscheBank = assetSvc.getByIssuer(pDeutscheBank);
//		Asset govBond = assetSvc.getByEnum(EAsset.BOND_GOVERNMENT);

		// BUY SHARES FROM IPO
		ObjUser userTrader1 = userSvc.getByEnum(EUser.U_TRADER_1);
		ObjParty partyTrader1 = userSvc.getNaturalPerson(userTrader1);
		OrderStex orderBuy = orderStexSvc.placeNewOrder(EOrderDir.BUY, EOrderType.STEX, deutscheBank, 1000, 16, userTrader1, partyTrader1);
//		User userTrader2 = userSvc.getByEnum(EUser.U_TRADER_2);
//		Party partyTrader2 = userSvc.getNaturalPerson(userTrader2);
//		OrderStex orderSell = orderStexSvc.placeNewOrder(EOrderDir.SELL, EOrderType.STEX, deutscheBank, 50, 100.00, userTrader2, partyTrader2);
		
		OrderStex orderSell = orderStexSvc.getByAsset(deutscheBank).get(0);
		orderStexSvc.matchOrders(orderBuy, orderSell);
	}

	private void createOrders() {
		createOrdersStex();
	}
	
	public void createDemoData() {
		createOrgUsers();
		createCentralBank();
		createLegalEntities();
		createAssets();
		createUsers();
		createOrders();
		System.err.println("Demo Data Loaded Succesfully");
	}


}
