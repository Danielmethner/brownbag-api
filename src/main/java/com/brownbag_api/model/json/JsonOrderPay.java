package com.brownbag_api.model.json;

import com.brownbag_api.model.jpa.OrderPay;

public class JsonOrderPay extends JsonOrder {

	private String partySend;

	private String partyRcv;

	public JsonOrderPay(OrderPay orderPay) {
		super(orderPay);
		this.partySend = orderPay.getPosSend().getParty().getName();
		this.partyRcv = orderPay.getPosRcv().getParty().getName();
	}

}
