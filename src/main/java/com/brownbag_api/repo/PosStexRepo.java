package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.ObjAsset;
import com.brownbag_api.model.ObjParty;
import com.brownbag_api.model.ObjPos;
import com.brownbag_api.model.ObjPosMacc;
import com.brownbag_api.model.ObjPosStex;

@Repository
public interface PosStexRepo extends JpaRepository<ObjPosStex, Long> {

	List<ObjPosStex> findByAsset(ObjAsset asset);
	
	ObjPosStex findByAssetAndParty(ObjAsset asset, ObjParty party);

}