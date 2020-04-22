package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.data.EAssetGrp;

@Repository
public interface AssetRepo extends JpaRepository<Asset, Long> {
	Asset findByName(String name);
	
	Asset findByIssuer(Party party);

	List<Asset> findAllByAssetGrp(EAssetGrp eAssetGrp);

}