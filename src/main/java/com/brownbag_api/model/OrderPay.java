package com.brownbag_api.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "order_pay")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderPay extends Order implements Serializable {

	private static final long serialVersionUID = 4643589803146964779L;

	@NotNull
	@OneToOne(targetEntity = Pos.class)
	@JoinColumn(name = "POS_SEND_ID")
	private Pos posSend;

	@NotNull
	@OneToOne(targetEntity = Pos.class)
	@JoinColumn(name = "POS_RCV_ID")
	private Pos posRcv;

	public OrderPay() {
		super();
	}

	public OrderPay(@NotNull double qty, @NotNull Asset asset, EOrderType orderType, EOrderStatus orderStatus,
			@NotNull User user, @NotNull Pos posSend, @NotNull Pos posRcv, String advText) {

		super(qty, asset, orderType, orderStatus, user, advText);
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
