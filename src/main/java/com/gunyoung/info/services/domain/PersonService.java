package com.gunyoung.info.services.domain;

import java.util.List;

import org.springframework.data.domain.Page;

import com.gunyoung.info.domain.Person;

public interface PersonService {
	
	/**
	 * ID로 Person 찾기 
	 * @param id 찾으려는 Person의 Id
	 * @return Person, null (해당 Id의 Person 없을 경우)
	 * @author kimgun-yeong
	 */
	public Person findById(Long id);
	
	/**
	 * email로 Person 찾기 
	 * @param email 찾으려는 Person의 email
	 * @return Person, null (해당 email의 Person 없을 경우)
	 * @author kimgun-yeong
	 */
	public Person findByEmail(String email);
	
	/**
	 * email로 Person 찾기 <br>
	 * Space와 페치조인
	 * @param email 찾으려는 Person의 email
	 * @return Person, null (해당 email의 Person 없을 경우)
	 * @author kimgun-yeong
	 */
	public Person findByEmailWithSpace(String email);
	
	/**
	 * 모든 Person 리스트 반환
	 * @author kimgun-yeong
	 */
	public List<Person> findAll();
	
	/**
	 * 모든 Peraon 리스트 반환 <br>
	 * 생성 오래된순으로 정렬
	 * @author kimgun-yeong
	 */
	public List<Person> findAllOrderByCreatedAtDesc();
	
	/**
	 * 모든 Person 페이지 반환
	 * @author kimgun-yeong
	 */
	public Page<Person> findAllInPage(Integer pageNumber);
	
	/**
	 * 모든 Person 페이지 반환 <br>
	 * 생성 오래된순으로 정렬
	 * @author kimgun-yeong
	 */
	public Page<Person> findAllOrderByCreatedAtDescInPage(Integer pageNumber);
	
	/**
	 * 모든 Person 페이지 반환 <br>
	 * 생성 최신순으로 정렬
	 * @author kimgun-yeong
	 */
	public Page<Person> findAllOrderByCreatedAtAscInPage(Integer pageNumber);
	
	/**
	 * 이름 키워드를 만족하는 모든 Person 페이지 반환
	 * @param keyword Person들의 name 검색 키워드
	 * @author kimgun-yeong 
	 */
	public Page<Person> findByNameKeywordInPage(String keyword);
	
	/**
	 * Person 생성 및 수정
	 * @param person 저장하려는 Person
	 * @return 저장된 Person
	 * @author kimgun-yeong
	 */
	public Person save(Person person);
	
	/**
	 * Person 삭제 
	 * @param person 삭제하려는 Person
	 * @author kimgun-yeong
	 */
	public void delete(Person person);
	
	/**
	 * 모든 Person 개수 반환
	 * @author kimgun-yeong
	 */
	public long countAll();
	
	/**
	 * 이름 키워드를 만족하는 모든 Person 개수 반환
	 * @param keyword Person들의 name 검색 키워드
	 * @author kimgun-yeong
	 */
	public long countWithNameKeyword(String keyword);
	
	/**
	 * email로 Person 존재 여부 반환
	 * @param email 존재 여부 확인하려는 Person의 email
	 * @author kimgun-yeong
	 */
	public boolean existsByEmail(String email);
}
