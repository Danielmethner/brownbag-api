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

import com.brownbag_api.model.data.EOrderStatus;
import com.brownbag_api.model.data.EOrderType;

@Entity
@Table(name = "booking")
public class Booking implements Serializable {

	private static final long serialVersionUID = -3458221490393509305L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotNull
	@ManyToOne(targetEntity = Order.class)
	@JoinColumn(name = "ORDER_ID")
	public Order order;

	@NotNull
	@Column(name = "POS_BAL_OLD")
	private double posBalOld;

	@NotNull
	@Column(name = "QTY")
	private double qty;

	@NotNull
	@Column(name = "POS_BAL_NEW")
	private double posBalNew;

	@NotNull
	@ManyToOne(targetEntity = Pos.class)
	@JoinColumn(name = "POS_ID")
	private Pos pos;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP", updatable = false)
	private Date timestampCreate;

	@NotNull
	@ManyToOne(targetEntity = BalSheetItem.class)
	@JoinColumn(name = "BAL_SHEET_ITEM_ID")
	private BalSheetItem bsi;

	public Booking() {
	}

	public Booking(@NotNull Order order, @NotNull double posBalOld, @NotNull double qty, @NotNull double posBalNew,
			@NotNull Pos pos, @NotNull BalSheetItem bsi) {
		super();
		this.order = order;
		this.posBalOld = posBalOld;
		this.qty = qty;
		this.posBalNew = posBalNew;
		this.pos = pos;
		this.bsi = bsi;
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

	public double getPosBalOld() {
		return posBalOld;
	}

	public void setPosBalOld(double posBalOld) {
		this.posBalOld = posBalOld;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public double getPosBalNew() {
		return posBalNew;
	}

	public void setPosBalNew(double posBalNew) {
		this.posBalNew = posBalNew;
	}

	public Pos getPos() {
		return pos;
	}

	public void setPos(Pos pos) {
		this.pos = pos;
	}

	public Date getTimestampCreate() {
		return timestampCreate;
	}

	public void setTimestampCreate(Date timestampCreate) {
		this.timestampCreate = timestampCreate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}