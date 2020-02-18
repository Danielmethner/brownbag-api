package com.brownbag_api.model.demo;

import com.brownbag_api.database.DB;
import com.brownbag_api.model.User;

public class Demo {

	public static void createDemoData() {
		User user = new User("DemoUser");
		DB.beginTrx();
		DB.saveEntity(user);
		DB.commitTrx();
	}
}
