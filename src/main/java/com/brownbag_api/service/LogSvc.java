package com.brownbag_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brownbag_api.model.jpa.Log;
import com.brownbag_api.repo.LogRepo;

@Service
public class LogSvc extends OrderSvc {

	@Autowired
	private LogRepo logRepo;

	public void write(String msg) {

		Log log = new Log(msg);
		logRepo.save(log);
	}

	public List<Log> getAll() {
		return logRepo.findAll();
	}

	public List<Log> getRecentEntries() {
		return logRepo.findFirst20ByOrderByTimestampCreateDesc();

	}

}
