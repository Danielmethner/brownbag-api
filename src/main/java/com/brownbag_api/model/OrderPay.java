package com.brownbag_api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.brownbag_api.model.data.EOrderStatus;
import com.brownbag_api.model.data.EOrderType;

@Entity
@Table(name="order_pay")
public class OrderPay extends Order implements Serializable {

	private static final long serialVersionUID = 4643589803146964779L;

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
			@NotNull User user, @NotNull String bookText) {
		super(qty, asset, orderType, orderStatus, user);
		this.bookText = bookText;
	}

	public String getBookText() {
		return bookText;
	}

	public void setBookText(String bookText) {
		this.bookText = bookText;
	}
}
