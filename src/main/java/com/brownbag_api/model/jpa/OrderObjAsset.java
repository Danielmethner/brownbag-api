package com.brownbag_api.model.jpa;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.brownbag_api.model.enums.EOrderStatus;
import com.brownbag_api.model.enums.EOrderType;

@Entity
@Table(name = "order_obj_asset")
public class OrderObjAsset extends Order implements Serializable {

	private static final long serialVersionUID = 4643589803146964779L;

	public OrderObjAsset() {
	}

	public OrderObjAsset(double qty, ObjAsset asset, @NotNull EOrderType orderType, EOrderStatus orderStatus,
			@NotNull ObjUser user, String advText) {
		super(0, null, orderType, orderStatus, user, advText);

	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
