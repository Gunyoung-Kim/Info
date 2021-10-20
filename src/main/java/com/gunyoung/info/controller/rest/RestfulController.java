package com.gunyoung.info.controller.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.dto.MainListDTO;
import com.gunyoung.info.services.domain.PersonService;

import lombok.RequiredArgsConstructor;

/**
 * 메인 화면에서 필요한 리소스 반환하는 컨트롤러
 * @author kimgun-yeong
 *
 */
@RestController
@RequiredArgsConstructor
public class RestfulController {
	
	private final PersonService personService;
	
	/**
	 *  main 화면에서 노출할 리스트를 반환
	 *  @return {@code List<MainListDTO>}, MainListDTO(DTO 객체) -> Person.fullname + Person.email
	 *  @author kimgun-yeong
	 */
	@GetMapping(value="/main/list")
	public List<MainListDTO> index() {
		List<Person> personList = personService.findAll();
		return MainListDTO.of(personList);
	}
}
