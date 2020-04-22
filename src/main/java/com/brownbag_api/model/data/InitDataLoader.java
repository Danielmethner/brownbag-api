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
		Party issuer = partySvc.getByEnum(eAsset.getParty());
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
		Asset assetLoanVanilla = new Asset(EAsset.LOAN_GENERIC.getName(), null, EAsset.LOAN_GENERIC.getAssetGrp(), ecb, 1);
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
		Party deutscheBank = partySvc.getByEnum(EParty.DEUTSCHE_BANK);
		partySvc.goPublic(deutscheBank, "DE0005140008", 10, 10000);
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

		Party pDeutscheBank = partySvc.getByEnum(EParty.DEUTSCHE_BANK);
		Asset deutscheBank = assetSvc.getByIssuer(pDeutscheBank);
		Asset govBond = assetSvc.getByEnum(EAsset.BOND_GOVERNMENT);

		User userTrader1 = userSvc.getByEnum(EUser.U_TRADER_1);
		Party partyTrader1 = userSvc.getNaturalPerson(userTrader1);
		orderStexSvc.placeNewOrder(EOrderDir.BUY, EOrderType.STEX, deutscheBank, 100, 100.55, userTrader1, EOrderStatus.NEW, partyTrader1);
		User userTrader2 = userSvc.getByEnum(EUser.U_TRADER_2);
		Party partyTrader2 = userSvc.getNaturalPerson(userTrader2);
		orderStexSvc.placeNewOrder(EOrderDir.SELL, EOrderType.STEX, deutscheBank, 50, 100.55, userTrader2, EOrderStatus.NEW, partyTrader2);
	}

	private void createOrders() {
		createOrdersStex();
	}


	private void createExecuteStexOrder() {
		
	}
	
	public void createDemoData() {
		createOrgUsers();
		createCentralBank();
		createLegalEntities();
		createAssets();
		createUsers();
		createOrders();
		createExecuteStexOrder();
	}


}
