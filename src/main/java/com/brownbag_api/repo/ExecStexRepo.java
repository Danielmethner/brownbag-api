package com.brownbag_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.jpa.ExecStex;
import com.brownbag_api.model.jpa.ObjAsset;

@Repository
public interface ExecStexRepo extends JpaRepository<ExecStex, Long> {
	

	ExecStex findFirstByAssetOrderByTimestampCreateDesc(ObjAsset asset);
}
