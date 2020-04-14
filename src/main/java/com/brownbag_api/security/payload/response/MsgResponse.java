package com.brownbag_api.security.payload.response;

public class MsgResponse {
	private String message;

	public MsgResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
