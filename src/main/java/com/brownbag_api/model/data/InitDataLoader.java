package com.brownbag_api.model.data;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.EDirection;
import com.brownbag_api.model.Order;
import com.brownbag_api.model.User;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.security.repo.UserRepo;

@Component
public class InitDataLoader {

	@Autowired
	private AssetRepo assetRepo;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	UserRepo userRepo;

	private void createAsset(String name, boolean isShare, User issuer) {
		Asset asset = new Asset(name, isShare, issuer);
		assetRepo.save(asset);
	}

	public void createAssets() {
		User issCentralBank = userRepo.findByUsername(EIssuer.ISS_CENTRAL_BANK.toString()).orElseThrow(
				() -> new UsernameNotFoundException("User Not Found with username: " + EIssuer.ISS_CENTRAL_BANK));
		createAsset("Cash", false, issCentralBank);
		User issGovernment = userRepo.findByUsername(EIssuer.ISS_GOVERNMENT.toString()).orElseThrow(
				() -> new UsernameNotFoundException("User Not Found with username: " + EIssuer.ISS_GOVERNMENT));
		createAsset("Government Bond", true, issGovernment);
		User issDeutscheBank = userRepo.findByUsername(EIssuer.ISS_DEUTSCHE_BANK.toString()).orElseThrow(
				() -> new UsernameNotFoundException("User Not Found with username: " + EIssuer.ISS_DEUTSCHE_BANK));
		createAsset("Deutsche Bank", true, issDeutscheBank);
	}

	private void createOrder(EDirection eDirection, Asset asset, int qty, double price, @NotNull User user) {
		Order order = new Order(eDirection, asset, qty, price, user);
		orderRepo.save(order);
	}

	public void createOrders() {
		Asset deutscheBank = assetRepo.findByName("Deutsche Bank");
		Asset govBond = assetRepo.findByName("Government Bond");
		User userTrader = userRepo.findByUsername(EUser.U_TRADER_1.toString()).orElseThrow(
				() -> new UsernameNotFoundException("User Not Found with username: " + EUser.U_TRADER_1));
		createOrder(EDirection.BUY, deutscheBank, 10, 100, userTrader);
		createOrder(EDirection.BUY, govBond, 120, 200, userTrader);
	}
}
