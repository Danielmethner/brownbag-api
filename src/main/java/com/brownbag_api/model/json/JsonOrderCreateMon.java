package com.brownbag_api.model.json;

import java.time.LocalDateTime;

import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;
import com.brownbag_api.model.jpa.ObjPosLoan;
import com.brownbag_api.model.jpa.ObjPosMacc;
import com.brownbag_api.model.jpa.OrderCreateMon;
import com.brownbag_api.model.jpa.OrderLoan;

public class JsonOrderCreateMon extends JsonOrder {

	

	public JsonOrderCreateMon(OrderCreateMon orderCreateMon) {
		super(orderCreateMon);
	}

}
