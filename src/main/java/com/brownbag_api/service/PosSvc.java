package com.brownbag_api.service;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.Order;
import com.brownbag_api.model.Position;
import com.brownbag_api.model.User;
import com.brownbag_api.repo.OrderRepo;
import com.brownbag_api.repo.PosRepo;

@Service
public class PosSvc {
	
	@Autowired
	private PosRepo posRepo;

	public void createPosition(@NotNull int qty, @NotNull User user, @NotNull Asset asset, @NotNull double priceAvg) {
		Position position = new Position(null, priceAvg, qty, asset, user);
		posRepo.save(position);
	}

}
