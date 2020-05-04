package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjPosStex;

@Repository
public interface PosStexRepo extends JpaRepository<ObjPosStex, Long> {

	List<ObjPosStex> findByAsset(ObjAsset asset);

	ObjPosStex findByAssetAndParty(ObjAsset asset, ObjParty party);

}