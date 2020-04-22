package com.brownbag_api.model;

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
@Table(name = "EXEC_STEX")
public class ExecStex implements Serializable {

	private static final long serialVersionUID = -3458221490393509305L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP", updatable = false)
	private Date timestampCreate;

	@NotNull
	@ManyToOne(targetEntity = Pos.class)
	@JoinColumn(name = "POS_SEND_ID")
	private Pos posSend;

	@NotNull
	@ManyToOne(targetEntity = Pos.class)
	@JoinColumn(name = "POS_RCV_ID")
	private Pos posRcv;

	@NotNull
	@ManyToOne(targetEntity = Order.class)
	@JoinColumn(name = "ORDER_BUY_ID")
	public Order orderBuy;

	@NotNull
	@ManyToOne(targetEntity = Order.class)
	@JoinColumn(name = "ORDER_SELL_ID")
	public Order orderSell;

	@Column(name = "BOOK_TEXT")
	private String bookText;

	@NotNull
	@Column(name = "QTY")
	private double qty;

	@NotNull
	@Column(name = "PRICE")
	private double price;

	public ExecStex() {
	}

	public ExecStex(@NotNull Pos posSend, @NotNull Pos posRcv, @NotNull Order orderBuy, @NotNull Order orderSell,
			String bookText, @NotNull double qty, @NotNull double price) {
		super();
		this.posSend = posSend;
		this.posRcv = posRcv;
		this.orderBuy = orderBuy;
		this.orderSell = orderSell;
		this.bookText = bookText;
		this.qty = qty;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTimestampCreate() {
		return timestampCreate;
	}

	public void setTimestampCreate(Date timestampCreate) {
		this.timestampCreate = timestampCreate;
	}

	public Pos getPosSend() {
		return posSend;
	}

	public void setPosSend(Pos posSend) {
		this.posSend = posSend;
	}

	public Pos getPosRcv() {
		return posRcv;
	}

	public void setPosRcv(Pos posRcv) {
		this.posRcv = posRcv;
	}

	public Order getOrderBuy() {
		return orderBuy;
	}

	public void setOrderBuy(Order orderBuy) {
		this.orderBuy = orderBuy;
	}

	public Order getOrderSell() {
		return orderSell;
	}

	public void setOrderSell(Order orderSell) {
		this.orderSell = orderSell;
	}

	public String getBookText() {
		return bookText;
	}

	public void setBookText(String bookText) {
		this.bookText = bookText;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}