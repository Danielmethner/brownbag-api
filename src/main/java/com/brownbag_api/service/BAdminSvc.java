package com.brownbag_api.service;

import org.springframework.stereotype.Service;

import com.brownbag_api.util.UtilBA;

@Service
public class BAdminSvc {

	public double getIntrRate() {
		return UtilBA.getIntrRate();
	}
}
