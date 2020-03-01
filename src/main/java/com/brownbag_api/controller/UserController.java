package com.brownbag_api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.User;
import com.brownbag_api.repository.UserRepo;

// Origin: https://bezkoder.com/spring-boot-jpa-crud-rest-api/

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserRepo userRepo;

	@GetMapping("/all")
	public ResponseEntity<List<User>> getAll() {
		try {
			List<User> users = new ArrayList<User>();

			userRepo.findAll().forEach(users::add);

			if (users.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(users, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
		Optional<User> userData = userRepo.findById(id);

		if (userData.isPresent()) {
			return new ResponseEntity<>(userData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<List<User>> getUserByName(@PathVariable("name") String name) {
		System.out.println("Trigger Function");
		try {
			List<User> users = userRepo.findByName(name);
			if (users.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(users, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/name/containing/{name}")
	public ResponseEntity<List<User>> getUserByNameContaining(@PathVariable("name") String name) {

		try {

			List<User> users = userRepo.findByNameContaining(name);
			if (users.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(users, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PostMapping("/")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		try {
			User _user = userRepo.save(user);
			return new ResponseEntity<>(_user, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {
		Optional<User> userData = userRepo.findById(id);

		if (userData.isPresent()) {
			User _user = userData.get();
			_user.setName(user.getName());
			_user.setPassword(user.getPassword());
			return new ResponseEntity<>(userRepo.save(_user), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
		try {
			userRepo.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

//	@DeleteMapping("/users")
//	public ResponseEntity<HttpStatus> deleteAllUsers() {
//		try {
//			userRepo.deleteAll();
//			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//		} catch (Exception e) {
//			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
//		}
//
//	}

}
