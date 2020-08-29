package com.brownbag_api.model.enums;

import java.util.ArrayList;
import java.util.List;

import com.brownbag_api.model.json.JsonEOrderAction;

public enum EEntityType {
	// MASTER DATA
	OBJ_PARTY("Party", EDataKind.MASTER_DATA), OBJ_ASSET("Asset", EDataKind.MASTER_DATA),
	OBJ_FIN_STMT("Financial Statement", EDataKind.MASTER_DATA),
	// TRANSACTION DATA
	ORDER_LOAN("Loan", EDataKind.TRX_DATA), ORDER_PAY("Payment", EDataKind.TRX_DATA),
	ORDER_INTR("Interest", EDataKind.TRX_DATA), ORDER_STEX("Stocks", EDataKind.TRX_DATA),
	ORDER_CREATE_MON("Money Creation", EDataKind.TRX_DATA);

	public final String name;
	public final EDataKind dataKind;

	private EEntityType(String name, EDataKind dataKind) {
		this.name = name;
		this.dataKind = dataKind;
	}

	public String getName() {
		return name;
	}

	public EDataKind getDataKind() {
		return dataKind;
	}

	public List<JsonEOrderAction> getJsonEOrderActionListByOldOrderStatus(EOrderStatus oldOrderStatus) {
		List<JsonEOrderAction> orderActionList = new ArrayList<JsonEOrderAction>();
		for (EOrderAction orderAction : EOrderAction.values()) {
			if (orderAction.getEntityType() == this && orderAction.getOldStatus() == oldOrderStatus) {
				orderActionList.add(new JsonEOrderAction(orderAction));
			}
		}
		return orderActionList;
	}

}
