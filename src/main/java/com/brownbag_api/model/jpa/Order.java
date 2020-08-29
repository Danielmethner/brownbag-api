package com.brownbag_api.model.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;

@Entity
@Table(name = "order_base")
@Inheritance(strategy = InheritanceType.JOINED)
public class Order implements Serializable {

	private static final long serialVersionUID = -3458221490393509305L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotNull
	@Column(name = "QTY")
	private double qty;

	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private EOrderType orderType;

	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	private EOrderStatus orderStatus;

	@Column(name = "ADV_TEXT")
	private String advText;

	@ManyToOne(targetEntity = ObjAsset.class)
	@JoinColumn(name = "ASSET_ID")
	private ObjAsset asset;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TS_INS", updatable = false)
	private Date timestampCreate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TS_LAST_MODIFIED")
	private Date timestampModified;

	@NotNull
	@ManyToOne(targetEntity = ObjUser.class)
	@JoinColumn(name = "USER_ID")
	private ObjUser user;

	public Order() {
	}

	public Order( double qty, ObjAsset asset, @NotNull EOrderType orderType, EOrderStatus orderStatus,
			@NotNull ObjUser user, String advText) {
		super();
		this.qty = qty;
		this.asset = asset;
		this.orderType = orderType;
		this.orderStatus = orderStatus;
		this.user = user;
		this.advText = advText;

	}

	public EOrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(EOrderType orderType) {
		this.orderType = orderType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ObjAsset getAsset() {
		return asset;
	}

	public void setAsset(ObjAsset asset) {
		this.asset = asset;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public ObjUser getUser() {
		return user;
	}

	public void setUser(ObjUser user) {
		this.user = user;
	}

	public EOrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(EOrderStatus orderStatus) {
		this.orderStatus = orderStatus;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAdvText() {
		return advText;
	}

	public void setAdvText(String advText) {
		this.advText = advText;
	}

}
