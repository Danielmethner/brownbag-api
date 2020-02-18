package com.brownbag_api.controller;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;

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
		User user = (User) DB.getById(User.class, id);
		return user;
	}

}
