package com.gunyoung.info.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.repos.PersonRepository;
import com.gunyoung.info.repos.SpaceRepository;
import com.gunyoung.info.services.domain.ContentService;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.services.domain.SpaceService;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class SpaceServiceTest {
	
	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	SpaceRepository spaceRepository;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	ContentService contentService;
	
	@Autowired
	SpaceService spaceService;
	
	@BeforeEach
	void setup() {
		// 유저 등록
		if(!personService.existsByEmail("test@google.com")) {
			Person person = new Person();
			person.setEmail("test@google.com");
			person.setPassword("abcd1234");
			person.setFirstName("스트");
			person.setLastName("테");
										
			personService.save(person);
			
			// space 내용 설정
			Space space = person.getSpace();
			space.setDescription("test용 자기소개입니다.");
			space.setGithub("https://github.com/Gunyoung-Kim");
			
			// content 들 설정
			int contentsNumber = 1;
			for(int i=0;i<=contentsNumber;i++) {
				Content content = new Content();
				content.setTitle(i+" 번째 타이틀");
				content.setDescription(i+" 번째 프로젝트 설명");
				content.setContributors(i+" 번째 기여자들");
				content.setContents(i+ " 번째 프로젝트 내용");
				content.setSpace(space);
				contentService.save(content);
				
				space.getContents().add(content);
			}
			
			spaceService.save(space);
		}
		//2번쨰 유저 등록
		if(!personService.existsByEmail("second@naver.com")) {
			Person person2 = new Person();				
			person2.setEmail("second@naver.com");	
			person2.setPassword("abcd1234");
			person2.setFirstName("로그");
			person2.setLastName("블");
							
			// space 내용 설정
			Space space2 = person2.getSpace();
			space2.setDescription("test2222용 자기소개입니다.");
			space2.setGithub("https://github.com/Gunyoung-Kim");
							
			personService.save(person2);
		}	
	}
	
	/*
	 *  - 대상 메소드:
	 *  	 public Space save(Space space);
	 */
	
	@Test
	@Transactional
	@DisplayName("Space save (성공)")
	public void saveSpaceTest() {
		// space 수정 사항 반영
		Person person = personRepository.findByEmail("test@google.com").get();
		Space space = person.getSpace();
		Long spaceId = space.getId();
		
		space.setDescription("changed");
		
		spaceService.save(space);
		
		assertEquals(spaceRepository.getById(spaceId).getDescription(),"changed");
	}
	
}
