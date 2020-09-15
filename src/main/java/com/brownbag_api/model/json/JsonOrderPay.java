package com.brownbag_api.model.json;

import com.brownbag_api.model.jpa.OrderPay;

public class JsonOrderPay extends JsonOrder {

	private String partySend;

	private String partyRcv;

	public JsonOrderPay(OrderPay orderPay) {
		super(orderPay);
		this.partySend = orderPay.getPosSend() != null ? orderPay.getPosSend().getParty().getName() : null;
		this.partyRcv = orderPay.getPosRcv() != null ? orderPay.getPosRcv().getParty().getName() : null;
	}

	public String getPartySend() {
		return partySend;
	}

	public void setPartySend(String partySend) {
		this.partySend = partySend;
	}

	public String getPartyRcv() {
		return partyRcv;
	}

	public void setPartyRcv(String partyRcv) {
		this.partyRcv = partyRcv;
	}

	
	
}
