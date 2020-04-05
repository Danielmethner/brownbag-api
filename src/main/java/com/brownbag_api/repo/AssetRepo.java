package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.Asset;

@Repository
public interface AssetRepo extends JpaRepository<Asset, Long> {
	Asset findByName(String name);
	List<Asset> findAllByIsCurry(boolean isCurry);

}