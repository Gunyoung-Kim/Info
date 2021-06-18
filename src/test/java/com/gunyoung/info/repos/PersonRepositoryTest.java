package com.gunyoung.info.repos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.enums.RoleType;

/**
 * JpaRepository 상속 클래스에서 직접 쿼리 작성한 메소드에 대한 테스트 코드 작성
 * @author kimgun-yeong
 *
 */
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class PersonRepositoryTest {
	
	@Autowired
	PersonRepository personRepository; 
	
	private static final int PAGE_SIZE = 10;
	
	private static int INPUT_NUMBER = 30;
	
	@BeforeEach
	void setup() {
		List<Person> personList = new LinkedList<>();
		for(int i=1;i<=INPUT_NUMBER;i++) {
			Person person = new Person();
			person.setEmail(i+"_email@test.com");
			person.setPassword("abcd1234");
			person.setFirstName(i+"번째");
			person.setLastName("사람");
			person.setRole(RoleType.USER);
			
			personList.add(person);
		}
		
		personRepository.saveAll(personList);
	}
	
	@AfterEach
	void tearDown() {
		personRepository.deleteAll();
	}
	
	/**
	 * 
	 * @author kimgun-yeong
	 */
	@Test
	@DisplayName("이름 검색으로 Person 가져오는 쿼리")
	public void getByNameWithKeywordTest() {
		List<Person> resultList;
		
		System.out.println("asdd");
		resultList = personRepository.getByNameWithKeyword(INPUT_NUMBER + "번째", getPageRequest(1)).getContent();
		
		assertEquals(resultList.size(),1);
	}
	
	/**
	 * 
	 * @author kimgun-yeong
	 */
	@Test
	@DisplayName("이름 검색으로 Person 가져오는 쿼리 (like문 제대로 적용되었는지)")
	public void getByNameWithKeywordTestLike() {
		List<Person> resultList;
		
		resultList = personRepository.getByNameWithKeyword("번째",getPageRequest(1)).getContent();
		
		assertEquals(resultList.size(),PAGE_SIZE);
	}
	
	/**
	 * @author kimgun-yeong
	 */
	@Test
	@DisplayName("이름 검색으로 해당하는 Person 개수 가져오는 쿼리")
	public void countWithNameKeyword() {
		long result;
		
		result = personRepository.countWithNameKeyword("번째");
		
		assertEquals(result,INPUT_NUMBER);
	}
	
	/**
	 * 
	 * @param pageNumber
	 * @return
	 */
	private Pageable getPageRequest(Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, PAGE_SIZE);
		return pageRequest;
	}
}
