package com.brownbag_api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.enums.ECtrlVar;
import com.brownbag_api.model.jpa.CtrlVar;
import com.brownbag_api.model.jpa.Log;
import com.brownbag_api.model.json.JsonCtrlVar;
import com.brownbag_api.service.ControlSvc;
import com.brownbag_api.service.LogSvc;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/settings/")
public class SettingsController {

	@Autowired
	private LogSvc logSvc;
	
	@Autowired
	private ControlSvc ctrlVarSvc;

	@GetMapping("/log")
	public List<Log> getLogEntries() {
		return logSvc.getAll();
	}

	@GetMapping("/log/recent")
	public ResponseEntity<?> getRecentEntries() {
		return ResponseEntity.ok(logSvc.getRecentEntries());
	}
	
	private List<JsonCtrlVar> jpaToJson(List<CtrlVar> jpaObjList) {
		List<JsonCtrlVar> jsonObjList = new ArrayList<JsonCtrlVar>();
		for (CtrlVar jpaObj : jpaObjList) {
			JsonCtrlVar jsonObj = new JsonCtrlVar(jpaObj);
			jsonObjList.add(jsonObj);
		}
		return jsonObjList;
	}
	
	@GetMapping("/ctrlvar/finyear")
	public ResponseEntity<?> setFinYear() {
		return ResponseEntity.ok(ctrlVarSvc.getFinYear());
	}
		
	@GetMapping("/ctrlvar/finyear/switch")
	public ResponseEntity<?> incrFinYear() {
		Integer finYearRtn = ctrlVarSvc.switchFinYear();
		return ResponseEntity.ok(finYearRtn);
	}
	
	@GetMapping("/ctrlvar")
	public ResponseEntity<?> getCtrlVars() {
		
		return ResponseEntity.ok(jpaToJson(ctrlVarSvc.getAll()));
	}
	
	@GetMapping("/ctrlvar/{eCtrlVar}")
	public ResponseEntity<?> getCtrlVars(@PathVariable ECtrlVar eCtrlVar) {
		CtrlVar controlVar = ctrlVarSvc.getByEnum(eCtrlVar);
		JsonCtrlVar jsonCtrlVar = new JsonCtrlVar(controlVar);
		return ResponseEntity.ok(jsonCtrlVar);
	}
}