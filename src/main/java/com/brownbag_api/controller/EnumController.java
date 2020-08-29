package com.brownbag_api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.enums.EEntityType;
import com.brownbag_api.model.enums.ELegalForm;
import com.brownbag_api.model.json.JsonEEntityType;
import com.brownbag_api.model.json.JsonELegalForm;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/enum")
public class EnumController {

	@GetMapping("/legalform/all")
	public ResponseEntity<?> allLegalForms() {

		List<JsonELegalForm> legalFormJsonList = new ArrayList<JsonELegalForm>();
		ELegalForm[] legalFormArray = ELegalForm.values();

		for (ELegalForm legalForm : legalFormArray) {
			JsonELegalForm legalFormJson = new JsonELegalForm(legalForm);
			legalFormJsonList.add(legalFormJson);
		}
		return ResponseEntity.ok(legalFormJsonList);
	}

	@GetMapping("/enitityType/all")
	public ResponseEntity<?> allMenuItems() {

		List<JsonEEntityType> legalFormJsonList = new ArrayList<JsonEEntityType>();
		EEntityType[] entitiyTypeArray = EEntityType.values();

		for (EEntityType entityType : entitiyTypeArray) {
			JsonEEntityType entityTypeJson = new JsonEEntityType(entityType);
			legalFormJsonList.add(entityTypeJson);
		}
		return ResponseEntity.ok(legalFormJsonList);
	}
}