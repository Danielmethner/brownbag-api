package com.brownbag_api.controller;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;

import org.hibernate.query.Query;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.database.DB;
import com.brownbag_api.model.User;

@RestController
public class UserController {
	/**
	 * GET OBJ BY ID
	 * 
	 * SAMPLE: "localhost:8080/user/get/1"
	 * 
	 * @param id
	 * @return User
	 * @throws Exception
	 */
	@CrossOrigin
	@RequestMapping(value = "/user/get/{id}", method = RequestMethod.GET)
	public User getObjById(@PathVariable @NotNull Long id) throws Exception {
		DB.openSession();
		User user = (User) DB.getById(User.class, id);
		DB.closeSession();
		return user;
	}
	
	@CrossOrigin
	@RequestMapping(value = "/user/get", method = RequestMethod.GET)
	public ArrayList<User> getAll() throws Exception {
		DB.openSession();
		ArrayList<User> userList = new ArrayList<User>();
		
		@SuppressWarnings("unchecked")
		Query<User> query = DB.s
				.createQuery("from " + User.class.getSimpleName());		

		if (query.list().isEmpty()) {
			User user = new User();
			user.setName("No uesrs found!");
			userList.add(user);
			DB.closeSession();
			return userList;
		} else {
			userList = (ArrayList<User>) query.list();
			DB.closeSession();
			return userList;
		}
		
	}

}
