package com.brownbag_api.model.demo;

import com.brownbag_api.database.DB;
import com.brownbag_api.model.User;

public class Demo {

	public static void createDemoDataUser(String userName) {
		User user = new User(userName);
		DB.beginTrx();
		DB.saveEntity(user);
		DB.commitTrx();
	}
	
	public static void createDemoData() {
		createDemoDataUser("First User");
		createDemoDataUser("Second User");
		createDemoDataUser("Third User");
	}
}
