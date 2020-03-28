package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.Order;
import com.brownbag_api.model.OrderTrans;
import com.brownbag_api.model.User;

@Repository
public interface OrderTransRepo extends JpaRepository<OrderTrans, Long> {

}