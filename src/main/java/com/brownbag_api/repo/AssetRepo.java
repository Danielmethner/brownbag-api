package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.enums.EAssetGrp;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjParty;

@Repository
public interface AssetRepo extends JpaRepository<ObjAsset, Long> {
	ObjAsset findByName(String name);
	
	ObjAsset findByIssuer(ObjParty party);

	List<ObjAsset> findAllByAssetGrp(EAssetGrp eAssetGrp);

}