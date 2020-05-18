package com.brownbag_api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.jpa.Log;
import com.brownbag_api.service.LogSvc;
import com.brownbag_api.util.UtilDate;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/settings/")
public class SettingsController {

	@Autowired
	private LogSvc logSvc;

	@GetMapping("/finyear")
	public int getFinYear() {
		return UtilDate.getFinYear();
	}

	@GetMapping("/finyear/increment")
	public LocalDate incrementFinYear() {
		return UtilDate.incrFinYear();
	}

	@GetMapping("/log")
	public List<Log> getLogEntries() {
		return logSvc.getAll();
	}

	@GetMapping("/log/recent")
	public ResponseEntity<?> getRecentEntries() {
		return ResponseEntity.ok(logSvc.getRecentEntries());
	}
}