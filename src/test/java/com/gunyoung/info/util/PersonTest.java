package com.gunyoung.info.util;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.enums.RoleType;

/**
 * Test 클래스 전용 Person 엔티티 관련 유틸리티 클래스
 * @author kimgun-yeong
 *
 */
public class PersonTest {
	
	public static final String DEFAULT_PERSON_EMAIL = "test@test.com";
	
	/**
	 * 임의의 Person 인스턴스 반환
	 * @author kimgun-yeong
	 */
	public static Person getPersonInstance() {
		return getPersonInstance(DEFAULT_PERSON_EMAIL);
	}
	
	/**
	 * 임의의 Person 인스턴스 반환 <br>
	 * email 커스터마이징 가능
	 * @author kimgun-yeong
	 */
	public static Person getPersonInstance(String email) {
		return getPersonInstance(email, RoleType.USER);
	}
	
	/**
	 * 임의의 Person 인스턴스 반환 <br>
	 * role 커스터마이징 가능
	 * @author kimgun-yeong
	 */
	public static Person getPersonInstance(RoleType role) {
		return getPersonInstance(DEFAULT_PERSON_EMAIL, role);
	}
	
	/**
	 * 임의의 Person 인스턴스 반환 <br>
	 * email, role 커스터마이징 가능
	 * @author kimgun-yeong
	 */
	public static Person getPersonInstance(String email, RoleType role) {
		Person person = Person.builder()
				.email(email)
				.password("abcd1234")
				.firstName("스트")
				.lastName("테")
				.role(role)
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
