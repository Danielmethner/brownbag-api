package com.brownbag_api.model.demo;

import com.brownbag_api.model.User;

public class Demo {

	public static void createDemoDataUser(String userName) {
		User user = new User(userName);

	}
	
	public static void createDemoData() {
		createDemoDataUser("First User");
		createDemoDataUser("Second User");
		createDemoDataUser("Third User");
	}
}
