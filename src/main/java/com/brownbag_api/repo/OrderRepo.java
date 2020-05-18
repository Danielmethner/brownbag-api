package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.model.jpa.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

	List<Order> findByUser(ObjUser user);

	List<Order> findByAssetAndOrderStatus(ObjAsset asset, EOrderStatus orderStatus);

}