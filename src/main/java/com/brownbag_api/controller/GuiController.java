package com.brownbag_api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brownbag_api.model.enums.EEntityType;
import com.brownbag_api.model.json.JsonFormMenuItem;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/gui")
public class GuiController {

	@GetMapping("/menu-items/all")
	public ResponseEntity<?> allMenuItems() {

		List<JsonFormMenuItem> menuItemList = new ArrayList<JsonFormMenuItem>();
		EEntityType[] entitiyTypeArray = EEntityType.values();

		for (EEntityType entityType : entitiyTypeArray) {
			JsonFormMenuItem entityTypeJson = new JsonFormMenuItem(entityType);
			menuItemList.add(entityTypeJson);
		}
		return ResponseEntity.ok(menuItemList);
	}
}