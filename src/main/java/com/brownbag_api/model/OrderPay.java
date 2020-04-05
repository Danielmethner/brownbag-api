package com.brownbag_api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.brownbag_api.model.data.EOrderStatus;
import com.brownbag_api.model.data.EOrderType;

@Entity
@Table(name = "order_pay")
public class OrderPay extends Order implements Serializable {

	private static final long serialVersionUID = 4643589803146964779L;

	@NotNull
	@ManyToOne(targetEntity = Position.class)
	@JoinColumn(name = "POS_SEND_ID")
	private Position posSend;

	@NotNull
	@ManyToOne(targetEntity = Position.class)
	@JoinColumn(name = "POS_RCV_ID")
	private Position posRcv;

	@NotNull
	@Column(name = "BOOK_TEXT")
	private String bookText;

	public OrderPay() {
	}

	public OrderPay(@NotNull String bookText) {
		super();
		this.bookText = bookText;
	}

	public OrderPay(@NotNull int qty, @NotNull Asset asset, EOrderType orderType, EOrderStatus orderStatus,
			@NotNull User user, @NotNull Position posSend, @NotNull Position posRcv, @NotNull String bookText) {
		super(qty, asset, orderType, orderStatus, user);
		this.posSend = posSend;
		this.posRcv = posRcv;
		if (bookText == null) {
			this.bookText = "Payment from " + posSend.getOwner().getName() + " to " + posRcv.getOwner().getName();
		} else {
			this.bookText = bookText;
		}

	}

	public String getBookText() {
		return bookText;
	}

	public void setBookText(String bookText) {
		this.bookText = bookText;
	}

	public Position getPosSend() {
		return posSend;
	}

	public void setPosSend(Position posSend) {
		this.posSend = posSend;
	}

	public Position getPosRcv() {
		return posRcv;
	}

	public void setPosRcv(Position posRcv) {
		this.posRcv = posRcv;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
