package com.gunyoung.info.controller.rest;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.dto.ProfileDTO;
import com.gunyoung.info.error.code.PersonErrorCode;
import com.gunyoung.info.error.exceptions.nonexist.PersonNotFoundedException;
import com.gunyoung.info.services.domain.PersonService;

import lombok.RequiredArgsConstructor;

/**
 * Space 관련 처리를 담당하는 RestController
 * @author kimgun-yeong
 *
 */
@RestController
@RequiredArgsConstructor
public class SpaceRestController {
	
	private final PersonService personService;
	
	/**
	 * Person 프로필(Person + Space) 수정, DB: ProfileDTO에서 Person 및 Space의 변경 사항 추출 후 save
	 * @param profileDTO Person의 필드 및 Space의 필드 값 수정을 위한 ProfileDTO
	 * @throws PersonNotFoundedException 전달된 ProfileDTO에 있는 이메일이 DB에 존재하지 않을때 
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/space/updateprofile", method = RequestMethod.PUT)
	public void updateProfilePost(@ModelAttribute("formModel") @Valid ProfileDTO profileDTO) {
		Person person = personService.findByEmailWithSpace(profileDTO.getEmail());
		if(person == null) {
			throw new PersonNotFoundedException(PersonErrorCode.PERSON_NOT_FOUNDED_ERROR.getDescription());
		}
		Space space = person.getSpace();
		profileDTO.updatePersonAndSpace(person, space);
		
		personService.save(person);
	}
}
