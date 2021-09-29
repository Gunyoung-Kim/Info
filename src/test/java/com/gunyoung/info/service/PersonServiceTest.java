package com.gunyoung.info.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.repos.PersonRepository;
import com.gunyoung.info.repos.SpaceRepository;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.testutil.Integration;
import com.gunyoung.info.util.PersonTest;

/**
 * {@link PersonService} 에 대한 테스트 클래스 <br>
 * 테스트 범위:(통합 테스트) 서비스 계층 - 영속성 계층 
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
public class PersonServiceTest {
	
	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	SpaceRepository spaceRepository;
	
	@Autowired
	PersonService personService;
	
	private static final String MAIN_PERSON_EMAIL = "test@test.com";
	
	private Person person;
	
	@BeforeEach
	void setup() {
		person = PersonTest.getPersonInstance(MAIN_PERSON_EMAIL);
		personRepository.save(person);
	}
	
	@AfterEach
	void tearDown() {
		personRepository.deleteAll();
	}
	
	/*
	 *  - 대상 메소드: 
	 *  	public Person save(Person person);
	 */
	
	@Test
	@DisplayName("Person save (성공, 수정, 버전 변경 확인)")
	public void modifyPersonTestCheckVersion() {
		//Given
		String changedFirstName = "변경";
		person.setFirstName(changedFirstName);
		
		int givenPersonVersion = person.getVersion();
		
		Long personId = person.getId();
		
		//When
		personService.save(person);
		
		assertEquals(givenPersonVersion + 1, personRepository.findById(personId).get().getVersion());
	}
	
	@Test
	@DisplayName("Person save (성공, 수정, 개수 동일 확인)")
	public void modifyPersonTestCheckCount() {
		//Given
		long givenPersonNum = personRepository.count();
		
		String changedFirstName = "변경";
		person.setFirstName(changedFirstName);
		
		//When
		personService.save(person);
		
		//Then
		assertEquals(givenPersonNum, personRepository.count());
	}
	
	@Test
	@DisplayName("Person save (성공, 수정, 변경 사항 적용 확인)")
	public void modifyPersonTestCheckChanged() {
		//Given
		String changedFirstName = "변경";
		person.setFirstName(changedFirstName);
		
		Long personId = person.getId();
		
		//When
		personService.save(person);
		
		//Then
		assertEquals(changedFirstName, personRepository.findById(personId).get().getFirstName());
	}
	
	@Test
	@DisplayName("Person save (성공, 추가)")
	public void addPersonTestCheckNum() {
		//Given
		long givenPersonNum = personRepository.count();
		
		Person newPerson = PersonTest.getPersonInstance("new@test.com");
		
		//When
		personService.save(newPerson);
		
		//Then
		assertEquals(givenPersonNum + 1, personRepository.count());
	}
	
	/*
	 *  - 대상 메소드: 
	 *  	public void deletePerson(Person person);
	 */
	
	@Test
	@DisplayName("Person Delete (실패- 해당 Person 없음)")
	public void deletePersonNonExist() {
		//Given
		long givenPersonNum = personRepository.count();
		Person nonExistInDBPerson = PersonTest.getPersonInstance("newperson@test.com");
		
		//When
		personService.delete(nonExistInDBPerson);
		
		//Then
		assertEquals(givenPersonNum,personRepository.count());
	}
	
	@Test
	@DisplayName("Person Delete (성공), Person 삭제 확인")
	public void deletePersonTest() {
		//Given
		long givenPersonNum = personRepository.count();
	
		//When
		personService.delete(person);
		
		//Then
		assertEquals(givenPersonNum-1, personRepository.count());
	}
	
	@Test
	@DisplayName("Person Delete (성공), Space 삭제 확인")
	public void deletePersonTestCheckSpaceRemove() {
		//Given
		Space spaceForPerson = person.getSpace();
		Long spaceId = spaceForPerson.getId();
		
		//When
		personService.delete(person);
		
		//Then
		assertFalse(spaceRepository.existsById(spaceId));
	}
}
