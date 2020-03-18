package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.OrderStatus;

@Repository
public interface OrderStatusRepo extends JpaRepository<OrderStatus, Long> {

	OrderStatus findByIntlId(String intlId);

}