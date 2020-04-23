package com.brownbag_api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

import com.brownbag_api.model.enums.EOrderAction;
import com.brownbag_api.model.enums.EOrderStatus;

@Entity
@Table(name = "ORDER_TRANS")
public class OrderTrans implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8877249146176590434L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotNull
	@ManyToOne(targetEntity = Order.class)
	@JoinColumn(name = "ORDER_ID")
	public Order order;

	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private EOrderAction orderAction;

	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	private EOrderStatus orderStatusOld;

	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	private EOrderStatus orderStatusNew;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP", updatable = false)
	private Date timestampCreate;

	public OrderTrans() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public EOrderAction getOrderAction() {
		return orderAction;
	}

	public void setOrderAction(EOrderAction orderAction) {
		this.orderAction = orderAction;
	}

	public EOrderStatus getOrderStatusOld() {
		return orderStatusOld;
	}

	public void setOrderStatusOld(EOrderStatus orderStatusOld) {
		this.orderStatusOld = orderStatusOld;
	}

	public EOrderStatus getOrderStatusNew() {
		return orderStatusNew;
	}

	public void setOrderStatusNew(EOrderStatus orderStatusNew) {
		this.orderStatusNew = orderStatusNew;
	}

	public Date getTimestampCreate() {
		return timestampCreate;
	}

	public void setTimestampCreate(Date timestampCreate) {
		this.timestampCreate = timestampCreate;
	}

}
