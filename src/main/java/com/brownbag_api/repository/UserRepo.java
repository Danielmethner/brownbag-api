package com.brownbag_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brownbag_api.model.User;

public interface UserRepo extends JpaRepository<User, Long> {

	List<User> findByName(String name);

	List<User> findByNameContaining(String name);
}
