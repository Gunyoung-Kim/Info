package com.gunyoung.info.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.repos.ContentRepository;
import com.gunyoung.info.repos.PersonRepository;
import com.gunyoung.info.repos.SpaceRepository;
import com.gunyoung.info.services.domain.SpaceService;
import com.gunyoung.info.testutil.Integration;
import com.gunyoung.info.util.ContentTest;
import com.gunyoung.info.util.PersonTest;
import com.gunyoung.info.util.SpaceTest;

/**
 * {@link SpaceService} 에 대한 테스트 클래스 <br>
 * 테스트 범위:(통합 테스트) 서비스 계층 - 영속성 계층 
 * @author kimgun-yeong
 *
 */
@Integration
@SpringBootTest
public class SpaceServiceTest {
	
	@Autowired
	SpaceRepository spaceRepository;
	
	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	ContentRepository contentRepository;
	
	@Autowired
	SpaceService spaceService;
	
	private Space space;
	
	@BeforeEach
	void setup() {
		space = SpaceTest.getSpaceInstance();
		spaceRepository.save(space);
	}
	
	@AfterEach
	void tearDown() {
		spaceRepository.deleteAll();
	}
	
	/*
	 *   public Space save(Space space);
	 */
	
	@Test
	@DisplayName("Space save (성공, 수정)")
	public void saveSpaceTest() {
		//Given
		String changeDescription = "changeDescription";
		space.setDescription(changeDescription);
		
		Long spaceId = space.getId();
		
		//When
		spaceService.save(space);
		
		//Then
		assertEquals(changeDescription, spaceRepository.findById(spaceId).get().getDescription());
	}
	
	/*
	 * public void deleteByPersonId(Long personId)
	 */
	
	@Test
	@Transactional
	@DisplayName("Person ID로 Space 삭제 -> 정상, Space 삭제 확인")
	public void deleteByPersonIdTestCheckSpaceRemove() {
		//Given
		System.out.println("sfafsa");
		Person personForSpace = addNewPersonForSpace();
		Long personId = personForSpace.getId();
		Long spaceId = space.getId();
		
		//When
		spaceService.deleteByPerson(personId);
		
		//Then
		assertFalse(spaceRepository.existsById(spaceId));
		System.out.println("sfafsa");
	}
	
	@Test
	@Transactional
	@DisplayName("Person ID로 Space 삭제 -> 정상, 관련 Content 삭제 확인")
	public void deleteByPersonIdTestCheckContentsRemove() {
		//Given
		int newContentsNum = 10;
		addNewContentsForSpace(newContentsNum);
		
		Person personForSpace = addNewPersonForSpace();
		Long personId = personForSpace.getId();
		long beforeContentNum = contentRepository.count();
		
		//When
		spaceService.deleteByPerson(personId);
		
		//Then
		assertEquals(beforeContentNum - newContentsNum, contentRepository.count());
	}
	
	private List<Content> addNewContentsForSpace(int newContentsNum) {
		List<Content> newContents = new ArrayList<>();
		for(int i=0; i < newContentsNum; i++) {
			Content content = ContentTest.getContentInstance();
			content.setSpace(space);
			newContents.add(content);
		}
		contentRepository.saveAll(newContents);
		return newContents;
	}
	
	private Person addNewPersonForSpace() {
		Person person = PersonTest.getPersonInstance();
		person.setSpace(space);
		personRepository.save(person);
		
		space.setPerson(person);
		spaceRepository.save(space);
		return person;
	}
}
