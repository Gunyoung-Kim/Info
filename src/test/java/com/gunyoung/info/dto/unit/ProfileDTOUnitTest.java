package com.gunyoung.info.dto.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.dto.ProfileDTO;
import com.gunyoung.info.util.PersonTest;
import com.gunyoung.info.util.SpaceTest;

/**
 * {@link ProfileDTO} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) ProfileDTO only
 * @author kimgun-yeong
 *
 */
public class ProfileDTOUnitTest {
	
	/*
	 * public void updatePersonAndSpace(Person person, Space space)
	 */
	
	@Test
	@DisplayName("ProfileDTO에 담긴 정보로 Person, Space 정보 업데이트-> 정상, 필드 확인")
	public void updatePersonAndSpaceTestCheckField() {
		//Given
		Person person = Person.builder().build();
		Space space = Space.builder().build();
		
		ProfileDTO profileDTO = PersonTest.getProfileDTOInstance("test@test.com");
		
		//When
		profileDTO.updatePersonAndSpace(person, space);
		
		//Then
		verifyPersonWithProfileDTO(profileDTO, person);
		verifySpaceWithProfileDTO(profileDTO, space);
	}
	
	private void verifyPersonWithProfileDTO(ProfileDTO profileDTO, Person person) {
		assertEquals(profileDTO.getFirstName(), person.getFirstName());
		assertEquals(profileDTO.getLastName(), person.getLastName());
	}
	
	private void verifySpaceWithProfileDTO(ProfileDTO profileDTO, Space space) {
		assertEquals(profileDTO.getDescription(), space.getDescription());
		assertEquals(profileDTO.getGithub(), space.getGithub());
		assertEquals(profileDTO.getFacebook(), space.getFacebook());
		assertEquals(profileDTO.getInstagram(), space.getInstagram());
		assertEquals(profileDTO.getTweeter(), space.getTweeter());
	}
	
	/*
	 * public static ProfileDTO createFromPersonAndSpace(Person person, Space space)
	 */
	
	@Test
	@DisplayName("Person, Space 를 통해 ProfileDTO 생성 후 반환 -> 정상, Field Check")
	public void createFromPersonAndSpaceTestCheckField() {
		//Given
		Person person = PersonTest.getPersonInstance();
		Space space = SpaceTest.getSpaceInstance();
		
		//When
		ProfileDTO result = ProfileDTO.createFromPersonAndSpace(person, space);
		
		//Then
		verifyProfileDTOWithPerson(person, result);
		verifyProfileDTOWithSpace(space, result);
	}
	
	private void verifyProfileDTOWithPerson(Person person, ProfileDTO result) {
		assertEquals(person.getEmail(), result.getEmail());
		assertEquals(person.getFirstName(), result.getFirstName());
		assertEquals(person.getLastName(), result.getLastName());
	}
	
	private void verifyProfileDTOWithSpace(Space space, ProfileDTO result) {
		assertEquals(space.getDescription(), result.getDescription());
		assertEquals(space.getGithub(), result.getGithub());
		assertEquals(space.getInstagram(), result.getInstagram());
		assertEquals(space.getTweeter(), result.getTweeter());
		assertEquals(space.getFacebook(), result.getFacebook());
	}
}
