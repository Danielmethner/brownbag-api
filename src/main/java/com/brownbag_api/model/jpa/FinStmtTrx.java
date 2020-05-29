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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "BAL_TRX")
public class FinStmtTrx implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1773057542765188702L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotNull
	@ManyToOne(targetEntity = Order.class)
	@JoinColumn(name = "ORDER_ID")
	public Order order;

	@NotBlank
	@Size(max = 200)
	private String name;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP", updatable = false)
	private Date timestampCreate;

	@NotNull
	@ManyToOne(targetEntity = ObjFinStmtItem.class)
	@JoinColumn(name = "BAL_SHEET_ITEM_ID")
	private ObjFinStmtItem bsi;

	@NotNull
	@ManyToOne(targetEntity = Booking.class)
	@JoinColumn(name = "BOOKING_ID")
	private Booking booking;

	@NotNull
	@Column(name = "QTY", columnDefinition = "Decimal(20,2)")
	private double qty;

	public FinStmtTrx() {

	}

	public FinStmtTrx(@NotNull Order order, @NotNull ObjFinStmtItem bsi, Booking booking, @NotNull double qty) {
		super();
		this.order = order;
		this.name = genName();
		this.bsi = bsi;
		this.booking = booking;
		this.qty = qty;
	}

	private @NotBlank @Size(max = 100) String genName() {
		return order.getAdvText();
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTimestampCreate() {
		return timestampCreate;
	}

	public void setTimestampCreate(Date timestampCreate) {
		this.timestampCreate = timestampCreate;
	}

	public ObjFinStmtItem getBsi() {
		return bsi;
	}

	public void setBsi(ObjFinStmtItem bsi) {
		this.bsi = bsi;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
