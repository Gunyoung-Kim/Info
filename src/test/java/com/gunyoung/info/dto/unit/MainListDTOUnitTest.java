package com.gunyoung.info.dto.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.dto.MainListDTO;
import com.gunyoung.info.util.PersonTest;

/**
 * {@link MainListDTO} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) MainListDTO only
 * @author kimgun-yeong
 *
 */
public class MainListDTOUnitTest {
	
	/*
	 * public static List<MainListDTO> of(Iterable<Person> people)
	 */
	
	@Test
	@DisplayName("Person 컬렉션을 통해 MainListDTO 리스트 반환 -> 정상")
	public void ofListTest() {
		//Given
		List<Person> givenPeople = new ArrayList<>();
		Long givenPersonNum = Long.valueOf(24);
		
		for(int i=0; i < givenPersonNum;i++) {
			Person person = PersonTest.getPersonInstance("test"+ i + "@test.com");
			person.setId(Long.valueOf(i));
			person.setFirstName("f" + i);
			person.setLastName("l" + i);
			givenPeople.add(person);
		}
		
		//When
		List<MainListDTO> result = MainListDTO.of(givenPeople);
		
		//Then
		verifyMainListDTOListWithPeopleList(givenPeople, givenPersonNum, result);
	}
	
	private void verifyMainListDTOListWithPeopleList(List<Person> people, Long givenPersonNum, List<MainListDTO> result) {
		for(int i=0;i<givenPersonNum;i++) {
			Person person = people.get(i);
			MainListDTO dto = result.get(i);
			assertEquals(person.getId(), dto.getPersonId());
			assertEquals(person.getFullName(), dto.getPersonName());
			assertEquals(person.getEmail(), dto.getPersonEmail());
		}
	}
}
