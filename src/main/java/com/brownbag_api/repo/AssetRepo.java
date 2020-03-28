package com.brownbag_api.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.User;

@Repository
public interface AssetRepo extends JpaRepository<Asset, Long> {
	Asset findByName(String name);
	List<Asset> findAllByIsMacc(boolean isShare);

}