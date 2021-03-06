package com.brownbag_api.model.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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

	@Column(name = "BOOK_TEXT")
	private String bookText;

	@NotNull
	@Column(name = "POS_BAL_OLD", columnDefinition = "Decimal(20,2)")
	private double posBalOld;

	@NotNull
	@Column(name = "QTY", columnDefinition = "Decimal(20,2)")
	private double qty;

	@NotNull
	@Column(name = "POS_BAL_NEW", columnDefinition = "Decimal(20,2)")
	private double posBalNew;

	@NotNull
	@ManyToOne(targetEntity = ObjPos.class)
	@JoinColumn(name = "POS_ID")
	private ObjPos pos;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP", updatable = false)
	private Date timestampCreate;

	public Booking() {
	}

	public Booking(@NotNull Order order, @NotNull double posBalOld, @NotNull double qty, @NotNull double posBalNew,
			@NotNull ObjPos pos, @NotNull String bookText) {
		super();
		this.order = order;
		this.posBalOld = posBalOld;
		this.qty = qty;
		this.posBalNew = posBalNew;
		this.pos = pos;
		this.bookText = bookText;
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

	public ObjPos getPos() {
		return pos;
	}

	public void setPos(ObjPos pos) {
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

	public String getBookText() {
		return bookText;
	}

	public void setBookText(String bookText) {
		this.bookText = bookText;
	}

}