package com.gunyoung.info.controller.rest;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.dto.ProfileObject;
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
	 * <pre>
	 *  - 기능: updateProfile이 반환한 뷰에서 작성한 프로필 변경 사항들을 유저가 POST Request로 보내면 이를 처리하기 위한 컨트롤러
	 * 	- 성공
	 * 		DB: ProfileObject에서 Person 및 Space의 변경 사항 추출 후 save
	 * </pre>
	 * @param profileObject Person의 필드 및 Space의 필드 값 수정을 위한 ProfileObject DTO 객체 
	 * @throws PersonNotFoundedException 전달된 ProfileObject에 있는 이메일이 DB에 존재하지 않을때 
	 * @author kimgun-yeong
	 */
	@RequestMapping(value="/space/updateprofile", method = RequestMethod.PUT)
	public void updateProfilePost(@ModelAttribute("formModel") @Valid ProfileObject profileObject) {
		Person person = personService.findByEmailWithSpace(profileObject.getEmail());
		if(person == null) {
			throw new PersonNotFoundedException(PersonErrorCode.PERSON_NOT_FOUNDED_ERROR.getDescription());
		}
		
		Space space = person.getSpace();
		profileObject.updatePersonAndSpace(person, space);
		
		personService.save(person);
	}
}
