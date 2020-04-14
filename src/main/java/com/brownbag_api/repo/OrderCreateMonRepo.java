package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.Order;
import com.brownbag_api.model.OrderPay;
import com.brownbag_api.model.User;

@Repository
public interface OrderCreateMonRepo extends JpaRepository<OrderPay, Long> {

	List<Order> findByUser(User user);


}