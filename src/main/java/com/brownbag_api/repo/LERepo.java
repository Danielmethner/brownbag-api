package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.Asset;
import com.brownbag_api.model.LegalEntity;
import com.brownbag_api.model.Position;
import com.brownbag_api.model.User;
import com.brownbag_api.model.data.ELEType;

@Repository
public interface LERepo extends JpaRepository<LegalEntity, Long> {

	List<LegalEntity> findByManagerAndLegalEntityType(User manager, ELEType eLEType);

}