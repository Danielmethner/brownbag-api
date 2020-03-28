package com.brownbag_api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

import com.brownbag_api.model.data.EOrderAction;
import com.brownbag_api.model.data.EOrderDir;
import com.brownbag_api.model.data.EOrderStatus;
import com.brownbag_api.model.data.EOrderType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "ORDER_TRANS")
public class OrderTrans implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotNull
	@ManyToOne(targetEntity = Order.class)
	@JoinColumn(name = "ORDER_ID")
	public Order order;

	@Enumerated(EnumType.STRING)
	@Column(length = 4)
	private EOrderAction orderAction;

	@Enumerated(EnumType.STRING)
	@Column(length = 4)
	private EOrderStatus orderStatusOld;

	@Enumerated(EnumType.STRING)
	@Column(length = 4)
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
