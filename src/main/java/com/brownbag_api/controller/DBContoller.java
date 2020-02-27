package com.brownbag_api.controller;

import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.database.DB;
import com.brownbag_api.model.User;
import com.brownbag_api.model.demo.Demo;

@RestController
public class DBContoller {

	@CrossOrigin
	@RequestMapping(value = "/db/connect", method = RequestMethod.GET)
	public String connectDB() throws Exception {
		try {
			DB.connect();
			DB.openSession();
			Demo.createDemoData();
			return "DB Connected";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getStackTrace().toString();
		}
		
	}
}
