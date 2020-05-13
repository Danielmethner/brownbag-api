package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjPos;

@Repository
public interface PosRepo extends JpaRepository<ObjPos, Long> {

	List<ObjPos> findByParty(ObjParty owner);

	ObjPos findByAssetAndParty(ObjAsset asset, ObjParty party);

}