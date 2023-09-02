package com.scm.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.scm.dao.ContactsRepository;
import com.scm.models.Contacts;

@RestController
public class SearchController {

	@Autowired
	private ContactsRepository contactRepository;

	@GetMapping(path = "/search/{query}")
	public ResponseEntity<?> getQueryContact(@PathVariable String query){
		List<Contacts> searchResult = this.contactRepository.searchByKeywordInContact(query);
		return ResponseEntity.ok(searchResult);
	}

}
