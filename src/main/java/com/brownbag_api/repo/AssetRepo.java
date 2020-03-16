package com.brownbag_api.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.Asset;
import com.brownbag_api.security.model.User;

@Repository
public interface AssetRepo extends JpaRepository<Asset, Long> {
	Optional<User> findByName(String name);
	List<Asset> findAllByIsShare(boolean isShare);

}