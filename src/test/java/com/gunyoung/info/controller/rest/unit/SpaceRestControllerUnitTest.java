package com.gunyoung.info.controller.rest.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gunyoung.info.controller.rest.SpaceRestController;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.dto.ProfileDTO;
import com.gunyoung.info.error.exceptions.nonexist.PersonNotFoundedException;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.util.PersonTest;

/**
 * {@link SpaceRestController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) SpaceRestController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class SpaceRestControllerUnitTest {
	
	@Mock
	PersonService personService;
	
	@InjectMocks
	SpaceRestController spaceRestController;
	
	/*
	 * public void updateProfilePost(@ModelAttribute("formModel") @Valid ProfileDTO profileDTO)
	 */
	
	@Test
	@DisplayName("Person 프로필(Person + Space) 수정 -> 전달된 ProfileDTO에 있는 이메일이 DB에 존재하지 않을때")
	public void updateProfilePostTestPersonNonExist() {
		//Given
		String nonExistEmail = "nonexist@test.com";
		ProfileDTO profileDTO = PersonTest.getProfileDTOProfileDTOInstance(nonExistEmail);
		given(personService.findByEmailWithSpace(nonExistEmail)).willReturn(null);
		
		//When, Then
		assertThrows(PersonNotFoundedException.class, () -> {
			spaceRestController.updateProfilePost(profileDTO);
		});
	}
	
	@Test
	@DisplayName("Person 프로필(Person + Space) 수정 -> 정상, PersonService Test")
	public void updateProfilePostTestCheckPersonService() {
		//Given
		String personEmail = "test@test.com";
		ProfileDTO profileDTO = PersonTest.getProfileDTOProfileDTOInstance(personEmail);
		
		Person person = PersonTest.getPersonInstance();
		given(personService.findByEmailWithSpace(personEmail)).willReturn(person);
		
		//When
		spaceRestController.updateProfilePost(profileDTO);
		
		//Then
		then(personService.save(person));
	}
}
