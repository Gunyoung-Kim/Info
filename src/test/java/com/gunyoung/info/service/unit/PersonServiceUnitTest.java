package com.gunyoung.info.service.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.repos.PersonRepository;
import com.gunyoung.info.services.domain.PersonServiceImpl;

/**
 * {@link PersonServiceImpl}에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) Service class only <br>
 * {@link org.mockito.BDDMockito}를 활용한 서비스 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class PersonServiceUnitTest {
	
	@Mock
	PersonRepository personRepository;
	
	@InjectMocks
	PersonServiceImpl personService;
	
	private Person person;
	
	@BeforeEach
	void setup() {
		person = new Person();
	}
	
	/*
	 * public Content findById(Long id)
	 */
	
	@Test
	@DisplayName("ID로 Person 찾기 -> 존재하지 않음")
	public void findByIdNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(personRepository.findById(nonExistId)).willReturn(Optional.empty());
		
		//When
		Person result = personService.findById(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("ID로 Person 찾기 -> 정상")
	public void findByIdTest() {
		//Given
		Long personId = Long.valueOf(1);
		
		given(personRepository.findById(personId)).willReturn(Optional.of(person));
		
		//When
		Person result = personService.findById(personId);
		
		//Then
		assertEquals(person, result);
	}
	
	/*
	 * public Content findByEmail(Long id)
	 */
	
	@Test
	@DisplayName("Email로 Person 찾기 -> 존재하지 않음")
	public void findByEmailNonExist() {
		//Given
		String nonExistEmail = "nonexist@test.com";
		
		given(personRepository.findByEmail(nonExistEmail)).willReturn(Optional.empty());
		
		//When
		Person result = personService.findByEmail(nonExistEmail);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("Email로 Person 찾기 -> 정상")
	public void findByEmailTest() {
		//Given
		String personEmail = "test@test.com";
		
		given(personRepository.findByEmail(personEmail)).willReturn(Optional.of(person));
		
		//When
		Person result = personService.findByEmail(personEmail);
		
		//Then
		assertEquals(person, result);
	}
	
	/*
	 * public Content findByEmailWithSpace(Long id)
	 */
	
	@Test
	@DisplayName("Email로 Person 찾기, Space 페치 조인 -> 존재하지 않음")
	public void findByEmailWithSpaceNonExist() {
		//Given
		String nonExistEmail = "nonexist@test.com";
		
		given(personRepository.findByEmailWithSpace(nonExistEmail)).willReturn(Optional.empty());
		
		//When
		Person result = personService.findByEmailWithSpace(nonExistEmail);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("Email로 Person 찾기, Space 페치 조인 -> 정상")
	public void findByEmailWithSpaceTest() {
		//Given
		String personEmail = "test@test.com";
		
		given(personRepository.findByEmailWithSpace(personEmail)).willReturn(Optional.of(person));
		
		//When
		Person result = personService.findByEmailWithSpace(personEmail);
		
		//Then
		assertEquals(person, result);
	}
	
	/*
	 * public Content findByEmailWithSpaceAndContents(Long id)
	 */
	
	@Test
	@DisplayName("Email로 Person 찾기, Space, Contents 페치 조인 -> 존재하지 않음")
	public void findByEmailWithSpaceAndContentsNonExist() {
		//Given
		String nonExistEmail = "nonexist@test.com";
		
		given(personRepository.findByEmailWithSpaceAndContents(nonExistEmail)).willReturn(Optional.empty());
		
		//When
		Person result = personService.findByEmailWithSpaceAndContents(nonExistEmail);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("Email로 Person 찾기, Space, Contents 페치 조인 -> 정상")
	public void findByEmailWithSpaceAndContentsTest() {
		//Given
		String personEmail = "test@test.com";
		
		given(personRepository.findByEmailWithSpaceAndContents(personEmail)).willReturn(Optional.of(person));
		
		//When
		Person result = personService.findByEmailWithSpaceAndContents(personEmail);
		
		//Then
		assertEquals(person, result);
	}
	
	/*
	 * public Content findByIdWithSpaceAndContents(Long id)
	 */
	
	@Test
	@DisplayName("Id로 Person 찾기, Space, Contents 페치 조인 -> 존재하지 않음")
	public void findByIdWithSpaceAndContentsNonExist() {
		//Given
		Long nonExistId = Long.valueOf(1);
		
		given(personRepository.findByIdWithSpaceAndContents(nonExistId)).willReturn(Optional.empty());
		
		//When
		Person result = personService.findByIdWithSpaceAndContents(nonExistId);
		
		//Then
		assertNull(result);
	}
	
	@Test
	@DisplayName("Id로 Person 찾기, Space, Contents 페치 조인 -> 정상")
	public void findByIdWithSpaceAndContentsTest() {
		//Given
		Long personId = Long.valueOf(1);
		
		given(personRepository.findByIdWithSpaceAndContents(personId)).willReturn(Optional.of(person));
		
		//When
		Person result = personService.findByIdWithSpaceAndContents(personId);
		
		//Then
		assertEquals(person, result);
	}
	
	/*
	 * public List<Person> findAll()
	 */
	
	@Test
	@DisplayName("모든 Person 찾기 -> 정상")
	public void findAllTest() {
		//Given
		List<Person> people = new ArrayList<>();
		
		given(personRepository.findAll()).willReturn(people);
		
		//When
		List<Person> result = personService.findAll();
		
		//Then
		assertEquals(people, result);
	}
	
	/*
	 * public List<Person> findAllOrderByCreatedAtDesc()
	 */
	
	@Test
	@DisplayName("모든 Person 찾기, 생성 오래된순으로 정렬 -> 정상")
	public void findAllOrderByCreatedAtDescTest() {
		//Given
		List<Person> people = new ArrayList<>();
		
		given(personRepository.findAllByOrderByCreatedAtDesc()).willReturn(people);
		
		//When
		List<Person> result = personService.findAllOrderByCreatedAtDesc();
		
		//Then
		assertEquals(people, result);
	}
	
	/*
	 * public Page<Person> findAllInPage(Integer pageNumber)
	 */
	
	@Test
	@DisplayName("모든 Person 페이지 반환 -> 정상")
	public void findAllInPageTest() {
		//Given
		int pageNum = 1;
		
		//When
		personService.findAllInPage(pageNum);
		
		//Then
		then(personRepository).should(times(1)).findAll(any(PageRequest.class));
	}
	
	/*
	 * public Page<Person> findAllOrderByCreatedAtDescInPage(Integer pageNumber)
	 */
	
	@Test
	@DisplayName("모든 Person 페이지 반환, 생성 오래된 순으로 정렬 -> 정상")
	public void findAllOrderByCreatedAtDescInPageTest() {
		//Given
		int pageNum = 1;
		
		//When
		personService.findAllOrderByCreatedAtDescInPage(pageNum);
		
		//Then
		then(personRepository).should(times(1)).findAllByOrderByCreatedAtDesc(any(PageRequest.class));
	}
	
	/*
	 * public Page<Person> findAllOrderByCreatedAtAscInPage(Integer pageNumber)
	 */
	
	@Test
	@DisplayName("모든 Person 페이지 반환, 생성 최신 순으로 정렬 -> 정상")
	public void findAllOrderByCreatedAtAscInPageTest() {
		//Given
		int pageNum = 1;
		
		//When
		personService.findAllOrderByCreatedAtAscInPage(pageNum);
		
		//Then
		then(personRepository).should(times(1)).findAllByOrderByCreatedAtAsc(any(PageRequest.class));
	}
	
	/*
	 * public Page<Person> findByNameKeywordInPage(Integer pageNumber)
	 */
	
	@Test
	@DisplayName("이름 검색 키워드 만족하는 모든 Person 페이지 반환 -> 정상")
	public void findByNameKeywordInPageTest() {
		//Given
		int pageNum = 1;
		String keyword = "keyword";
		
		//When
		personService.findByNameKeywordInPage(pageNum, keyword);
		
		//Then
		then(personRepository).should(times(1)).findByNameWithKeyword(anyString(),any(PageRequest.class));
	}
	
	/*
	 * public Person save(Person person)
	 */
	
	@Test
	@DisplayName("Person 생성 및 수정 -> 정상")
	public void saveTest() {
		//Given
		given(personRepository.save(person)).willReturn(person);
		
		//When
		Person result = personService.save(person);
		
		//Then
		assertEquals(person, result);
	}
	
	/*
	 *  public void delete(Person person)
	 */
	
	@Test
	@DisplayName("Person 삭제 -> 정상")
	public void deleteTest() {
		//Given
		
		//When
		personService.delete(person);
		
		//Then
		then(personRepository).should(times(1)).delete(person);
	}
	
	/*
	 * public boolean existsByEmail(String email)
	 */
	
	@Test
	@DisplayName("Email로 Person 존재 여부 반환 -> true")
	public void existsByEmailTrueTest() {
		//Given
		String existEmail = "test@test.com";
		boolean isExist = true;
		
		given(personRepository.existsByEmail(existEmail)).willReturn(isExist);
		
		//When
		boolean result = personService.existsByEmail(existEmail);
		
		//Then
		assertEquals(isExist, result);
	}
	
	@Test
	@DisplayName("Email로 Person 존재 여부 반환 -> false")
	public void existsByEmailFalseTest() {
		//Given
		String nonExistEmail = "nonexist@test.com";
		boolean isExist = false;
		
		given(personRepository.existsByEmail(nonExistEmail)).willReturn(isExist);
		
		//When
		boolean result = personService.existsByEmail(nonExistEmail);
		
		//Then
		assertEquals(isExist, result);
	}
	
	/*
	 * public long countAll()
	 */
	
	@Test
	@DisplayName("모든 Person 개수 세기 -> 정상")
	public void countAllTest() {
		//Given
		long num = 1;
		
		given(personRepository.count()).willReturn(num);
		
		//When
		long result = personService.countAll();
		
		//Then
		assertEquals(num, result);
	}
	
	/*
	 * public long countWithNameKeyword(String keyword)
	 */
	
	@Test
	@DisplayName("이름 키워드를 만족하는 모든 Person 개수 반환 -> 정상")
	public void countWithNameKeywordTest() {
		//Given
		String keyword = "keyword";
		long num = 1;
		
		given(personRepository.countWithNameKeyword(keyword)).willReturn(num);
		
		//When
		long result = personService.countWithNameKeyword(keyword);
		
		//Then
		assertEquals(num, result);
	}
}
