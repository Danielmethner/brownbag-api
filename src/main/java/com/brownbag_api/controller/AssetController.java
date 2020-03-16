package com.brownbag_api.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.Asset;
import com.brownbag_api.repo.AssetRepo;
import com.brownbag_api.security.jwt.JwtUtils;
import com.brownbag_api.security.model.ERole;
import com.brownbag_api.security.model.Role;
import com.brownbag_api.security.model.User;
import com.brownbag_api.security.payload.request.LoginRequest;
import com.brownbag_api.security.payload.request.SignupRequest;
import com.brownbag_api.security.payload.response.JwtResponse;
import com.brownbag_api.security.payload.response.MsgResponse;
import com.brownbag_api.security.repo.RoleRepo;
import com.brownbag_api.security.repo.UserRepo;
import com.brownbag_api.security.svc.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/asset")
public class AssetController {

	@Autowired
	private AssetRepo assetRepo;

	@GetMapping("/sec/all")
	public List<Asset> allSecurities() {
		return assetRepo.findAllByIsShare(true);
	}

}