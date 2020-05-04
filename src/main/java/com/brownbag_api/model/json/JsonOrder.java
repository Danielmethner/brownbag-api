package com.brownbag_api.model.json;

import java.util.Date;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.brownbag_api.model.enums.EAssetGrp;
import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;
import com.brownbag_api.model.jpa.ObjAsset;
import com.brownbag_api.model.jpa.ObjParty;
import com.brownbag_api.model.jpa.ObjUser;
import com.brownbag_api.model.jpa.Order;
import com.brownbag_api.model.jpa.OrderPay;

public class JsonOrder {

	private Long id;

	private double qty;

	private EOrderType orderType;

	private EOrderStatus orderStatus;

	private String advText;

	private String assetName;

	private Date timestampCreate;

	private Date timestampModified;

	private String userName;

	public JsonOrder(Order order) {
		this.id = order.getId();
		this.qty = order.getQty();
		this.orderType = order.getOrderType();
		this.orderStatus = order.getOrderStatus();
		this.advText = order.getAdvText();
		this.assetName = order.getAsset().getName();
		this.timestampCreate = order.getTimestampCreate();
		this.timestampModified = order.getTimestampModified();
		this.userName = order.getUser().getName();
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

}
