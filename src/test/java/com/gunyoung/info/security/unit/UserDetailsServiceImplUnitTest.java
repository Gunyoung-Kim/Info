package com.gunyoung.info.security.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.enums.RoleType;
import com.gunyoung.info.security.UserDetailsServiceImpl;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.util.PersonTest;

/**
 * {@link UserDetailsServiceImpl} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) UserDetailsServiceImpl only
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplUnitTest {
	
	@InjectMocks
	UserDetailsServiceImpl userDetailsService;
	
	@Mock
	PersonService personService;
	
	/**
	 *   UserDetails loadPersonByPersonname(String email) throws PersonnameNotFoundException
	 */
	
	@Test
	@DisplayName("DB에서 가져온 Person 정보로 UserDetails 반환 -> Personservice에서 가져온 Person가 없을 때")
	void loadPersonByPersonnameNonExist() {
		//Given
		String nonExistEmail = "nonExist@test.com";
		given(personService.findByEmail(nonExistEmail)).willReturn(null);
		
		//When, Then
		assertThrows(UsernameNotFoundException.class, () -> {
			userDetailsService.loadUserByUsername(nonExistEmail);
		});
	}
	
	@Test
	@DisplayName("관리자 권한의 UserDetails 반환 -> 정상")
	void loadPersonByPersonnameForAdminTest() {
		//Given
		String adminEmail = "admin@test.com";
		Person person = PersonTest.getPersonInstance(adminEmail,RoleType.ADMIN);

		given(personService.findByEmail(adminEmail)).willReturn(person);
		
		//When
		UserDetails personDetails = userDetailsService.loadUserByUsername(person.getEmail());
		
		//Then
		assertTrue(personDetails.getAuthorities().stream()
				.anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN")));
	}

	
	@Test
	@DisplayName("일반 유저 권한의 PersonDetials 반환 -> 정상")
	void loadPersonByPersonnameForPersonTest() {
		//Given
		String personEmail ="person@test.com";
		Person person = PersonTest.getPersonInstance(personEmail,RoleType.USER);
		
		given(personService.findByEmail(personEmail)).willReturn(person);
		
		//When
		UserDetails personDetails = userDetailsService.loadUserByUsername(person.getEmail());
		
		//Then
		assertTrue(personDetails.getAuthorities().stream()
				.anyMatch(ga -> ga.getAuthority().equals("ROLE_USER")));
	}
}
