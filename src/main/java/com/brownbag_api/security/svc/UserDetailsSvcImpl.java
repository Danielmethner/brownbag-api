package com.brownbag_api.security.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brownbag_api.model.User;
import com.brownbag_api.repo.UserRepo;

@Service
public class UserDetailsSvcImpl implements UserDetailsService {
	@Autowired
	UserRepo userRepo;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);

		return UserDetailsImpl.build(user);
	}

}