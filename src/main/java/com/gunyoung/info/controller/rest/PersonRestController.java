package com.gunyoung.info.controller.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gunyoung.info.error.code.PersonErrorCode;
import com.gunyoung.info.error.exceptions.access.NotMyResourceException;
import com.gunyoung.info.error.exceptions.nonexist.PersonNotFoundedException;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.util.AuthorityUtil;

import lombok.RequiredArgsConstructor;

/**
 * Person 관련 처리를 하는 RestController
 * @author kimgun-yeong
 *
 */
@RestController
@RequiredArgsConstructor
public class PersonRestController {
	
	private final PersonService personService;
	
	/**
	 * <pre>
	 *  - 기능: 회원가입할때 Email 중복 여부 반환하는 컨트롤러
	 *  - 반환: 
	 *  	True or False
	 *  </pre>
	 *  @param email 중복여부를 확인하려는 email 값
	 *  @author kimgun-yeong
	 */
	@RequestMapping(value="/join/emailverification", method=RequestMethod.GET)
	public String emailVerification(@RequestParam("email") String email) {
		return String.valueOf(personService.existsByEmail(email));
	}
	
	
	/**
	 * <pre>
	 *  - 기능: 회원 탈퇴를 처리하는 컨트롤
	 *  	   DB: 해당 person 삭제
	 *  </pre>
	 *  @param email 회원 탈퇴하려는 주체의 email값
	 *  @throws PersonNotFoundedException 해당 계정이 DB에 존재하지 않을 때
	 *  @throws NotMyResourceException 로그인 계정이 탈퇴 계정과 일치하지 않을 때
	 *  @author kimgun-yeong
	 */
	@RequestMapping(value="/withdraw", method=RequestMethod.DELETE)
	public void personWithdraw(@RequestParam("email") String email) {
		
		if(!personService.existsByEmail(email)) {
			throw new PersonNotFoundedException(PersonErrorCode.PERSON_NOT_FOUNDED_ERROR.getDescription());
		}
		
		String userEmail = AuthorityUtil.getSessionUserEmail();
		
		if(!userEmail.equals(email)) {
			throw new NotMyResourceException(PersonErrorCode.RESOURCE_IS_NOT_MINE_ERROR.getDescription());
		}
		
		personService.delete(personService.findByEmail(email));
	}
}
