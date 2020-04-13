package com.brownbag_api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.brownbag_api.model.data.EBookType;
import com.brownbag_api.model.data.EOrderStatus;
import com.brownbag_api.model.data.EOrderType;

@Entity
@Table(name = "order_pay")
public class OrderPay extends Order implements Serializable {

	private static final long serialVersionUID = 4643589803146964779L;

	@NotNull
	@ManyToOne(targetEntity = Pos.class)
	@JoinColumn(name = "POS_SEND_ID")
	private Pos posSend;

	@NotNull
	@ManyToOne(targetEntity = Pos.class)
	@JoinColumn(name = "POS_RCV_ID")
	private Pos posRcv;

	public OrderPay() {
		super();
	}

	public OrderPay(@NotNull int qty, @NotNull Asset asset, EOrderType orderType, EOrderStatus orderStatus,
			@NotNull User user, @NotNull Pos posSend, @NotNull Pos posRcv, @NotNull EBookType bookType,
			@NotNull String advText) {

		super(qty, asset, orderType, orderStatus, user, bookType, advText);
		this.posSend = posSend;
		this.posRcv = posRcv;

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
