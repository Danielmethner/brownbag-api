package com.brownbag_api.model.json;

import java.util.Date;

import com.brownbag_api.model.jpa.Booking;

public class JsonBooking {

	private Long id;
	private Date timestamp;
	private Long orderId;
	private Long posId;
	private String bookText;
	private double posBalOld;
	private double posBalNew;
	private double qty;

	public JsonBooking() {
	}

	public JsonBooking(Booking booking) {
		this.id = booking.getId();
		this.timestamp = booking.getTimestampCreate();
		this.orderId = booking.getOrder().getId();
		this.posId = booking.getPos().getId();
		this.bookText = booking.getBookText();
		this.posBalOld = booking.getPosBalOld();
		this.posBalNew = booking.getPosBalNew();
		this.qty = booking.getQty();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getPosId() {
		return posId;
	}

	public void setPosId(Long posId) {
		this.posId = posId;
	}

	public String getBookText() {
		return bookText;
	}

	public void setBookText(String bookText) {
		this.bookText = bookText;
	}

	public double getPosBalOld() {
		return posBalOld;
	}

	public void setPosBalOld(double posBalOld) {
		this.posBalOld = posBalOld;
	}

	public double getPosBalNew() {
		return posBalNew;
	}

	public void setPosBalNew(double posBalNew) {
		this.posBalNew = posBalNew;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

}
