package com.gunyoung.info.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.dto.MainListObject;
import com.gunyoung.info.services.PersonService;
import com.gunyoung.info.services.SpaceService;

@RestController
public class RestfulController {
	@Autowired
	PersonService personService;
	
	@Autowired
	SpaceService spaceService;
	
	
	@GetMapping("/main/list")
	public List<MainListObject> index() {
		List<Person> personList = personService.getAll();
		
		List<MainListObject> result = new LinkedList<>();
		
		for(Person p : personList) {
			result.add(new MainListObject(p.getFullName(),p.getEmail()));
		}
		
		return result;
	}
	
	@GetMapping("/join/idverification/{email}")
	public boolean idVerification(@PathVariable String email) {
		return personService.existsByEmail(email);
	}
}
