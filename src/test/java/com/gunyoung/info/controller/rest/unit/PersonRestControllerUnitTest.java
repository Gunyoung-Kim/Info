package com.gunyoung.info.controller.rest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.gunyoung.info.controller.rest.PersonRestController;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.error.exceptions.access.NotMyResourceException;
import com.gunyoung.info.error.exceptions.nonexist.PersonNotFoundedException;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.util.PersonTest;

/**
 * {@link PersonRestController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) PersonRestController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class PersonRestControllerUnitTest {
	
	@Mock
	PersonService personService;
	
	@InjectMocks
	PersonRestController personRestController;
	
	private SecurityContext securityContext;
	
	private Authentication auth;
	
	@BeforeEach
	void setup() {
		securityContext = mock(SecurityContext.class);
		SecurityContextHolder.setContext(securityContext);
		
		auth = mock(Authentication.class);
	}
	
	/*
	 * String emailVerification(@RequestParam("email") String email)
	 */
	
	@Test
	@DisplayName("Email 중복 여부 반환 -> 정상, TRUE")
	void emailVerificationTestTrue() {
		//Given
		String existEmail = "test@test.com";
		given(personService.existsByEmail(existEmail)).willReturn(true);
		
		//When
		String result = personRestController.emailVerification(existEmail);
		
		//Then
		assertEquals("true", result);
	}
	
	@Test
	@DisplayName("Email 중복 여부 반환 -> 정상, false")
	void emailVerificationTestFalse() {
		//Given
		String nonExistEmail = "nonexist@test.com";
		given(personService.existsByEmail(nonExistEmail)).willReturn(false);
		
		//When
		String result = personRestController.emailVerification(nonExistEmail);
		
		//Then
		assertEquals("false", result);
	}
	
	/*
	 * void personWithdraw(@RequestParam("email") String targetPersonEmail)
	 */
	
	@Test
	@DisplayName("회원 탈퇴를 처리 -> 해당 계정이 DB에 존재하지 않을 때")
	void personWithdrawTestPersonNonExist() {
		//Given
		String nonExistEmail = "nonexist@test.com";
		given(personService.findByEmail(nonExistEmail)).willReturn(null);
		
		//When, Then
		assertThrows(PersonNotFoundedException.class, () -> {
			personRestController.personWithdraw(nonExistEmail);
		});
	}
	
	@Test
	@DisplayName("회원 탈퇴를 처리 -> 로그인 계정이 탈퇴 계정과 일치하지 않을 때")
	void personWithdrawTestNotMyResource() {
		//Given
		String targetEmail = "test@test.com";
		mockingPersonServiceFindByEmail(targetEmail);
		
		String loginPersonEmail = "notmine@test.com";
		mockingAuthorityutilGetSessionUserEmail(loginPersonEmail);
		
		//When, Then
		assertThrows(NotMyResourceException.class, () -> {
			personRestController.personWithdraw(targetEmail);
		});
	}
	
	@Test
	@DisplayName("회원 탈퇴를 처리 -> 정상, personService delete check")
	void personWithdrawTestCheckPersonServiceDelete() {
		//Given
		String targetEmail = "test@test.com";
		Person targetPerson = mockingPersonServiceFindByEmail(targetEmail);
		mockingAuthorityutilGetSessionUserEmail(targetEmail);
		
		//When
		personRestController.personWithdraw(targetEmail);
		
		//Then
		then(personService).should(times(1)).delete(targetPerson);
	}
	
	private Person mockingPersonServiceFindByEmail(String targetEmail) {
		Person person = PersonTest.getPersonInstance(targetEmail);
		given(personService.findByEmail(targetEmail)).willReturn(person);
		return person;
	}
	
	private void mockingAuthorityutilGetSessionUserEmail(String email) {
		given(securityContext.getAuthentication()).willReturn(auth);
		given(auth.getName()).willReturn(email);
	}
}
