package com.brownbag_api.model.json;

import java.util.Date;

import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.Order;

public class JsonOrder {

	private Long id;

	private double qty;

	private EOrderType orderType;

	private EOrderStatus orderStatus;

	private String advText;

	private Long assetId;

	private String assetName;

	private Date timestampCreate;

	private Date timestampModified;

	private Long userId;
	private String userName;

	public JsonOrder(Order order) {
		this.id = order.getId();
		this.qty = order.getQty();
		this.orderType = order.getOrderType();
		this.orderStatus = order.getOrderStatus();
		this.advText = order.getAdvText();
		ObjAsset objAsset = order.getAsset();
		this.assetId = objAsset != null ? objAsset.getId() : null;
		this.assetName = objAsset != null ? objAsset.getName() : null;
		this.timestampCreate = order.getTimestampCreate();
		this.timestampModified = order.getTimestampModified();
		this.userName = order.getUser().getName();
		this.userId = order.getUser().getId();
	}

	public JsonOrder(double qty, EOrderType orderType, EOrderStatus orderStatus, Long assetId, Long userId) {
		super();
		this.qty = qty;
		this.orderType = orderType;
		this.orderStatus = orderStatus;
		this.assetId = assetId;
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public EOrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(EOrderType orderType) {
		this.orderType = orderType;
	}

	public EOrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(EOrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getAdvText() {
		return advText;
	}

	public void setAdvText(String advText) {
		this.advText = advText;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public Date getTimestampCreate() {
		return timestampCreate;
	}

	public void setTimestampCreate(Date timestampCreate) {
		this.timestampCreate = timestampCreate;
	}

	public Date getTimestampModified() {
		return timestampModified;
	}

	public void setTimestampModified(Date timestampModified) {
		this.timestampModified = timestampModified;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

}
