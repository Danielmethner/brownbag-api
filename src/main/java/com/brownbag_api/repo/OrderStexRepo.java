package com.brownbag_api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brownbag_api.model.enums.EOrderDir;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.model.jpa.Order;
import com.brownbag_api.model.jpa.OrderPay;
import com.brownbag_api.model.jpa.OrderStex;

@Repository
public interface OrderStexRepo extends JpaRepository<OrderStex, Long> {

	List<OrderStex> findByAsset(ObjAsset asset);

	List<OrderStex> findByAssetAndOrderDir(ObjAsset asset, EOrderDir orderDir);

}