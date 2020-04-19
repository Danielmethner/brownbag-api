package com.brownbag_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.BalTrx;

@Repository
public interface BalTrxRepo extends JpaRepository<BalTrx, Long> {

}