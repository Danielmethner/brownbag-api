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
	@ManyToOne(targetEntity = ObjPosStex.class)
	@JoinColumn(name = "POS_SEND_ID")
	private ObjPosStex posSend;

	@NotNull
	@ManyToOne(targetEntity = ObjPosStex.class)
	@JoinColumn(name = "POS_RCV_ID")
	private ObjPosStex posRcv;

	@NotNull
	@ManyToOne(targetEntity = OrderStex.class)
	@JoinColumn(name = "ORDER_SELL_ID")
	public OrderStex orderSell;

	@NotNull
	@ManyToOne(targetEntity = OrderStex.class)
	@JoinColumn(name = "ORDER_BUY_ID")
	public OrderStex orderBuy;

	@Column(name = "BOOK_TEXT")
	private String bookText;

	@NotNull
	@ManyToOne(targetEntity = ObjAsset.class)
	@JoinColumn(name = "ASSET_ID")
	private ObjAsset asset;

	@NotNull
	@Column(name = "QTY_EXEC", columnDefinition = "Decimal(20,2)")
	private int qtyExec;

	@NotNull
	@Column(name = "PRICE", columnDefinition = "Decimal(20,2)")
	private double price;

	public ExecStex() {
	}

	public ExecStex(@NotNull ObjPosStex posSend, @NotNull ObjPosStex posRcv, @NotNull OrderStex orderSell,
			@NotNull OrderStex orderBuy, String bookText, @NotNull int qtyExec,
			@NotNull double price) {
		super();
		this.posSend = posSend;
		this.posRcv = posRcv;
		this.orderSell = orderSell;
		this.orderBuy = orderBuy;
		this.bookText = bookText;
		this.asset = orderSell.getAsset();
		this.qtyExec = qtyExec;
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

	public ObjPosStex getPosSend() {
		return posSend;
	}

	public void setPosSend(ObjPosStex posSend) {
		this.posSend = posSend;
	}

	public ObjPosStex getPosRcv() {
		return posRcv;
	}

	public void setPosRcv(ObjPosStex posRcv) {
		this.posRcv = posRcv;
	}

	public OrderStex getOrderSell() {
		return orderSell;
	}

	public void setOrderSell(OrderStex orderSell) {
		this.orderSell = orderSell;
	}

	public OrderStex getOrderBuy() {
		return orderBuy;
	}

	public void setOrderBuy(OrderStex orderBuy) {
		this.orderBuy = orderBuy;
	}

	public String getBookText() {
		return bookText;
	}

	public void setBookText(String bookText) {
		this.bookText = bookText;
	}

	public int getQtyExec() {
		return qtyExec;
	}

	public void setQtyExec(int qtyExec) {
		this.qtyExec = qtyExec;
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

	public double getAmtExec() {
		return this.price * this.qtyExec;
	}

	public ObjAsset getAsset() {
		return asset;
	}

	public void setAsset(ObjAsset asset) {
		this.asset = asset;
	}

}