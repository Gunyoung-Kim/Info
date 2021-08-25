package com.gunyoung.info.controller.rest;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.dto.MainListDTO;
import com.gunyoung.info.services.domain.PersonService;

import lombok.RequiredArgsConstructor;

/**
 * 브라우저와 Ajax 통신을 위한 컨트롤러
 * @author kimgun-yeong
 *
 */
@RestController
@RequiredArgsConstructor
public class RestfulController {
	
	private final PersonService personService;
	
	/**
	 * <pre>
	 *  - 기능: main 화면에서 노출할 리스트를 반환
	 *  - 반환:
	 *  	List<MainListDTO>, MainListDTO(DTO 객체) -> Person.fullname + Person.email
	 *  </pre>
	 *  @author kimgun-yeong
	 */
	@RequestMapping(value="/main/list",method=RequestMethod.GET)
	public List<MainListDTO> index() {
		List<Person> personList = personService.findAll();
		
		List<MainListDTO> result = MainListDTO.of(personList);
		
		return result;
	}
}
