package com.brownbag_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.jpa.OrderTrans;

@Repository
public interface OrderTransRepo extends JpaRepository<OrderTrans, Long> {

}