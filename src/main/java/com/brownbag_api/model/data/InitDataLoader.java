package com.brownbag_api.model.data;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brownbag_api.model.Asset;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.security.model.ERole;
import com.brownbag_api.security.model.Role;
import com.brownbag_api.security.repo.RoleRepo;
import com.brownbag_api.security.repo.UserRepo;
import com.brownbag_api.security.svc.UserSvc;

@Component
public class InitDataLoader {

	@Autowired
	private AssetRepo assetRepo;
	
	private void createAsset(String name, boolean isShare) {
		
		Asset asset = new Asset(name, isShare);
		assetRepo.save(asset);
	}

	public void createAssets() {
		createAsset("Cash", false);
		createAsset("Government Bond", true);
		createAsset("Deutsche Bank", true);
	}
	
	
	
}
