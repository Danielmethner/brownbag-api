package com.brownbag_api.model.enums;

import java.util.ArrayList;
import java.util.List;

import com.brownbag_api.model.json.JsonEOrderAction;

public enum EFormMenuCmd {
	// MASTER DATA
	CREATE_ORDER("Create Order", "newOrder");

	public final String name;
	public final String functionName;

	private EFormMenuCmd(String name, String functionName) {
		this.name = name;
		this.functionName = functionName;
	}

	public String getName() {
		return name;
	}

	public String getFunctionName() {
		return functionName;
	}
	
	
}
