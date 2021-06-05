package com.gunyoung.info.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	/*
	 *  - 기능: main 화면에서 노출할 리스트를 반환하는 컨트롤러
	 *  - 반환:
	 *  	List<MainObject>, MainObject(DTO 객체) -> Person.fullname + Person.email
	 */
	
	@GetMapping("/main/list")
	public List<MainListObject> index() {
		List<Person> personList = personService.getAll();
		
		List<MainListObject> result = new LinkedList<>();
		
		for(Person p : personList) {
			result.add(new MainListObject(p.getFullName(),p.getEmail()));
		}
		
		return result;
	}
	
	/*
	 *  - 기능: 회원가입할때 Email 중복 여부 반환하는 컨트롤러
	 *  - 반환: 
	 *  	True or False
	 */
	@GetMapping("/join/emailverification")
	public String emailVerification(@RequestParam("email") String email) {
		return String.valueOf(personService.existsByEmail(email));
	}
}
