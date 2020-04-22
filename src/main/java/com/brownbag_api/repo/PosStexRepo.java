package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.Party;
import com.brownbag_api.model.Pos;
import com.brownbag_api.model.PosMacc;
import com.brownbag_api.model.PosStex;

@Repository
public interface PosStexRepo extends JpaRepository<PosStex, Long> {

	PosStex findByAssetAndParty(Asset asset, Party party);

}