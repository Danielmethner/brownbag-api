package com.brownbag_api.model.data;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.EDirection;
import com.brownbag_api.model.ERole;
import com.brownbag_api.model.Order;
import com.brownbag_api.model.OrderStatus;
import com.brownbag_api.model.Position;
import com.brownbag_api.model.Role;
import com.brownbag_api.model.User;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.repo.OrderStatusRepo;
import com.brownbag_api.repo.PosRepo;
import com.brownbag_api.security.repo.RoleRepo;
import com.brownbag_api.security.repo.UserRepo;
import com.brownbag_api.security.svc.UserSvc;
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
	private OrderStatusRepo orderStatusRepo;

	@Autowired
	private PosSvc posSvc;

	@Autowired
	private UserRepo userRepo;

	// ---------------------------------------------------------------------
	// CREATE PARAM DATA
	// ---------------------------------------------------------------------
	private void createRole(ERole eRole) {

		if (!roleRepo.existsByName(eRole.name)) {
			Role role = new Role(eRole.toString());
			roleRepo.save(role);
		}
	}

	public void createRoles() {
		createRole(ERole.ROLE_BROKER);
		createRole(ERole.ROLE_TRADER);
		createRole(ERole.ROLE_ISSUER);
	}

	private void createOrderStatus(EOrderStatus eOrderStatus) {
		OrderStatus orderStatus = new OrderStatus(eOrderStatus.getName(), eOrderStatus.getIntlId());
		orderStatusRepo.save(orderStatus);
	}

	private void createOrderStates() {
		createOrderStatus(EOrderStatus.NEW);
		createOrderStatus(EOrderStatus.PLACED);
	}

	public void createParamData() {
		createRoles();
		createOrderStates();
	}

	// ---------------------------------------------------------------------
	// CREATE DEMO DATA
	// ---------------------------------------------------------------------
	public void createUser(EUser eUser, ERole eRole) {
		Set<String> roles = new HashSet<>();
		roles.add(eRole.toString());
		userSvc.registerUser(eUser.toString(), eUser.name(), eUser.toString(), roles);
	}

	private void createIssuerUsers() {
		createUser(EUser.ISS_CENTRAL_BANK, ERole.ROLE_ISSUER);
		createUser(EUser.ISS_DEUTSCHE_BANK, ERole.ROLE_ISSUER);
		createUser(EUser.ISS_GOVERNMENT, ERole.ROLE_ISSUER);
	}

	private void createAsset(String name, boolean isShare, User issuer) {
		Asset asset = new Asset(name, isShare, issuer);
		assetRepo.save(asset);
	}

	private void createAssets() {
		User issCentralBank = userRepo.findByUsername(EUser.ISS_CENTRAL_BANK.toString());
		createAsset(EAsset.CASH.getName(), false, issCentralBank);
		User issGovernment = userRepo.findByUsername(EUser.ISS_GOVERNMENT.toString());
		createAsset(EAsset.GOVERNMENT_BOND.getName(), true, issGovernment);
		User issDeutscheBank = userRepo.findByUsername(EUser.ISS_DEUTSCHE_BANK.toString());
		createAsset(EAsset.DEUTSCHE_BANK.getName(), true, issDeutscheBank);
	}

	public void createUsers() {
		// BROKER
		createUser(EUser.U_BROKER, ERole.ROLE_BROKER);
		// TRADER
		createUser(EUser.U_TRADER_1, ERole.ROLE_TRADER);
	}

	private void createOrder(EDirection eDirection, Asset asset, int qty, double price, @NotNull User user,
			@NotNull OrderStatus orderStatus) {
		Order order = new Order(eDirection, asset, qty, price, user, orderStatus);
		orderRepo.save(order);
	}

	private void createOrders() {
		Asset deutscheBank = assetRepo.findByName(EAsset.DEUTSCHE_BANK.getName());
		Asset govBond = assetRepo.findByName(EAsset.GOVERNMENT_BOND.getName());

		User userTrader = userRepo.findByUsername(EUser.U_TRADER_1.toString());

		OrderStatus orderStatus = orderStatusRepo.findByIntlId(EOrderStatus.NEW.getIntlId());

		createOrder(EDirection.BUY, deutscheBank, 10, 100, userTrader, orderStatus);
		createOrder(EDirection.BUY, govBond, 120, 200, userTrader, orderStatus);
	}

	private void createPosition(int qty, double priceAvg, @NotNull Asset cash, @NotNull User user) {
		posSvc.createPosition(qty, user, cash, priceAvg);
	}

	private void createPositions() {
		Asset cash = assetRepo.findByName(EAsset.DEUTSCHE_BANK.getName());
		User userTrader = userRepo.findByUsername(EUser.U_TRADER_1.toString());
		createPosition(20000, 10, cash, userTrader);
	}

	public void createDemoData() {
		createIssuerUsers();
		createAssets();
		createUsers();
		createOrders();
		createPositions();
	}

}
