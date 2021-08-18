package com.gunyoung.info.util;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.info.domain.Person;

/**
 * Test 클래스 전용 Person 엔티티 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class PersonTest {
	
	/**
	 * 임의의 Person 인스턴스 반환
	 * @param email 생성하려는 Person의 email
	 * @author kimgun-yeong
	 */
	public static Person getPersonInstance(String email) {
		Person person = Person.builder()
				.email(email)
				.password("abcd1234")
				.firstName("스트")
				.lastName("테")
				.build();
		
		return person;
	}
	
	/**
	 * Repository에 저장되지 않은 Person의 ID 반환
	 * @author kimgun-yeong
	 */
	public static Long getNonExistPersonId(JpaRepository<Person, Long> repository) {
		Long nonExistPersonId = Long.valueOf(1);
		
		for(Person p : repository.findAll()) {
			nonExistPersonId = Math.max(nonExistPersonId, p.getId());
		}
		
		nonExistPersonId++;
		
		return nonExistPersonId;
	}
}
