package com.brownbag_api.model.data;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brownbag_api.model.enums.EAsset;
import com.brownbag_api.model.enums.ECtrlVar;
import com.brownbag_api.model.enums.ELegalForm;
import com.brownbag_api.model.enums.EOrderDir;
import com.brownbag_api.model.enums.EOrderType;
import com.brownbag_api.model.enums.EParty;
import com.brownbag_api.model.enums.ERole;
import com.brownbag_api.model.enums.EUser;
import com.brownbag_api.model.jpa.CtrlVar;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjAssetLoan;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjRole;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.model.jpa.OrderStex;
import com.brownbag_api.repo.RoleRepo;
import com.brownbag_api.service.AssetSvc;
import com.brownbag_api.service.ControlSvc;
import com.brownbag_api.service.OrderCreateMonSvc;
import com.brownbag_api.service.OrderStexSvc;
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
	private ControlSvc controlSvc;

	@Autowired
	private AssetSvc assetSvc;

	@Autowired
	private PartySvc partySvc;

	@Autowired
	private OrderStexSvc orderStexSvc;

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
		ObjAsset asset = assetSvc.getByEnum(eAsset);
		if (asset == null) {
			ObjParty issuer = partySvc.getByEnum(eAsset.getParty());
			return assetSvc.createAssetStex(eAsset.getName(), null, eAsset.getAssetGrp(), issuer, 1);
		} else {
			return asset;
		}

	}

	// -----------------------------------------------------------
	// USER
	// -----------------------------------------------------------
	public void createUser(EUser eUser, Set<String> roles) {
		userSvc.registerUser(eUser.toString(), eUser.getName(), eUser.toString(), roles);
	}

	// -----------------------------------------------------------
	// USERS
	// -----------------------------------------------------------
	private void createOrgUsers() {
		// ECB
		Set<String> roles = new HashSet<>();
		roles.add(EUser.MGR_ECB.getRole().getName());
		createUser(EUser.MGR_ECB, roles);
		// ECB
		roles = new HashSet<>();
		roles.add(EUser.MGR_GOVERNMENT.getRole().getName());
		createUser(EUser.MGR_GOVERNMENT, roles);

	}

	// -----------------------------------------------------------
	// EUR CENTRAL BANK
	// -----------------------------------------------------------
	public void createCentralBank() {
		ObjParty ecb = partySvc.createParty(EParty.ECB);
		if (ecb == null) {
			ecb = partySvc.getByEnum(EParty.ECB);
		}

		if (assetSvc.getByEnum(EAsset.EUR) == null) {
			// ECB is issuer of EUR, hence needs to be created beforehand
			ObjAsset assetEUR = createAsset(EAsset.EUR);
			posSvc.createMacc(assetEUR.getIssuer(), 100000000, assetEUR);

		}

		if (assetSvc.getByEnum(EAsset.LOAN_GENERIC) == null) {
			ObjAsset assetLoanVanilla = new ObjAsset(EAsset.LOAN_GENERIC.getName(), null,
					EAsset.LOAN_GENERIC.getAssetGrp(), 0, ecb, 1);

			ObjAssetLoan assetLoan = new ObjAssetLoan(assetLoanVanilla, 0);
			assetSvc.save(assetLoan);
		}

	}

	// -----------------------------------------------------------
	// USERS
	// -----------------------------------------------------------
	private void createBrokerUser() {
		// BROKER
		Set<String> roles = new HashSet<>();
		roles.add(EUser.MGR_BROKER.getRole().getName());
		roles.add(ERole.ROLE_MGR.getName());
		createUser(EUser.MGR_BROKER, roles);

		// DEUTSCHE BANK
		roles = new HashSet<>();
		roles.add(EUser.MGR_DEUTSCHE_BANK.getRole().getName());
		createUser(EUser.MGR_DEUTSCHE_BANK, roles);
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

		// GO PUBLIC: Convert ownership into 10,000 shares
		deutscheBank = partySvc.goPublic(deutscheBank, 10000);
		
		// raise capital by offering 5,000 shares
		if (deutscheBank != null) {
			partySvc.issueShares(deutscheBank, 5000, 15);
		}

	}

	// -----------------------------------------------------------
	// PERSONS - created after central bank since they receive a money account by
	// default
	// -----------------------------------------------------------
	public void createUsers() {
		Set<String> roles = new HashSet<>();
		roles.add(EUser.U_TRADER_1.getRole().getName());

		// MANAGERS
		createUser(EUser.U_TRADER_1, roles);
		createUser(EUser.U_TRADER_2, roles);
		
		// COMPANY FOR TRADER_1
		ObjUser userTrader1 = userSvc.getByEnum(EUser.U_TRADER_1);
		ObjParty partyPrivTrader1 = userSvc.getNaturalPerson(userTrader1);
		partySvc.createLegalPerson("Trader 1: Business 1", ELegalForm.CORP, userTrader1, partyPrivTrader1, 15000, 100000);
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
		OrderStex orderBuy = orderStexSvc.placeNewOrder(EOrderDir.BUY, EOrderType.STEX, deutscheBank, 3000, 16,
				userTrader1, partyTrader1);

		if (orderBuy != null) {
			OrderStex orderSell = orderStexSvc.getByAssetAndDir(deutscheBank, EOrderDir.SELL).get(0);
			orderStexSvc.matchOrders(orderBuy, orderSell);
		}

//		ObjUser userTrader2 = userSvc.getByEnum(EUser.U_TRADER_2);
//		ObjParty partyTrader2 = userSvc.getNaturalPerson(userTrader2);
//		OrderStex orderBuy2 = orderStexSvc.placeNewOrder(EOrderDir.SELL, EOrderType.STEX, deutscheBank, 50, 100.00,
//				userTrader2, partyTrader2);

	}

	private void createOrders() {
		createOrdersStex();
	}

	private void createCtrlVars() {

		controlSvc.create(ECtrlVar.FIN_DATE, controlSvc.getCurrentDate());
		controlSvc.create(ECtrlVar.DEMO_DATA_CREATED, false);
		controlSvc.create(ECtrlVar.NATP_INIT_DEPOSIT_AMT, 250000);
		controlSvc.create(ECtrlVar.NATP_INIT_DEPOSIT_DURATION, 40);
	}

	private void setCtrlVars() {
		CtrlVar demoDataCreate = controlSvc.getByEnum(ECtrlVar.DEMO_DATA_CREATED);
		if (demoDataCreate == null) {
			createCtrlVars();
			// LOAD FIN DATE FROM DATABASE
			controlSvc.getFinDate();
		}
	}

	public void createDemoData() {
		setCtrlVars();
		if (controlSvc.getByEnum(ECtrlVar.DEMO_DATA_CREATED).isValBool()) {
			System.out.println("Demo data was already loaded in a previous run.");
			return;
		}
		createOrgUsers();
		createCentralBank();
		createBrokerUser();
		createLegalEntities();
		createAssets();
		createUsers();
		createOrders();
		controlSvc.setVal(ECtrlVar.DEMO_DATA_CREATED, true);
		System.err.println("Demo Data Loaded Succesfully");
	}

}
